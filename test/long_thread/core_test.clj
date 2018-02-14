(ns long-thread.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [long-thread.core :as long-thread]
            [long-thread.leak :as leak]))

(deftest test-start-join
  (leak/checking
    (testing "A thread can be started and stopped"
      (let [my-promise (promise)
            my-thread  (long-thread/start "A test thread" #(deliver my-promise :ok))]
        (long-thread/join my-thread)
        (is (= (deref my-promise 10000 :timeout) :ok))))))

(def ^:dynamic *dynamic-var* nil)

(deftest test-dynamic-vars-are-conveyed
  (leak/checking
    (binding [*dynamic-var* (Object.)]
      (let [my-promise (promise)
            my-thread (long-thread/start "Dynamic thread" #(deliver my-promise *dynamic-var*))]
        (is (= (deref my-promise 10000 :timeout) *dynamic-var*))))))

(deftest test-until-interrupted
  (leak/checking
    (testing "A thread keeps running until it's interrupted"
      (let [my-promise (promise)
            runnable   #(do (long-thread/until-interrupted (Thread/sleep 1000))
                            (deliver my-promise :ok))
            my-thread  (long-thread/start "Another thread" runnable)]
        (is (long-thread/alive? my-thread))
        (long-thread/stop my-thread)
        (is (not (long-thread/alive? my-thread)))
        (is (= (deref my-promise 10000 :timeout) :ok))))))

(defn- do-nothing
  []
  (long-thread/until-interrupted
    (Thread/sleep 1000)))

(deftest test-threads-by-name
  (leak/checking
    (let [threads (set (for [_ (range 3)]
                         (long-thread/start "threads-by-name test" do-nothing)))]
      (try
        (is (= (set (long-thread/threads-by-name "threads-by-name test")) threads))
        (finally
          (run! long-thread/stop threads))))))
