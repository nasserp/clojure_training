(ns clojure-in-action.let)

(defn compute-discount-amount [amount discount-percent min-charge]
  (if (> (* amount (- 1.0 discount-percent)) min-charge)
    (* amount (- 1.0 discount-percent))
    min-charge))

;; Don't do this!  
(defn compute-discount-amount [amount discount-percent min-charge]
  (def discounted-amount (* amount (- 1.0 discount-percent))); NOOOOO!
  (if (> discounted-amount min-charge)
    discounted-amount
    min-charge))
;because symbols bound with def have reasonably global visibility

;; Do use let! 
(defn compute-discount-amount [amount discount-percent min-charge]
  (let [discounted-amount (* amount (- 1.0 discount-percent))]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))
;Let is uded to 'A Local, Temporary Place for Your Stuff'

(defn compute-discount-amount [amount discount-percent min-charge]
  (let [discount (* amount discount-percent)
        discounted-amount (- amount discount)]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

(defn compute-discount-amount [amount discount-percent min-charge]
  (let [discount (* amount discount-percent)
        discounted-amount (- amount discount)]
    (println "Discount:" discount)
    (println "Discounted amount" discounted-amount)
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

;**Let Over Fn**
(def user-discounts
  {"Nicholas" 0.10 "Jonathan" 0.07 "Felicia" 0.05})

(defn compute-discount-amount [amount user-name user-discounts min-charge]
  (let [discount-percent (user-discounts user-name)
        discount (* amount discount-percent)
        discounted-amount (- amount discount)]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

(defn mk-discount-price-f [user-name user-discounts min-charge]
  (let [discount-percent (user-discounts user-name)]
    (fn [amount]
      (let [discount (* amount discount-percent)
            discounted-amount (- amount discount)]
        (if (> discounted-amount min-charge)
          discounted-amount
          min-charge)))))
;; Get a price function for Felicia.
(def compute-felicia-price (mk-discount-price-f "Felicia" user-discounts 10.0))
;; ...and sometime later compute a price
(compute-felicia-price 20.0)

;**Variations on the Theme***
(def anonymous-book
  {:title "Sir Gawain and the Green Knight"})
(def with-author
  {:title "Once and Future King" :author "White"})

(defn uppercase-author [book]
  (let [author (:author book)]
    (if author
      (.toUpperCase author))))
;; => #'clojure-in-action.let/uppercase-author

;->with if-let :
(defn uppercase-author [book]
  (if-let [author (:author book)]
    (.toUpperCase author)))

(defn uppercase-author [book]
  (if-let [author (:author book)]
    (.toUpperCase author)
    "ANONYMOUS"))

;->with when-let:
(defn uppercase-author [book]
  (when-let [author (:author book)]
    (.toUpperCase author)))

(let [title "Pride and Prejudice"]
  (let [title "Sense and Sensibility"]
    (println title))) ;; Sense & Sensibility.

(let [title "Pride and Prejudice" 
      title (str title " and Zombies")] 
  (println title)) ; Pride and Prejudice and Zombies