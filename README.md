# long-thread

A Clojure library for starting and stopping threads. Basically just a wrapper
`java.lang.Thread`.

## Latest version

[![Clojars Project](http://clojars.org/miikka/long-thread/latest-version.svg)](http://clojars.org/miikka/long-thread)

## Why?

Clojure has plenty fancy tools such as futures for concurrent programming.
However, sometimes what you need is a good old thread. You could use Java's
`Thread` directly, but the purpose of this wrapper is to encourage good
practices such as always naming your threads.

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
