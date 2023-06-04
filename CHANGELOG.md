<!--
SPDX-FileCopyrightText: 2018 Miikka Koskinen
SPDX-License-Identifier: EPL-2.0
-->

# 0.3.1 (2023-06-04)

* Use Markdown tables in docstrings

# 0.3.0 (2018-02-15)

* *BREAKING*: Convey dynamic bindings to the new threads from the starting thread.
* *BREAKING*: `long-thread.core/start` has been renamed to `long-thread.core/create`.
* Added `long-thread.core/start` for starting non-started threads.
* Added `:start?` and `:convey-bindings?` options to `long-thread.core/create`.
* Reflection warnings are gone.

# 0.2.0 (2018-02-14)

* Add `long-thread.core/running-threads` and `long-thread.core/threads-by-name` for inspection.
* Add `long-thread.leak/checking` for guarding against thread leaks.

# 0.1.0 (2018-02-14)

* Initial release.
