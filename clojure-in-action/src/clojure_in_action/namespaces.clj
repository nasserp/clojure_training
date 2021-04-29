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
;; -->   /src/blottsbooks/core.clj
;; (ns blottsbooks.core
;; (:require blottsbooks.pricing)
;; (:gen-class))
;; (defn -main []
;;   (println
;;    (blottsbooks.pricing/discount-price
;;     {:title "Emma" :price 9.99})))

;;**A Namespace of Your Own**

;; take the namespace name, convert any periods to directory-separating slashes, slap
;; a .clj on the end, and voilà: blottsbooks.core is found in blottsbooks/core.clj .

;; Class Paths
;; Along with the namespace-to-file-name transformation, Clojure
;; also relies on the Java class path—essentially a list of places that
;; the JVM looks for code—to help it locate namespaces. This is how
;; Clojure knows to look in the src directory, and how it manages to
;; locate the built-in Clojure library code. More on this in Interoper-
;; ating with Java.

;;If you happen to have any dashes in your namespace name—perhaps it’s
;; called blotts-books.current-pricing —then the dashes get converted to underscores
;; in the file name: blotts_books/current_pricing.clj .


;;**As and Refer**

;->As
;; (ns blottsbooks.core
;;   (:require [blottsbooks.pricing :as pricing])
;;   (:gen-class))
;; (defn -main []
;;   (println
;;    (pricing/discount-price {:title "Emma" :price 9.99})))

;-> Refer
;; (ns blottsbooks.core
;;   (require '[blottsbooks.pricing :refer [discount-price]])
;;   (:gen-class))
;; (defn -main []
;;   (println
;;   (discount-price {:title "Emma" :price 9.99})
;;->Note: what if we already have a discount-price function defined?In that case :refer will overwrite it.

;; REPL Prompts
;; Now that we’ve looked at namespaces, we can finally resolve the
;; Great REPL Prompt Mystery. REPLs generally include the name
;; of the current namespace in their prompts. If you start a REPL
;; with Leiningen outside of a project directory, your initial names-
;; pace will be user , and that’s what you will see in your prompt. On
;; the other hand, if you start a REPL from inside of a Clojure project
;; directory, Leiningen will default to the core namespace of that project.




;;**Namespaces, Symbols, and Keywords**

;->get at the current namespace
(println "Current ns:" *ns*)
;->ook up any existing namespace by name
(find-ns 'clojure-in-action.namespaces); Get the namespace called 'user.
;->discover all the things defined in that namespace
(ns-map 'clojure-in-action.namespaces); Includes all the predefined vars.
;; Gives us "pricing"
(namespace 'pricing/discount-print)
;symbole of namespace is just a name, not a reference to a namespace value
;Thus we can make up symbols with nonexistent namespaces
(namespace 'pricing1/discount-print) ;;pricing1
;if you remove the quote, as in pricing1/discount-print , then you are trying to look up discount-print in the pricing1 namespace

;calling keyword wit it's namespace
:blottsbooks.pricing/author

::author ;->the keyword will pick up the current namespace