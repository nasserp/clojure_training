(ns clojure-in-action.logic)


(defn print-greeting [preferred-customer]
  (if preferred-customer
    (println "Welcome back to Blotts Books!"))
  (println "Welcome to Blotts Books!"))

(defn shipping-charge [bool1 num1]
  (if bool1
    0.00
    (* num1 0.10))
  (if bool1 "So nice to have you back!")) ;;write short if expressions on a single line


;;Equality
;;Note that the = function is built on the idea of structural equality:
;;roughly, two values are equal according to = if they have the same
;;value. Under the hood, = is identical to the Java equals method.

(not= "Anna Karenina" "Jane Eyre")

(if (> 5 3) (println "5 is bigger than 3"))

(number? 1984)
(string? "Anna Karenina")
(keyword? "Anna Karenina")
(keyword? :anna-karenina)
(map? :anna-karenina)
(map? {:title 1984})
(vector? 1984)
(vector? [1984])

;; Charge extra if it's an 'a' or 'b' and they are not 'c'.
(defn shipping-surcharge? [a b c]
  (and (not a) (or b c)))

;;Clojure is willing to treat any value as a Boolean
;;With the exception of 'false'
(if false "A" "B")
(if nil "A" "B")
(if 1 "A" "B")
(if "hello" "A" "B")
(if [1 2 3] "A" "B")
(if [] "A" "B")
(if {:kw "STR"} "A" "B")
(if {} "A" "B")
(if '(:kw1 :kw2) "A" "B")
(if () (println "A" "B")) ;;An empty list
(if :kw "A" "B")


;;Some Clojure programmers believe the proper spelling is falsey.
;;This belief is false.


;;Armed with do , we can flesh out a simple if with multipart true and false legs:
(defn shipping-charge [preferred-customer order-amount]
  (if preferred-customer
    (do
      (println "Preferred customer, free shipping!")
      0.0)
    (do
      (println "Regular customer, charge them for shipping.")
      (* order-amount 0.10))))

;;Clojure also sports a variant of if called when , which doesn’t have an else
(defn shipping-charge [preferred-customer]
  (when preferred-customer
    (println "Hello returning customer!")
    (println "Welcome back to Blotts Books!")))



(defn shipping-charge [preferred-customer order-amount]
  (if preferred-customer
    0.0
    (if (< order-amount 50.0) ;;else if
      5.0
      (if (< order-amount 100.0) ;;else if
        10.0
        (* 0.1 order-amount))))) ;;else
;;------------------------------------------->USE 'cond'
(defn shipping-charge [preferred-customer order-amount]
  (cond
    preferred-customer 0.0
    (< order-amount 50.0) 5.0
    (< order-amount 100.0) 10.0
    :else (* 0.1 order-amount)))


;;Switch Case
(defn customer-greeting [status]
  (case status
    :gold
    "Welcome, welcome, welcome back!!!"
    :preferred "Welcome back!"
    "Welcome to Blotts Books"))


;;TRY-CATCH
(try
  (/ 0 0)
  (catch ArithmeticException e (println "Math problem."))
  (catch StackOverflowError e (println "Unable to publish..")))

;;Throw an exception manually
;;the easiest way to get hold of an exception value is to use the built-in ex-info function
;;The ex-info function takes a string describing the problem and a (possibly empty) map containing any other pertinent information
(defn publish-book [book]
  (when (not (:title book))
    (throw
     (ex-info "A book needs a title!" {:book book})))
  ;; Lots of publishing stuff...
  )


;;----------In the wild----------------
(defn ensure-task-is-a-vector [task]
  (if (vector? task) task [task]))
;;a lot like the ternary expressions
;;The interesting thing about this expression is that it doesn’t do anything.