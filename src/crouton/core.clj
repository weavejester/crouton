(ns crouton.core
  (:import [org.jsoup Jsoup]
           [org.jsoup.nodes Document]))

(defprotocol AsClojure
  (^:private as-clojure [x] "Turn a Java class into its Clojure equivalent"))

(extend-protocol AsClojure
  Document
  (as-clojure [doc]
    (-> doc .children first)))

(defn parse [source]
  (-> source
      Jsoup/parse
      as-clojure))
