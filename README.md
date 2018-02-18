# long-thread [![codecov](https://codecov.io/gh/miikka/long-thread/branch/master/graph/badge.svg)](https://codecov.io/gh/miikka/long-thread)

A Clojure library for starting and stopping threads. Basically just a wrapper for
`java.lang.Thread`.

## Latest version

[![Clojars Project](http://clojars.org/miikka/long-thread/latest-version.svg)](http://clojars.org/miikka/long-thread)

## Why?

Clojure has plenty fancy tools such as futures for concurrent programming.
However, sometimes what you need is a good old thread. You could use Java's
`Thread` directly, but the purpose of this wrapper is to encourage good
practices such as always naming your threads.

## Usage

* [API reference](https://miikka.github.io/long-thread/master/index.html)

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
  (let [my-thread (long-thread/create "greetings thread" my-loop)]
    (println "Press enter to stop...")
    (read-line)
    (long-thread/stop my-thread)))
```

## Acknowledgements

The inspiration for this work is `zthread.clj`, a similar library developed by
Joel Kaasinen and others at ZenRobotics.

## Development guide

* Run tests with `lein test`
* Generate a test coverage report with `lein cloverage` and look at `target/coverage/index.html`.

## License

Copyright © 2018 Miikka Koskinen.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
