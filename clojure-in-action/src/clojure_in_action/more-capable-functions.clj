(ns clojure-in-action.more-capable-functions)



;; ***multi-arity function***
(defn greet
  ([to-whom] (println "Welcome to Blotts Books" to-whom)) ;;Welcome to Blotts Books param1
  ([message to-whom] (println message to-whom))) ;; param1 param2

(defn greet
  ([to-whom] (greet "Welcome to Blotts Books" to-whom)) ;;Welcome to Blotts Books param1
  ([message to-whom] (println message to-whom))) ; param1 param2



;; ***varargs(variadic) functions***

(defn print-any-args [& args]
  (println "My arguments are:" args))
(print-any-args 7 true nil) ;;My arguments are: (7 true nil) Notic:(7 true nill) is a collection

(defn first-argument [& args]
  (first args))
(first-argument 5 8 7) ;;5

;;you can have ordinary arguments before the &
(defn new-first-argument [x & args] x)
(new-first-argument 5 8 7) ;;5



;; ***Multimethods***   -->

;;[nasser]assume
(def book1 {:title "War and Peace" :author "Tolstoy"})
(def book2 {:book "Emma" :by "Austen"})
(def book3 ["1984" "Orwell"])

;; Normalize book data to {:title ? :author ?}
(defn normalize-book [book]
  (if (vector? book)
    {:title (first book) :author (second book)} ;;Notic: second function
    (if (contains? book :title)
      book
      {:title (:book book) :author (:by book)})))

(normalize-book book1) ;;{:title "War and Peace", :author "Tolstoy"}
(normalize-book book2) ;;{:title "Emma", :author "Austen"}
(normalize-book book3) ;;{:title "1984", :author "Orwell"}

;;[nasser]what if we suddenly had to deal with a whole blizzard of book formats?!

(defn dispatch-book-format [book]
  (cond
    (vector? book) :vector-book
    (contains? book :title) :standard-map
    (contains? book :book) :alternative-map))
;; example the result of (dispatch-book-format {:book "Emma" :by "Austen"}) is :alternative-ma
(defmulti normalize-book dispatch-book-format)
(defmethod normalize-book :vector-book [book]
  {:title (first book) :author (second book)})
(defmethod normalize-book :standard-map [book]
  book)
(defmethod normalize-book :alternative-map [book]
  {:title (:book book) :author (:by book)})

(normalize-book {:title "War and Peace" :author "Tolstoy"}) ;;{:title "War and Peace", :author "Tolstoy"}
(normalize-book {:book "Emma" :by "Austen"}) ;;{:title "Emma" :author "Austen"}
(normalize-book ["1984" "Orwell"]) ;;{:title "1984" :author "Orwell"}

(normalize-book '("1984" "Orwell")) ;;Result: Execution error (IllegalArgumentException)...contains? not supported on type

;; Multi Who?
;; The careful reader will have noticed that normalize-book doesn’t
;; contain any code to handle bad input. The good news is that if
;; the dispatch function produces a value for which there is no cor-
;; responding defmethod , Clojure will generate an exception, which is
;; probably what you want. Alternatively, you can supply a method
;; for the value :default that will cover the everything else case.

(defn dispatch-published [book]
  (cond
    (< (:published book) 1928) :public-domain
    (< (:published book) 1978) :old-copyright
    :else :new-copyright))
(defmulti compute-royalties dispatch-published)
(defmethod compute-royalties :public-domain [book] 0)
(defmethod compute-royalties :old-copyright [book] 1)
(defmethod compute-royalties :new-copyright [book] 2)


;;There’s no requirement that all the bits of a single multimethod
;;be defined in the same file or at the same time.
(def books [{:title "Pride and Prejudice" :author "Austen" :genre :romance}
            {:title "World War Z" :author "Brooks" :genre :zombie}])
(defmulti book-description :genre)
(defmethod book-description :romance [book]
  (str "The heart warming new romance by " (:author book)))
(defmethod book-description :zombie [book]
  (str "The heart consuming new zombie adventure by " (:author book)))
(def ppz {:title "Pride and Prejudice and Zombies"
          :author "Grahame-Smith"
          :genre :zombie-romance})
(defmethod book-description :zombie-romance [book]
  (str "The heart warming and consuming new romance by " (:author book)))
(book-description ppz)


;;***Deeply Recursive***

(def books
[{:title "Jaws" :copies-sold 2000000}
{:title "Emma" :copies-sold 3000000}
{:title "2001" :copies-sold 4000000}])
;;to know the total number of books sold
(defn sum-copies
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (sum-copies
      (rest books)
      (+ total (:copies-sold (first books)))))))
