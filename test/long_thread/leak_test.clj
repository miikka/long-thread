(ns long-thread.leak-test
  (:require  [clojure.test :refer [deftest do-report is testing]]
             [long-thread.core :as long-thread]
             [long-thread.leak :as leak])
  (:import clojure.lang.ExceptionInfo))

(defn- do-nothing []
  (long-thread/until-interrupted
    (Thread/sleep 1000)))

(defn- drf [x] (deref x 10000 nil))

(deftest test-leak-checking
  (testing "thread leaks are caught"
    (let [thread-promise (promise)]
      (try
        (leak/checking
          (deliver thread-promise (long-thread/create "leaking thread" do-nothing)))
        (do-report {:type :fail, :message nil, :expected 'ExceptionInfo, :actual nil})
        (catch ExceptionInfo e
          (is (= "Leaked threads: leaking thread" (.getMessage e)))
          (is (= {:type ::leak/thread-leak, :leaked #{(drf thread-promise)}}
                 (ex-data e))))
        (finally
          (some-> (drf thread-promise) (long-thread/stop)))))))
