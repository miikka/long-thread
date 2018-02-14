(ns long-thread.core)

;; There's clojure.core/binding-conveyor-fn, but it's marked as private, so
;; let's not use it. Let's copy it here instead!
(defn- ^Runnable wrap-runnable [^Runnable runnable]
  (let [frame (clojure.lang.Var/cloneThreadBindingFrame)]
    (fn []
      (clojure.lang.Var/resetThreadBindingFrame frame)
      (.run runnable))))

(defn start
  "Start a thread.

  `options` is a map with the following keys:

  :daemon?   -- If truthy, create a daemon thread."
  ([^String thread-name ^Runnable runnable] (start thread-name runnable {}))
  ([^String thread-name ^Runnable runnable options]
   (doto (Thread. (wrap-runnable runnable) thread-name)
     (.setDaemon (boolean (:daemon? options)))
     (.start))))

(defn stop
  "Stop a thread and wait until it's interrupted."
  [^Thread thread]
  (doto thread
    (.interrupt)
    (.join)))

(defn join
  "Wait until a thread has stopped."
  [^Thread thread]
  (.join thread))

(defn alive?
  "Returns true if the thread is alive, i.e. started and not yet stopped."
  [^Thread thread]
  (.isAlive thread))

(defmacro until-interrupted
  "Run the body in an implicit do block until the thread is interrupted.

  If you catch InterruptedExceptions in the body, this thread won't be
  stopped. If you want the thread to stop, re-throw the exception."
  [& body]
  `(try
     (while (not (Thread/interrupted))
       ~@body)
     (catch InterruptedException _#)))

(defn running-threads
  "Get a sequence of running Thread objects."
  []
  (keys (Thread/getAllStackTraces)))

(defn threads-by-name
  "Get a sequence of Thread objects with the given name.

  Thread names are not unique, so there might be more than one."
  [thread-name]
  (filter #(= thread-name (.getName ^Thread %)) (running-threads)))
