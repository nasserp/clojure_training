;**Property-Based Testing**


;; [{:title "2001" :author "Clarke" :copies 21}
;;  {:title "Emma" :author "Austen" :copies 10}
;;  {:title "Misery" :author "King" :copies 101}]


(require '[clojure.test.check.generators :as gen])

(def title-gen gen/string-alphanumeric)
(def author-gen gen/string-alphanumeric)
(def copies-gen gen/pos-int)
;build a generator for (nonempty) titles, authors, and (nonzero) number of copies
(def title-gen (gen/such-that not-empty gen/string-alphanumeric))
(def author-gen (gen/such-that not-empty gen/string-alphanumeric))
(def copies-gen (gen/such-that (complement zero?) gen/pos-int))

(def book-gen
  (gen/hash-map :title title-gen :author author-gen :copies copies-gen))

(def inventory-gen (gen/not-empty (gen/vector book-gen)))

(def inventory-and-book-gen
  (gen/let [inventory inventory-gen
            book (gen/elements inventory)]
    {:inventory inventory :book book}))

;; Generator Magic?
;; Don’t fret if the test.check generators seem like magic. It is a bit
;; magical, but it’s all implemented with sophisticated-but-neverthe-
;; less-ordinary Clojure. For example, each generator is actually a
;; record instance with a function buried inside. And as you might
;; imagine from our adventures generating random books back in
;; Lazy Sequences, there are some lazy sequences in play, as well.


;**Checking Properties**

(prop/for-all [i gen/pos-int]
              (< i (inc i)))

(tc/quick-check 50 
                (prop/for-all [i gen/pos-int]
                              (< i (inc i))))
;the quick-check function returns a map describing the results, something like this:
{:result true, :num-tests 50, :seed 1509151628189}

