(defproject miikka/long-thread "0.3.1-SNAPSHOT"
  :description "Long-running threads in your Clojure"
  :url "https://github.com/miikka/long-thread"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :plugins [[lein-cloverage "1.1.2"]
            [lein-codox "0.10.7"]]
  :codox {:output-path "doc"
          :source-uri "https://github.com/miikka/long-thread/blob/{version}/{filepath}#L{line}"
          :metadata {:doc/format :markdown}}
  :deploy-repositories [["releases" :clojars]]
  :global-vars {*warn-on-reflection* true})
