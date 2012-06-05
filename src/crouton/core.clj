(ns crouton.core
  (:import [org.jsoup Jsoup]
           [org.jsoup.nodes Document Element TextNode]))

(defprotocol AsClojure
  (^:private as-clojure [x] "Turn a Java class into its Clojure equivalent"))

(extend-protocol AsClojure
  Document
  (as-clojure [doc]
    (-> doc .children first as-clojure))
  Element
  (as-clojure [element]
    {:tag (-> element .tagName keyword)
     :attrs {}
     :content (->> element .childNodes (map as-clojure))})
  TextNode
  (as-clojure [text-node]
    (.text text-node))
  Object
  (as-clojure [x] x))

(defn parse [source]
  (-> source
      Jsoup/parse
      as-clojure))
