(ns zenati.component
  "Component friendly functions"
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [zenati.core :refer (exists? create-index)]))

(refer-timbre)

(defn initialize
  "Creates systems index and types"
  [index parent types]
  (doseq [[k t] types]
    (let [idx (index parent k)]
      (when-not (exists? idx)
        (info "Creating index" idx)
        (create-index idx {:mappings {k t}})))))
