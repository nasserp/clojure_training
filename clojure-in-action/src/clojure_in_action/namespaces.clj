(ns clojure-in-action.namespaces)

;**A Place for Your Vars**

(ns pricing)
(def discount-rate 0.15)
(defn discount-price [book]
  (* (- 1.0 discount-rate) (:price book)))

(ns user)
(def discount-rate 0.50)
(defn discount-price [book]
  (* (- 1.0 discount-rate) (:price book)))


;; Back to the pricing namespace.
(ns pricing)
(println (discount-price {:title "Emma" :price 9.99}))

;; Back to the pricing namespace.
(ns user)
(println (discount-price {:title "Emma" :price 9.99}))


;;**Loading Namespaces**
(require 'clojure.data)

;;**A Namespace of Your Own**