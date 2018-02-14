(ns long-thread.core-test
  (:require [clojure.test :refer :all]
            [long-thread.core :as long-thread]))

(deftest test-start-join
  (testing "A thread can be started and stopped"
    (let [my-promise (promise)
          my-thread  (long-thread/start "A test thread" #(deliver my-promise :ok))]
      (long-thread/join my-thread)
      (is (= (deref my-promise 10000 :timeout) :ok)))))

(deftest test-until-interrupted
  (testing "A thread keeps running until it's interrupted"
    (let [my-promise (promise)
          runnable   #(do (long-thread/until-interrupted (Thread/sleep 1000))
                          (deliver my-promise :ok))
          my-thread  (long-thread/start "Another thread" runnable)]
      (is (long-thread/alive? my-thread))
      (long-thread/stop my-thread)
      (is (not (long-thread/alive? my-thread)))
      (is (= (deref my-promise 10000 :timeout) :ok)))))
