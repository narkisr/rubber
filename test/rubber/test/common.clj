(ns rubber.test.common
  "common testing utlities"
  (:require
   [rubber.node :as n :refer (stop connect)]))

(defn setup [f]
  (connect {:hosts ["http://localhost:9200"]})
  (f))

(def types
  {:mappings {:person {:properties {:name {:type "text"}}}}})

(defn gen-uuid []
  (.replace (str (java.util.UUID/randomUUID)) "-" ""))

