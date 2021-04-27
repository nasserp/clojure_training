(ns clojure-in-action.vectors-and-lists)

;***Vector**
[1 2 3 4]
[1 "two" 3 "four"]
[1 [true 3 "four" 5] 6]
[0 [1 [true 3 "four" 5] 6] 7]

(vector true 3 "four" 5)
(vector) ;[]

(def novels ["Emma" "Coma" "War and Peace"])
(count novels); Returns 3.

(first novels) ;"Emma"
(rest novels) ;;("Coma" "War and Peace")  NOTIC: It is a collection
(rest ["Ready Player One"]); () Returns an empty collection.
(rest []);() Also an empty collection.

(def year-books ["1491" "April 1865", "1984", "2001"])
(def third-book (first (rest (rest year-books)))); "1984".
(nth year-books 2); Returns "1984".
(year-books 2); Also returns "1984".

;; Immutable Exceptions?
;; There are some exceptions to the Clojure everything is immutable
;; rule. For example, consider that something must change when we
;; say (def n 99) . To find out what, see Def, Symbols, and Vars.

(conj novels "Carrie"); Add end of vector.["Emma" "Coma" "War and Peace" "Carrie"]
(cons "Carrie" novels) ;result is a collection,add begion of the collection,("Carrie" "Emma" "Coma" "War and Peace")


;***List***
'(1 2 3)
'([1 2 ("a" "list" "inside a" "vector")] "inside" "a" "list")
(list 1 2 3 "four" 5 "six")

(def poems '("Iliad" "Odyssey" "Now We Are Six"))
(count poems) ;Returns 3.
(first poems) ;"Iliad" .
(rest poems) ;("Odyssey" "Now We Are Six")
(nth poems 2) ;"Now We Are Six" .


;; How Many Slots?
;; Those two-slot list objects actually have three slots. The third slot
;; is a count of the number of items in the list. This enables the count
;; function do its thing without having to run down the whole list
;; saying, One item, two items, three items .... We can get away with
;; caching the count because lists are immutable.


(def poems '("Iliad" "Odyssey" "Now We Are Six"))
(conj poems "Jabberwocky") ; add BEGION of list ("Jabberwocky" "Iliad" "Odyssey" "Now We Are Six")

(def more-novels (conj novels "Jaws"))