(sum-copies books)
;;With a long books vector you will run out of stack space: StackOverflowError
;;optimization by recur: 
(defn sum-copies
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (recur
      (rest books)
      (+ total (:copies-sold (first books)))))))
;;use Loop
(defn sum-copies [books]
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur
       (rest books)
       (+ total (:copies-sold (first books)))))))
;;and easier
(defn sum-copies [books] (apply + (map :copies-sold books)))
(sum-copies books)


;;***Docstrings***

;; A documentation string—or docstring for short—is a regular string
;; that you can insert just after the function name in your defn
;;Clojure will store the string along with the function.

(defn average
  "Return the average of a and b."
  [a b]
  (/ (+ a b) 2.0))
;;The result of user=> (doc average) is :
;; user/average
;;  ([a b])
;;  Return the average of a and b.


;; Docstrings for the House!
;; Docstrings are not just for functions. Other members of the Clojure
;; menagerie, creatures like macros and records—which we’ll meet
;; in later chapters—also support docstrings.
;; So you can also supply a docstring in a plain old def : (def ISBN-LENGTH
;; "Length of an ISBN code." 13) .

(defn multi-average
  "Return the average of 2 or 3 numbers."
  ([a b]
   (/ (+ a b) 2.0))
  ([a b c]
   (/ (+ a b c) 3.0)))
;;The result of user=> (doc average) is :
;; user/multi-average
;;  ([a b] [a b c])
;;  Return the average of 2 or 3 numbers.



;;***Pre and Post Conditions***
(defn publish-book [book]
  (when-not (contains? book :title) ;;or :(when-not (:title book)
    (throw (ex-info "Books must contain :title" {:book book})))
  (prn 1)
  (prn 2))
(publish-book {:title "title"})
(publish-book {:title1 "title1"})

;; :pre condition just add a map after the arguments
(defn publish-book [book]
{:pre [(:title book)]} 
  (prn 1)
  (prn 2))
(publish-book {:title "title"})
(publish-book {:title1 "title1"}) ;Execution error (AssertionError)...Assert failed: (:title book)

;; :post condition, which lets you check on the value returned from the functio
(defn publish-book [book]
  {:pre [(:title book) (:author book)]
   :post [(boolean? %)]}
   (prn 1)
   (prn 2)
    true
  )
(publish-book {:title "title" :author "author"})

(defn publish-book [book]
  {:pre [(:title book) (:author book)]
   :post [(boolean? %)]}
  (prn 1)
  (prn 2))
(publish-book {:title "title" :author "author"}) ;;Execution error (AssertionError)... Assert failed: (boolean? %)


;;***Staying Out of Trouble***
;;mix variadic & multi-arity function
(defn one-two-or-more
  ([a] (println "One arg:" a))
  ([a b] (println "Two args:" a b))
  ([a b & more] (println "More than two:" a b more)))
(one-two-or-more 2 4 3 6) ;;NOTIC: result is: More than two: 2 4 (3 6)

(defn one-two-or-more
  ([a] (println "One arg:" a))
  ([a b] (println "Two args:" a b))
  ([& more] (println "More than two:" more)))
(one-two-or-more 2 4 3 6)  ;;Syntax error compiling fn* 

(defn print-any-args [& args]
  (println "My arguments are:" args))
(print-any-args 2 3)

;; (defn print-any-args [&args]
;;   (println "My arguments are:" args))
;; (print-any-args 2) ---->Result: Syntax error compiling...Unable to resolve symbol: args in this context







