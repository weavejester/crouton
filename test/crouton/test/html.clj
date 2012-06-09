(ns crouton.test.html
  (:use clojure.test
        crouton.html))

(defn- stream [s]
  (java.io.ByteArrayInputStream. (.getBytes s)))

(deftest test-parse
  (testing "Basic parsing"
    (is (= (parse (stream "<html><head></head><body></body></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil :content nil}]})))
  (testing "Attributes"
    (is (= (parse (stream "<html><body><div class=\"foo\"></div></body></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil
                       :content [{:tag :div
                                  :attrs {:class "foo"}
                                  :content nil}]}]}))))
