(ns long-thread.core)

(defn start
  "Start a thread.

  `options` is a map with the following keys:

  :daemon?   -- If truthy, create a daemon thread."
  ([^String thread-name ^Runnable runnable] (start thread-name runnable {}))
  ([^String thread-name ^Runnable runnable options]
   (doto (Thread. runnable thread-name)
     (.setDaemon (boolean (:daemon? options)))
     (.start))))

(defn stop
  "Stop a thread and wait until it's interrupted."
  [thread]
  (doto thread
    (.interrupt)
    (.join)))

(defn join
  "Wait until a thread has stopped."
  [thread]
  (.join thread))

(defn alive?
  "Returns true if the thread is alive, i.e. started and not yet stopped."
  [thread]
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
