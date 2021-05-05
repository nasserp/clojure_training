(ns clojure-in-action.inventory-test
  (:require [clojure.test :refer :all]
            [clojure-in-action.inventory :as i]))

(def books
  [{:title "2001" :author "Clarke" :copies 21}
   {:title "Emma" :author "Austen" :copies 10}
   {:title "Misery" :author "King" :copies 101}])

(deftest test-finding-books
  (is (not (nil? (i/find-by-title "Emma" books)))))

;You’re not limited to one expression per test
(deftest test-finding-books-better
  (is (not (nil? (i/find-by-title "Emma" books))))
  (is (nil? (i/find-by-title "XYZZY" books))))

;The combination of deftest and testing means that you can organize your tests
(deftest test-basic-inventory
  (testing "Finding books"
    (is (not (nil? (i/find-by-title "Emma" books))))
    (is (nil? (i/find-by-title "XYZZY" books))))
  (testing "Copies in inventory"
    (is (= 10 (i/number-of-copies-of "Emma" books)))))
