(ns clojure-in-action.def-symbols-vars)

;**A Global, Stable Place for Your Stuff**

;->def perfect for constants:
(def PI 3.14)
(def ISBN-LENGTH 13)
(def COMPANY-NAME "Blotts Books")

;->defn is mashed-up of def and fn
(def book-description
  (fn [book]
    (str (:title book)
         " Written by "
         (:author book))))

(defn book-description [book]
(str (:title book)
" Written by "
(:author book)))
  

;**Symbols Are Things**
(def author "Austen")
(str author) ;;"Austen" 
(str 'author);"author"
(def published) ;define symbol without value
  

;**Bindings Are Things Too**
(def author "Austen") ; Make a var.
#'author ; Get at the var for author -> "Austen".

(def my-author #'author) ; Grab the var.
(.get my-author) ;Get the value of the var: "Austen"
(.-sym my-author) ;Get the symbol of the var: author

;**Varying Your Vars**
;While your code is under development, mutable vars are a gift from heavenThe vars in a production program are just
;The vars in a production program are just as mutable as those in development, but you should avoid changing them.

;; Changing State?
;; So what’s a programmer to do if you have some state that you
;; need to model and that state changes over time? The longer answer
;; starts with the advice that you use atoms or refs or agents. The
;; shorter answer is to read Chapter 18, State, on page 215.

;->dynamics vars->we’ll talk about in Chapter 19
(def ^:dynamic *debug-enabled* false)
(defn debug [msg]
  (if *debug-enabled*
    (println msg)))
(binding [*debug-enabled* true]
  (debug "Calling that darned function")
  (prn "OK!")
  (debug "Back from that darned function"))

;->let does not create vars
(let [let-bound 42] #'let-bound) ;; Syntax error compiling var...Unable to resolve var

;;->Do use vars to weave the parts of your program together with the intention of revealing names(def, defn) and then leave them alone.
;;Don’t try to use vars as variables. In particular, don’t rely on changing the value of a var
;;And yes, binding also exists. But you should not be doing any of that terribly often.
;;Clojure a tool for modeling the changing state of the world. we’ll look at in Chapter 18, State, on page 215.
