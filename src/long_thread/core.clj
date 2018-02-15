(ns long-thread.core)

;; There's clojure.core/binding-conveyor-fn, but it's marked as private, so
;; let's not use it. Let's copy it here instead!
(defn- ^Runnable wrap-runnable [^Runnable runnable]
  (let [frame (clojure.lang.Var/cloneThreadBindingFrame)]
    (fn []
      (clojure.lang.Var/resetThreadBindingFrame frame)
      (.run runnable))))

(defn create
  "Create a thread.

  The thread is started immediately by default.

  `options` is a map with the following keys:

  :daemon?          -- If truthy, create a daemon thread. Default: false
  :start?           -- If truthy, thread is started immediately. Default: true.
  :convey-bindings? -- If truthy, the bindings for dynamic variables are
                       conveyed from the starting thread to the new thread.
                       Default: true."
  ([^String thread-name ^Runnable runnable] (create thread-name runnable {}))
  ([^String thread-name ^Runnable runnable options]
   (let [runnable (cond-> runnable
                    (get options :convey-bindings? true) wrap-runnable)]
     (doto (Thread. runnable thread-name)
       (.setDaemon (boolean (get options :daemon? false)))
       (cond-> (get options :start? true) (.start))))))

(defn start
  "Start a thread."
  [^Thread thread]
  (.start thread))

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
