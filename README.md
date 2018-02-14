# long-thread

A Clojure library for starting and stopping threads. Basically just a wrapper
`java.lang.Thread`.

## Usage

```clojure
(ns example.core
  (:require [long-thread.core :as long-thread]))

(defn my-loop
  []
  (long-thread/until-interrupted
    (println "This is your regularly scheduled greeting: Hello!")
    (Thread/sleep 1000)))

(defn main
  []
  (let [my-thread (long-thread/start "greetings thread" my-loop)]
    (println "Press enter to stop...")
    (read-line)
    (long-thread/stop my-thread)))
```

## License

Copyright © 2018 Miikka Koskinen.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
