(ns crouton.core
  (:import [org.jsoup Jsoup]
           [org.jsoup.nodes Attribute Attributes Document Element TextNode]))

(defprotocol AsClojure
  (^:private as-clojure [x] "Turn a Java class into its Clojure equivalent"))

(extend-protocol AsClojure
  Document
  (as-clojure [doc]
    (-> doc .children first as-clojure))
  Element
  (as-clojure [element]
    {:tag     (-> element .tagName keyword)
     :attrs   (-> element .attributes as-clojure not-empty)
     :content (->> element .childNodes (map as-clojure) vec not-empty)})
  Attributes
  (as-clojure [attrs]
    (into {} (map as-clojure attrs)))
  Attribute
  (as-clojure [attr]
    [(keyword (.getKey attr))
     (.getValue attr)])
  TextNode
  (as-clojure [text-node]
    (.text text-node))
  Object
  (as-clojure [x] x))

(defn parse [source]
  (-> source
      slurp
      Jsoup/parse
      as-clojure))
