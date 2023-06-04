;; SPDX-FileCopyrightText: 2018 Miikka Koskinen
;; SPDX-License-Identifier: EPL-2.0
(defproject miikka/long-thread "0.3.1-SNAPSHOT"
  :description "Long-running threads in your Clojure"
  :url "https://github.com/miikka/long-thread"
  :license {:name "EPL-2.0", :url "https://www.eclipse.org/legal/epl-2.0/"}
  :scm {:name "git", :url "https://github.com/miikka/long-thread"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :plugins [[lein-cloverage "1.1.2"]]
  :deploy-repositories [["releases" :clojars]]
  :global-vars {*warn-on-reflection* true})
