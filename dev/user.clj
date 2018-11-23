(ns user
  (:require
   [clojure.repl :refer :all]
   [zentai.core :refer :all]
   [zentai.node :refer :all :exclude (stop)]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]))

(defn stop
  "Shuts down and destroys the current development system."
  [])

(defn go
  "Initializes the current development system and starts it running."
  [])

(defn reset []
  (stop)
  (refresh :after 'user/go))

(defn clrs
  "clean repl"
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))
