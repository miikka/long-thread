<!--
SPDX-FileCopyrightText: 2018 Miikka Koskinen
SPDX-License-Identifier: EPL-2.0
-->

# long-thread [![cljdoc badge](https://cljdoc.org/badge/miikka/long-thread)](https://cljdoc.org/d/miikka/long-thread/CURRENT)

A Clojure library for starting and stopping threads. Basically just a wrapper for
`java.lang.Thread`.

## Latest version

[![Clojars Project](https://clojars.org/miikka/long-thread/latest-version.svg)](https://clojars.org/miikka/long-thread)

## Why?

Clojure has plenty fancy tools such as futures for concurrent programming.
However, sometimes what you need is a good old thread. You could use Java's
`Thread` directly, but the purpose of this wrapper is to encourage good
practices such as always naming your threads.

## Usage

* [API reference](https://cljdoc.org/d/miikka/long-thread/CURRENT)

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

## See also

* For value-returning one-off tasks, see Clojure's futures, [manifold's futures](http://aleph.io/manifold/deferreds.html#future-vs-manifold-deferred-future), or [java.util.concurrent.ExecutorService](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html).
* For scheduled tasks (e.g. run once in every minute), see [manifold.time](https://aleph.io/codox/manifold/manifold.time.html), Duct's [scheduler.simple](https://github.com/duct-framework/scheduler.simple), and [ScheduledThreadPoolExecutor](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledThreadPoolExecutor.html).

## Development guide

* Run tests with `lein test`
* Generate a test coverage report with `lein cloverage` and look at `target/coverage/index.html`.

## License

Copyright Miikka Koskinen.

Available under the terms of the Eclipse Public License 2.0, see `LICENSES/EPL-2.0.txt`.
