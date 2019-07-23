(defproject rubber "0.3.6"
  :description "Elasticsearch common functions, snapshot and index management"
  :url "https://github.com/narkisr/rubber"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
     [org.clojure/clojure "1.10.0"]

     ; string interpulation
     [org.clojure/core.incubator "0.1.4"]

     ; logging
     [com.taoensso/timbre "4.10.0"]
     [com.fzakaria/slf4j-timbre "0.3.8"]
     [timbre-ns-pattern-level "0.1.2"]

     ; pretty output
     [narkisr/clansi "1.2.0"]

     ; time
     [clj-time/clj-time "0.14.2"]

     ; es
     [cc.qbits/spandex "0.6.4" :exclusions [org.clojure/clojure]]
     [com.brunobonacci/safely "0.2.4"]
   ]

   :profiles {
     :dev {
       :source-paths  ["dev"]
       :dependencies [
          ; repl
          [org.clojure/tools.namespace "0.2.11"]
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

  :signing {:gpg-key "narkisr@gmail.com"}

)
