(ns rubber.test.core
  "rubber core testing"
  (:require
   [rubber.test.common :refer (setup gen-uuid)]
   [rubber.core :as r]
   [rubber.node :as n]
   [clojure.test :refer (deftest is use-fixtures)]))

(use-fixtures :once setup)

(def types
  {:mappings {:properties {:name {:type "text"}}}})

(deftest index-creation
  (let [uuid (gen-uuid)
        idx (keyword (str "people-" uuid))]
    (is (= 200 (r/create-index idx types)))
    (is (= {idx types} (r/mappings idx)))
    (is (some #{(name idx)} (map :index (r/list-indices))))
    (is (r/exists? idx))
    (is (= 200 (r/delete-index idx)))
    (is (not (some #{(name idx)} (map :index (r/list-indices)))))
    (is (nil? (r/mappings idx)))))

(deftest core-functions
  (let [uuid (gen-uuid)
        idx (keyword (str "people-" uuid))]
    (is (= 200 (r/create-index idx types)))
    (is (= {idx types} (r/mappings idx)))
    (let [id (r/create idx {:name "joe"})]
      (is (string? id))
      (is (= 200 (r/exists? idx id)))
      (is (= 200 (r/delete idx id)))
      (is (not (r/exists? idx id))))
    (let [ids (r/bulk-create idx [{:name "joe"} {:name "foo"}])]
      (is (= (count (get ids true)) 2))
      (doseq [[id _] (get ids true)]
        (is (= 200 (r/exists? idx id)))))
    (is (= 200 (r/delete-index idx)))))

(deftest bulk-functions
  (let [uuid (gen-uuid)
        idx (keyword (str "people-" uuid))]
    (is (= 200 (r/create-index idx types)))
    (is (= {idx types} (r/mappings idx)))
    (let [id (r/bulk-create idx [{:name "joe"} {:name "bar"}])]
      ;; (is (string? id))
      ;; (is (= 200 (r/exists? idx :person id)))
      ;; (is (= 200 (r/delete idx :person id)))
      #_(is (not (r/exists? idx :person id))))
    #_(is (= 200 (r/delete-index idx)))))

(deftest searching
  (let [uuid (gen-uuid)
        idx (keyword (str "people-" uuid))]
    (is (= 200 (r/create-index idx types)))
    (is (= {idx types} (r/mappings idx)))
    (let [id-1 (r/create idx {:name "joe"})
          id-2 (r/create idx {:name "dave"})]
      (is (= 200 (r/refresh-index idx)))
      (is (= [[id-1 {:name "joe"}]] (r/search idx {:query {:match {:name "joe"}}}))))
    (is (= 200 (r/delete-index idx)))))

(deftest non-existing
  (is (thrown? clojure.lang.ExceptionInfo (r/refresh-index (gen-uuid)))))

(deftest health
  (is (= 200 (n/check))))
