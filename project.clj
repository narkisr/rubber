(defproject rubber "0.4.2"
  :description "Elasticsearch common functions, snapshot and index management"
  :url "https://github.com/narkisr/rubber"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
     [org.clojure/clojure "1.10.1"]

     ; string interpulation
     [org.clojure/core.incubator "0.1.4"]

     ; logging
     [com.taoensso/timbre "4.10.0"]
     [com.fzakaria/slf4j-timbre "0.3.19"]
     [timbre-ns-pattern-level "0.1.2"]

     ; pretty output
     [narkisr/clansi "1.2.0"]

     ; time
     [clj-time/clj-time "0.15.2"]

     ; es
     [cc.qbits/spandex "0.8.2" :exclusions [org.clojure/clojure org.clojure/core.async]]
     [org.clojure/core.async "1.2.603"]
     [com.brunobonacci/safely "0.5.0"]
   ]

   :profiles {
     :dev {
       :source-paths  ["dev"]
       :dependencies [
          ; repl
          [org.clojure/tools.namespace "1.0.0"]
        ]
      }
    }

   :plugins [
     [lein-cljfmt "0.5.6"]
     [lein-ancient "0.6.15" :exclusions [org.clojure/clojure]]
     [lein-tag "0.1.0"]
     [lein-set-version "0.3.0"]]


   :repl-options {
    :init-ns user
    :prompt (fn [ns] (str "\u001B[35m[\u001B[34m" "rubber" "\u001B[35m]\u001B[33mλ:\u001B[m " ))
   }


   :aliases {
     "travis" [
        "do" "clean," "compile," "cljfmt" "check," "test"
     ]
   }

)
