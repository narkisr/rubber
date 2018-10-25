(defproject zenati "0.1.0"
  :description "Comfort functions for working against Elasticsearch using Spandex"
  :url "https://github.com/narkisr/zenati"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
     [org.clojure/clojure "1.9.0"]

     ; string interpulation
     [org.clojure/core.incubator "0.1.4"]

     ; logging
     [com.taoensso/timbre "4.10.0"]
     [com.fzakaria/slf4j-timbre "0.3.8"]
     [timbre-ns-pattern-level "0.1.2"]

     ; serialization
     [cheshire "5.7.1"]

     ; pretty output
     [narkisr/clansi "1.2.0"]

     ; es
     [cc.qbits/spandex "0.6.4" :exclusions [org.clojure/clojure]]
     [com.brunobonacci/safely "0.2.4"]
   ]

   :profiles {
     :dev {
       :source-paths  ["dev"]
       :dependencies [ 
          ; repl
          [org.clojure/tools.nrepl "0.2.10"]
          [io.aviso/pretty "0.1.34"]
        ]
      }
    }

   :plugins [
     [jonase/eastwood "0.2.4"]
     [lein-cljfmt "0.5.6"]
     [lein-ancient "0.6.15" :exclusions [org.clojure/clojure]]
     [lein-tag "0.1.0"]
     [lein-set-version "0.3.0"]]


   :repl-options {
    :init-ns user
    :prompt (fn [ns] (str "\u001B[35m[\u001B[34m" "re-mote" "\u001B[35m]\u001B[33mλ:\u001B[m " ))
    :welcome (println "Welcome to re-mote!" )
   }
   

   :aliases {
     "travis" [
        "do" "clean," "compile," "cljfmt" "check," "eastwood"
     ]
   }

  :signing {:gpg-key "narkisr@gmail.com"}
)
