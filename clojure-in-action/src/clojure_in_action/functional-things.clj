(ns clojure-in-action.functional-things)

;***Functions are first-class values***

(def dracula {:title "Dracula"

              :author "Stoker"
              :price 1.99
              :genre :horror})

(defn cheap? [book]
  (when (<= (:price book) 9.99)
    book))
(defn pricey? [book]
  (when (> (:price book) 9.99)
    book))
(cheap? dracula) ; Yes!
(pricey? dracula) ; No!

(defn horror? [book]
  (when (= (:genre book) :horror)
    book))
(defn adventure? [book]
  (when (= (:genre book) :adventure)
    book))
(horror? dracula) ; Yes!
(adventure? dracula) ; Nope!

(defn cheap-horror? [book]
  (when (and (cheap? book)
             (horror? book))
    book))
(defn pricy-adventure? [book]
  (when (and (pricey? book)
             (adventure? book))
    book))

;; Possession
;; It turns out there’s a remarkable number of novels called Possession, with at least a dozen in print as I write this.

;In Clojure functions Are Values such as numbers and strings
;-> You can bind that function value to another symbol
(def reasonably-priced? cheap?)
(reasonably-priced? dracula) ; Yes!
;->You can pass function values to other functions. To take a silly example we could do this
(defn run-with-dracula [f]
  (f dracula))
(run-with-dracula pricey?) ; Nope.
(run-with-dracula horror?) ; Yes!
(defn both? [first-predicate-f second-predicate-f book]
  (when (and (first-predicate-f book)
             (second-predicate-f book))
    book))
(both? cheap? horror? dracula) ; Yup!
(both? pricey? adventure? dracula) ; Nope!

;***Functions on the Fly***
;->Annonymous function as value
(println "A function:" (fn [n] (* 2 n)))
(fn [n] (* 2 n))
(def double-it (fn [n] (* 2 n))) ;;bind it to a symbol

;Returning to above example
(defn cheaper-f [max-price]
  (fn [book]
    (when (<= (:price book) max-price)
      book)))
;-> Define some helpful functions.
(def real-cheap? (cheaper-f 1.00))
(def kind-of-cheap? (cheaper-f 1.99))
(def marginally-cheap? (cheaper-f 5.99))
;-> And use them.
(real-cheap? dracula) ; Nope.
(kind-of-cheap? dracula) ; Yes.
(marginally-cheap? dracula) ; Indeed.
(defn both-f [predicate-f-1 predicate-f-2]
  (fn [book]
    (when (and (predicate-f-1 book) (predicate-f-2 book))
      book)))
(def cheap-horror? (both-f cheap? horror?))
(def real-cheap-adventure? (both-f real-cheap? adventure?))
(def real-cheap-horror? (both-f real-cheap? horror?))
(def cheap-horror-possession?
  (both-f cheap-horror?
          (fn [book] (= (:title book) "Possession"))))
;-> This idea of a function grabbing and remembering the bindings that existed
;; when the function was born is called a 'closure'. We say that the function
;; closes over the scope in which it was defined.

;***A Functional Toolkit***

;->apply
(apply + [1 2 3]) ; It is equal with (+ 1 2 3)
(apply * '(1 2 3)) ; It is equal with (+ 1 2 3)
(apply vector '(1 2 3)) ;[1 2 3]
(apply list [1 2 3]) ; '(1 2 3)

;->partial
(def my-inc (partial + 1)) ;(defn my-inc [n] (+ 1 n))
;Notic: in partial we have 'def'
(defn cheaper-than [max-price book]
  (when (<= (:price book) max-price)
    book))
(def real-cheap? (partial cheaper-than 1.00))

;->complement
(defn adventure? [book]
  (when (= (:genre book) :adventure)
    book))
;----looked for nonadventure books:
(defn not-adventure? [book] (not (adventure? book)))
;----with 'complement' :
(def not-adventure? (complement adventure?)) ;Notic:in complement we have 'def'

;->every-pred
;It combines predicate functions into a single function that ands them all together.
(def cheap-horror? (every-pred cheap? horror?))
(def cheap-horror-possession?
  (every-pred
   cheap?
   horror?
   (fn [book] (= (:title book) "Possession"))))


;**Function Literals**

#(* 2 %1) ;(fn [n] (* 2 n))
#(* 2 %) ;#(* 2 %1)
#(+ 2 %1 %2) ;(fn [n m] (+ 2 n m))
#(when (= (:genre %1) :adventure) %1) ;(fn [n] (when (= (:genre n) :adventure)))


