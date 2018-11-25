(ns rubber.backup
  "Backup Elasticsearch indices using snapshot/restore APIs check:
     https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-snapshots.html"
  (:require [rubber.core :refer (call)]))

(defn create-repository
  "Create a snapshot repository"
  [repo location]
  (call :put [:_snapshot repo] {:type "fs" :settings {:location location}}))

(defn get-repository
  "Get repository information"
  [repo]
  (call :get [:_snapshot repo]))

(defn create-snapshot
  "snapshot Elasticsearch"
  [repo snap]
  (call :put [:_snapshot repo snap] {}))

(defn get-snapshot
  "Get a snapshot information"
  [repo snap]
  (call :get [:_snapshot repo snap]))

(defn list-repositories
  "List available repositories"
  []
  (:body (call :get [:_snapshot :_all])))

(defn list-snapshots
  "List available repositories"
  [repo]
  (:body (call :get [:_snapshot repo :_all])))

(defn restore-snapshot
  "Restore a snapshot "
  [repo snap]
  (call :post [:_snapshot repo snap :_restore]))

