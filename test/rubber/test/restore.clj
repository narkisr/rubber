(ns rubber.test.restore
  "Snapshot/restore testing"
  (:require
   [rubber.test.common :refer (setup gen-uuid)]
   [rubber.core :as r]
   [rubber.backup :as b]
   [clojure.test :refer (deftest is use-fixtures)]))

(use-fixtures :once setup)

(defn has-snapshot? [id {:keys [snapshots]}]
  (first (filter #(= id (% :snapshot)) snapshots)))

(deftest create-repository
  (let [snapshot (gen-uuid)]
    (is (= 200 (b/create-repository :backup-1 "backup-1")))
    (is (contains? (b/list-repositories) :backup-1))
    (is (= 200 (b/create-snapshot :backup-1 snapshot)))
    (is (has-snapshot? snapshot (b/get-snapshot :backup-1 snapshot)))))
