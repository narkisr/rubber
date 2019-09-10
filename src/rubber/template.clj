(ns rubber.template
  "Index template support see:
    https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-templates.html"
  (:require
   [rubber.core :refer (call ok)]))

(defn add-template [t patterns settings mappings]
  (ok
   (call :put [:_template t] {:index_patterns patterns :mappings mappings :settings settings})))

(defn get-templates
  "Get all defined templates"
  []
  (call :get [:_template]))

(defn delete-template [t]
  (ok (call :delete [:_template t])))

(defn template-exists? [t]
  (ok (call :head [:_template t] nil {:include_type_name true})))
