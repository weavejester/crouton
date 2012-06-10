(ns crouton.test.html
  (:use clojure.test
        crouton.html))

(defn- stream [s]
  (java.io.ByteArrayInputStream. (.getBytes s)))

(deftest test-parse
  (testing "Elements"
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
                                  :content nil}]}]})))
  (testing "Text"
    (is (= (parse (stream "<html><body>Hello World</body></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil
                       :content ["Hello World"]}]})))
  (testing "Scripts"
    (is (= (parse (stream "<html><head><script>foo();</script></head></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil
                       :content [{:tag :script
                                  :attrs nil
                                  :content ["foo();"]}]}
                      {:tag :body :attrs nil :content nil}]})))
  (testing "Comments"
    (is (= (parse (stream "<html><body>Foo<!--bar-->Baz</html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil :content ["FooBaz"]}]})))
  (testing "Doctypes"
    (is (= (parse (stream "<!DOCTYPE html><html></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil :content nil}]})))
  (testing "XML declarations"
    (is (= (parse (stream "<?xml version=\"1.0\" encoding=\"UTF-8\"><html></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil :content nil}]})))
  (testing "Character entities"
    (is (= (parse (stream "<html><body>x &amp; y</body></html>"))
           {:tag :html :attrs nil
            :content [{:tag :head :attrs nil :content nil}
                      {:tag :body :attrs nil :content ["x & y"]}]}))))
