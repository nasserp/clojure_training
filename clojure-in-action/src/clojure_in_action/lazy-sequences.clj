(ns clojure-in-action.lazy-sequences)

;**Sequences Without End**
;All unbounded sequences are lazy
(def jack "All work and no play makes Jack a dull boy.")
;; Be careful with repeated-text in the REPL. There's a surprise lurking...
(def repeated-text (repeat jack))
(first repeated-text) ;"All work and no play makes Jack a dull boy."
(nth repeated-text 10) ;"All work and no play makes Jack a dull boy."
(nth repeated-text 10202) ;"All work and no play makes Jack a dull boy."
(take 2 repeated-text) ;("All work and no play makes Jack a dull boy." "All work and no play makes Jack a dull boy.")

;**More Interesting Laziness**
(take 7 (cycle [1 2 3])) ;(1 2 3 1 2 3 1)
(take 7 (repeat [1 2 3])) ;([1 2 3] [1 2 3] [1 2 3] [1 2 3] [1 2 3] [1 2 3] [1 2 3])
;(iterate function startingValue)
(def numbers (iterate inc 1))
(first numbers) ;1 startingValue
(nth numbers 0) ;1
(nth numbers 1) ;2
(nth numbers 99) ;100
(take 5 numbers) ;(1 2 3 4 5)

;**Lazy Friends**
;'take' is itself lazy.It waits to be asked before it does anything.
(println (take 20 (take 1000000000 (iterate inc 1))))
;'map' is lazy.
(def evens (map #(* 2 %) (iterate inc 1)))
(take 20 evens)
;'interleave' is lazy
(take 10 (interleave numbers evens)) ;;; Returns (1 2 2 4 3 6 4 8 5 10)

;**Laziness in Practice**
(def numbers (iterate inc 1))
(def titles (map #(str "Wheel of Time, Book " %) numbers))
(def first-names ["Bob" "Jane" "Chuck" "Leo"])
(def last-names ["Jordan" "Austen" "Dickens" "Tolstoy" "Poe"])
(defn combine-names [fname lname]
  (str fname " " lname))
(def authors
  (map combine-names
       (cycle first-names)
       (cycle last-names)))
(defn make-book [title author]
  {:author author :title title})
(def test-books (map make-book titles authors))
(take 3 test-books) ;({:author "Bob Jordan", :title "Wheel of Time, Book 1"} {:author "Jane Austen", :title "Wheel of Time, Book 2"} {:author "Chuck Dickens", :title "Wheel of Time, Book 3"})

;**Behind the Scenes**
(defn chatty-vector []
  (println "Here we go!")
  [1 2 3])
(first chatty-vector) ;; Execution error...Don't know how to create ISeq from chatty_vector
(def s (lazy-seq (chatty-vector)))
(first s) ;Here we go!  1

;**Staying Out of Trouble**
;Be careful about side effects when dealing with lazy sequences

(slurp "chap1.txt") ;;; Get the contents of the file as a string.

;; Slurp and Spit
;; The slurp function is the Clojure programmer’s universal I need to
;; read something friend. Most notably, slurp will do exactly what you
;; want if you hand it a URL: (slurp "http://russolsen.com/index.html") .
;; If your interest lies more in writing than reading, there is spit , which
;; will take a path and a string to write the string to that file, as in:
;; (spit "/tmp/chapter1.txt" "It was a dark...") .
;; As I say, these functions are your friends. The names ... well, I
;; guess you had to be there.

(def chapters (take 10 (map slurp (map #(str "chap" % ".txt") numbers))))
(doall chapters) ;; Read the chapters NOW! ,down lazy sequence ;If you want it done right now, doall is your friend.
(doseq [c chapters] ;; Read the chapters NOW!
  (println "The chapter text is" c))