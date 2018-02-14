(ns long-thread.leak
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [long-thread.core :as long-thread]))

(defn ^:no-doc report-leaked [leaked]
  (let [thread-names (map #(.getName ^Thread %) leaked)
        message (str "Leaked threads: " (str/join ", " thread-names))]
    (throw (ex-info message {:type ::thread-leak, :leaked leaked}))))

(defmacro checking
  "Check for thread leaks.

  The body is run in an implicit do block. When exiting the block, if there are
  threads running that weren't running when entering the block, an exception is
  thrown.

  This is mainly useful for checking that thread-creating tests close their
  threads afterwards. Note that if you run thread-creating tests in parallel,
  you'll get false positives!

  The exception is an ExceptionInfo with the following keys:

  :type    -- always has value :long-thread.leak/thread-leak
  :leaked  -- the set of leaked Thread objects"
  [& body]
  `(let [threads# (set (long-thread/running-threads))]
     ~@body
     (some-> (not-empty (set/difference (set (long-thread/running-threads)) threads#))
             (report-leaked))))
