(ns zenati.component
  "Component friendly functions"
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [zenati.common :as common :refer (get-es! exists? create-index)]))

(refer-timbre)

(defn initialize
  "Creates systems index and types"
  [parent types]
  (doseq [[k t] types]
    (let [idx (common/index parent k)]
      (when-not (exists? idx)
        (info "Creating index" idx)
        (create-index idx {:mappings {k t}}))
      (clean/setup-index-jobs parent {k t}))))
