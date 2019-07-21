(ns rubber.core
  "Common functions (put/get/post/delete)"
  (:refer-clojure :exclude (get))
  (:require
   [clojure.string :refer (split)]
   [taoensso.timbre :refer (refer-timbre)]
   [qbits.spandex :as s]
   [clj-time.core :as t]
   [clj-time.format :as f]
   [rubber.node :refer (connection)])
  (:import
   [org.elasticsearch.client ResponseException]
   java.io.StringWriter
   java.io.PrintWriter))

(refer-timbre)

(defn error-m [e]
  (let [sw (StringWriter.) p (PrintWriter. sw)]
    (.printStackTrace e p)
    (error (.getMessage e) (.toString sw))))

(defn ok [resp]
  (#{200 201} (:status resp)))

(defn- illegal [e]
  (instance? java.lang.IllegalStateException e))

(defn- reactor-stopped
  "Used when the connection is reset"
  [e]
  (let [c "Request cannot be executed; I/O reactor status: STOPPED"]
    (and (illegal e) (= (-> e Throwable->map :cause) c))))

(defn pretty-error
  "A pretty print error log"
  [m]
  (let [st (java.io.StringWriter.)]
    (binding [*out* st]
      (clojure.pprint/pprint m))
    (error (.toString st))))

(defn- handle-ex [e]
  (when-not (reactor-stopped e)
    (error-m e)
    (when (= (class e) ResponseException)
      (pretty-error (s/response-ex->response e)))
    (when-let [data (ex-data e)]
      (pretty-error data))
    (throw e)))

(defn missing?
  "A document missing error reponse"
  [verb e]
  (and (#{:get :head} verb) (= 404 (:status (ex-data e)))))

(defn call
  ([verb target]
   (try
     (s/request (connection) {:url target :method verb :query-string {:include_type_name true}})
     (catch Exception e
       (when-not (missing? verb e)
         (handle-ex e)))))
  ([verb target body]
   (try
     (s/request (connection) {:url target :method verb :body body :query-string {:include_type_name true}})
     (catch Exception e
       (when-not (missing? verb e)
         (handle-ex e))))))

; Core functions
(defn exists?
  "Check if index exists or instance with id existing within an index"
  ([index]
   (ok (call :head [index])))
  ([index t id]
   (ok (call :head [index t id]))))

(defn put [index t id m]
  (call :put [index t id] m))

(defn get
  "(get :people :person *1)"
  [index t id]
  (get-in (call :get [index t id]) [:body :_source]))

(defn bulk-get
  "Bulk get a list of documents with ids
    (bulk-get :people :person [*1 *2])"
  [index t ids]
  {:pre [(not (empty? ids))]}
  (let [{:keys [body] :as resp} (call :get [index t :_mget] {:ids ids})]
    (when (ok resp)
      (into {} (map (juxt :_id :_source) (filter :found (body :docs)))))))

(defn create
  "Persist instance m of and return generated id
     (create :people :person {:name \"john\"})
   "
  [index t m]
  (let [{:keys [status body] :as resp}  (call :post [index t] m)]
    (when-not (ok resp)
      (throw (ex-info "failed to create" {:resp resp :m m :index index})))
    (body :_id)))

(defn delete
  "Delete all under index or a single id"
  ([index t]
   (ok (call :delete [index t])))
  ([index t id]
   (ok (call :delete [index t id]))))

(defn delete-all
  [index]
  (ok (call :post [index :_delete_by_query] {:query {:match_all {}}})))

; Index functions
(def ^:const default-settings {:settings {:number_of_shards 1}})

(defn refresh-index
  "Refresh the index in order to get the lastest operations available for search"
  [index]
  (let [resp (call :post [index :_refresh])]
    (if-let [r (ok resp)]
      r
      (throw (ex-info "failed to refresh" {:resp resp :index index})))))

(defn create-index
  "Create an index with provided (single) mappings (since 6.X)
     (create-index :people {:mappings {:person {:properties {:name {:type \"text\"}}}}})"
  [index {:keys [mappings] :as spec}]
  {:pre [mappings]}
  (ok (call :put [index] (merge default-settings spec))))

(defn delete-index
  "Delete an index
     (delete-index :people)"
  [idx]
  (ok (call :delete [idx])))

(defn list-indices
  "List all available indices"
  []
  (let [ks [:health :status :index :uuid :pri :rep :docs.count :docs.deleted :store.size :pri.store.size]]
    (map #(zipmap ks (filter (comp not empty?) (split % #"\s")))
         (split (:body (call :get [:_cat :indices])) #"\n"))))

; Query functions
(defn all
  "An all query using match all on provided index this should use scrolling for 10K systems"
  [index]
  (let [query {:size 10000 :query {:match_all {}}}
        {:keys [body]} (call :get [index :_search] query)]
    (mapv (juxt :_id :_source) (get-in body [:hits :hits]))))

(defn search
  "An Elasticsearch search query"
  [index input]
  (let [{:keys [body] :as resp} (call :get [index :_search] input)]
    (when (ok resp)
      (mapv (juxt :_id :_source) (get-in body [:hits :hits])))))

(defn delete-by
  "Delete by query like {:match {:type \"nmap scan\"}}"
  [index t query]
  (call :delete  [index t :_delete_by_query]  {:query query}))

(def conn-prefix (atom :default))

(defn prefix-switch
  "Change Elasticsearch connection prefix (connect to another instance)"
  [k]
  (reset! conn-prefix k))

(defn mappings
  "get index mappings"
  [idx t]
  (:body (call :get [idx :_mappings t])))
