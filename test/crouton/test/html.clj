(ns crouton.test.html
  (:use clojure.test
        crouton.html))

(defn- string-stream [s]
  (java.io.ByteArrayInputStream. (.getBytes s)))

(deftest test-parse
  (testing "Basic parsing"
    (is (= (parse (string-stream "<html><head></head><body></body>"))
           {:tag :html
            :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil :content nil}]}))))
