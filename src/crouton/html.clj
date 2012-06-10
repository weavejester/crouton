(ns crouton.html
  (:import [org.jsoup Jsoup]
           [org.jsoup.nodes
            Attribute Attributes Comment DataNode
            Document Element TextNode]))

(defn- join-adjacent-strings [coll]
  (reverse
   (reduce
    (fn [[x & xs :as xxs] s]
      (if (and xxs (string? s) (string? x))
        (cons (str x s) xs)
        (cons s xxs)))
    nil
    coll)))

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
     :content (->> element
                   .childNodes
                   (map as-clojure)
                   (remove nil?)
                   join-adjacent-strings
                   vec
                   not-empty)})
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
  DataNode
  (as-clojure [data-node]
    (.getWholeData data-node))
  Comment
  (as-clojure [comment] nil))

(defn parse
  "Reads and parses the HTML from the supplied source, which map be anything
  that can be consumed by clojure.core/slurp. Returns a tree compatible with
  that returned by clojure.xml/parse, i.e. a map that has the keys :tag, :attrs
  and :content."
  [source]
  (-> source
      slurp
      Jsoup/parse
      as-clojure))
