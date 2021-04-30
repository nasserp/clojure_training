(ns clojure-in-action.sequences)

;**One Thing After Another**

;; Is this how count is implemented?
(defn flavor [x]
  (cond
    (list? x) :list
    (vector? x) :vector
    (set? x) :set
    (map? x) :map
    (string? x) :string
    :else :unknown))
(defmulti my-count flavor)
(defmethod my-count :list [x] (list-specific-count x))
(defmethod my-count :vector [x] (vector-specific-count x))
;; And so on...

;; Sequence Adapter
;; If you have an object-oriented programming background, then the
;; wrapper design of sequences might look familiar: hidden behind
;; those sequences are the adapter pattern.

(def title-seq (seq ["Emma" "Oliver Twist" "Robinson Crusoe"])) ;("Emma" "Oliver Twist" "Robinson Crusoe")
(seq '("Emma" "Oliver Twist" "Robinson Crusoe")) ;;("Emma" "Oliver Twist" "Robinson Crusoe")
(seq {:title "Emma", :author "Austen", :published 1815}) ;([:title "Emma"] [:author "Austen"] [:published 1815])
(seq (seq ["Red Queen" "The Nightingale" "Uprooted"])) ;("Red Queen" "The Nightingale" "Uprooted")

(seq []) ;nil
(seq '()) ;nil
(seq {}) ;;nil

;**A Universal Interface**
(first (seq '("Emma" "Oliver Twist" "Robinson Crusoe"))) ;"Emma"
(rest (seq '("Emma" "Oliver Twist" "Robinson Crusoe"))) ;("Oliver Twist" "Robinson Crusoe")
(next (seq '("Emma" "Oliver Twist" "Robinson Crusoe"))) ;("Oliver Twist" "Robinson Crusoe")
(rest ()) ;()
(next ()) ;nil
(cons "Emma" (seq '("Oliver Twist" "Robinson Crusoe"))) ;("Emma" "Oliver Twist" "Robinson Crusoe")

(defn my-count [col]
  (let [the-seq (seq col)]
    (loop [n 0 s the-seq]
      (if (seq s)
        (recur (inc n) (rest s))
        n))))
;; Watch Those nils!
;; If you’re thinking that my-count would be clearer if we simply
;; checked whether (first s) was nil , consider trying to count the items
;; in [nil nil nil] .

(cons 0 [1 2 3]) ;(0 1 2 3)
(cons 0 #{1 2 3}) ;(0 1 3 2)

;**A Rich Toolkit ...**

(def titles ["Jaws" "Emma" "2001" "Dracula"])
(sort titles) ; Sequence: ("2001" "Dracula" "Emma" "Jaws")

;; Seqa What?
;; Yes, seqable is a word, at least if you are a Clojurist. A seqable is
;; something that the seq function can turn into a sequence.

(reverse titles) ;("Dracula" "2001" "Emma" "Jaws")

(def titles-and-authors ["Jaws" "Benchley" "2001" "Clarke"])
(partition 2 titles-and-authors) ;(("Jaws" "Benchley") ("2001" "Clarke"))
(count (partition 2 titles-and-authors)) ;2

(def titles ["Jaws" "2001"])
(def authors '("Benchley" "Clarke"))
(interleave titles authors) ;("Jaws" "Benchley" "2001" "Clarke")

(def scary-animals ["Lions" "Tigers" "Bears"])
(interpose "and" scary-animals) ;("Lions" "and" "Tigers" "and" "Bears")

;**... Made Richer with Functional Values**

(filter neg? '(1 -22 3 -99 4 5 6 -77)) ;(-22 -99 -77)

(def books
  [{:title "Deep Six" :price 13.99 :genre :sci-fi :rating 6}
   {:title "Dracula" :price 1.99 :genre :horror :rating 7}
   {:title "Emma" :price 7.99 :genre :comedy :rating 9}
   {:title "2001" :price 10.50 :genre :sci-fi :rating 5}])
   
(defn cheap? [book]
  (when (<= (:price book) 9.99)
    book))

(filter cheap? books) ;({:title "Dracula", :price 1.99, :genre :horror, :rating 7} {:title "Emma", :price 7.99, :genre :comedy, :rating 9})
;'some' is similar to 'filter' but 'some' quits when it finds the first passing item, returning the value from the predicate.
(some cheap? books) ;{:title "Dracula", :price 1.99, :genre :horror, :rating 7}
(filter cheap? ()) ;()
(some cheap? ()) ;nil

;**Map**
(def some-numbers [1, 53, 811])
(def doubled (map #(* 2 %) some-numbers))
(prn doubled) ;(2 106 1622)

;;(map function sequence)
(map (fn [book] (:title book)) books) ;; it is equal (map :title books)
(map :title books) ;keywords are functions

(map (fn [book] (count (:title book))) books)
(map (comp count :title) books) ;;f(g(x))
;Behind is traditional languages
(for [b books]   ;It sets b to each value in the books collection
  (count (:title b)))

;**Reduce**
(def numbers [10 20 30 40 50])
(defn add2 [a b]
  (+ a b))
(reduce add2 0 numbers) ;150 ; 0 is initial value
(reduce + 0 numbers)
(reduce + numbers) ; reduce will use the first element of the collection as the initial value

(defn hi-price [hi book]
  (if (> (:price book) hi)
    (:price book)
    hi))
(reduce hi-price 0 books)

;**Composing a Solution**
(sort-by :rating books) ;the books from lowest to highest rating
(reverse (sort-by :rating books))
(take 3 (reverse (sort-by :rating books))) ;get the first three items
(map :title (take 3 (reverse (sort-by :rating books))))) ;("Emma" "1984" "Jaws")
(interpose
 " // "
 (map :title (take 3 (reverse (sort-by :rating books))))) ;("Emma" " // " "1984" " // " "Jaws")

(defn format-top-titles [books]
  (apply
   str
   (interpose
    " // "
    (map :title (take 3 (reverse (sort-by :rating books)))))))


 ;**Other Sources of Sequences**

(require '[clojure.java.io :as io])
(defn listed-author? [author]
  (with-open [r (io/reader "authors.txt")]
    (some (partial = author) (line-seq r))))

;; A regular expression that matches Pride and Prejudice followed by anything.
(def re #"Pride and Prejudice.*")
;; A string that may or may not match.
(def title "Pride and Prejudice and Zombies")
;; And we have a classic!
(if (re-matches re title)
  (println "We have a classic!")) ;We have a classic!

(re-seq #"\w+" title) ;;("Pride" "and" "Prejudice" "and" "Zombies")


;**Staying Out of Trouble**
(defn total-sales [books]
  "Total up book sales. Books maps must have :sales key."
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur (next books)
             (+ total (:sales (first books)))))))
;easier way
(defn total-sales [books] (apply + (map :sales books)))


(def maze-runner {:title "The Maze Runner" :author "Dashner"})
(:author maze-runner) ;"Dashner"
(:author (seq maze-runner)) ; But this give you a nil - a seq is not a map!
(:author (rest maze-runner)) ; Also nil: rest returns a seq.


(conj ["Emma" "1984" "The Maze Runner"] "Jaws") ; A *vector* ending with "Jaws".
(conj '("Emma" "1984" "The Maze Runner") "Jaws") ; A *list* starting with "Jaws".

(cons "Jaws" ["Emma" "1984" "The Maze Runner"]) ;A *seq* starting with "Jaws" .
(cons "Jaws" '("Emma" "1984" "The Maze Runner")) ;; A *seq* starting with "Jaws".

