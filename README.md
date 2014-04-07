# Crouton

Crouton is a HTML parsing library for Clojure that uses [JSoup][1].

[1]: http://jsoup.org/

## Installation

Add the following dependency to your `project.clj` file:

    [crouton "0.1.2"]

## Usage

Crouton provides a way of parsing a HTML file from a slurp-able
source:

```clojure
(require '[crouton.html :as html])

(html/parse "http://example.com")
```

The parsed result is a DOM tree compatible with that produced by
`clojure.xml/parse`.

If you have a string, rather than a file, URL or input-stream, you can
use `parse-string`:

```clojure
(html/parse-string "<html><head></head><body></body></html>")
```

## License

Copyright © 2014 James Reeves

Distributed under the Eclipse Public License, the same as Clojure.
