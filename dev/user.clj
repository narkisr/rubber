(ns user
  (:require
   [clojure.repl :refer :all]
   [rubber.core :refer :all]
   [rubber.node :refer :all :exclude (stop)]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (rubber.node/stop))

(defn go
  "Initializes the current development system and starts it running."
  []
  (connect {:hosts ["http://localhost:9200"]}))

(defn reset []
  (stop)
  (refresh :after 'user/go))

(defn clrs
  "clean repl"
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))
