(ns zentai.backup
  "Backup Elasticsearch indices using snapshot/restore APIs check:
     https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-snapshots.html"
  (:require [zentai.core :refer (put-call get-call)]))

(defn create-repository
  "Create a snapshot repository"
  [repo location]
  (put-call [:_snapshot repo] {:type "fs"
                               :settings {:location location}}))

(defn get-repository
  "Get repository information"
  [repo]
  (get-call [:_snapshot repo]))

(defn create-snapshot
  "snapshot Elasticsearch"
  [repo snap]
  (put-call [:_snapshot repo snap] {}))

(defn get-snapshot
  "Get a snapshot information"
  [repo snap]
  (get-call [:_snapshot repo snap]))

(defn restore-snapshot
  "Restore a snapshot "
  [repo snap])
(comment
  (create-repo "ronen" "backup-2")
  (get-repo "ronen")
  (snapshot "ronen" "1")
  (get-snapshot "ronen" "1"))
