;; SPDX-FileCopyrightText: 2018 Miikka Koskinen
;; SPDX-License-Identifier: EPL-2.0
(ns long-thread.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [long-thread.core :as long-thread]
            [long-thread.leak :as leak]))

(defn- do-nothing
  []
  (long-thread/until-interrupted
   (Thread/sleep 1000)))

(deftest test-create-join
  (leak/checking
    (testing "A thread can be createed and stopped"
      (let [my-promise (promise)
            my-thread  (long-thread/create "A test thread" #(deliver my-promise :ok))]
        (long-thread/join my-thread)
        (is (= :ok (deref my-promise 10000 :timeout)))))))

(deftest test-create-options
  (leak/checking
    (testing ":start?"
      (let [my-thread (long-thread/create "Non-running thread" do-nothing
                                          {:start? false})]
        (try
          (is (not (long-thread/alive? my-thread)))
          (long-thread/start my-thread)
          (is (long-thread/alive? my-thread))
          (finally
            (long-thread/stop my-thread)))))))

(def ^:dynamic *dynamic-var* nil)

(deftest test-dynamic-vars-are-conveyed
  (leak/checking
    (binding [*dynamic-var* (Object.)]
      (let [promise1 (promise)
            promise2 (promise)
            thread1 (long-thread/create "Conveyed thread" #(deliver promise1 *dynamic-var*))
            thread2 (long-thread/create "Non-conveyed thread" #(deliver promise2 *dynamic-var*)
                                        {:convey-bindings? false})]
        (try
          (is (= *dynamic-var* (deref promise1 10000 :timeout)))
          (is (= nil (deref promise2 10000 :timeout)))
          (finally
            (long-thread/join thread1)
            (long-thread/join thread2)))))))

(deftest test-until-interrupted
  (leak/checking
    (testing "A thread keeps running until it's interrupted"
      (let [my-promise (promise)
            runnable   #(do (long-thread/until-interrupted (Thread/sleep 1000))
                            (deliver my-promise :ok))
            my-thread  (long-thread/create "Another thread" runnable)]
        (is (long-thread/alive? my-thread))
        (long-thread/stop my-thread)
        (is (not (long-thread/alive? my-thread)))
        (is (= :ok (deref my-promise 10000 :timeout)))))))

(deftest test-threads-by-name
  (leak/checking
    (let [threads (set (for [_ (range 3)]
                         (long-thread/create "threads-by-name test" do-nothing)))]
      (try
        (is (= threads (set (long-thread/threads-by-name "threads-by-name test"))))
        (finally
          (run! long-thread/stop threads))))))
