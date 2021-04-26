(ns clojure-in-action.basic)

(println "Hello, world!")


(println "Hello, world!")


;; REPL Who?
;; The REPL is one of the few programs whose name is its algorithm.
;; All the REPL does is read some code—the code you type in—eval-
;; uate the code, print the result, and then loop back to read some
;; more code: Read. Evaluate. Print. Loop.

(str "Hello," " " "world" "!") ; Returns the string "Hello, world!"

(count "Hello, world") ; Returns 12.
(count "") ; Returns 0.

(println true) ; Prints true...
(println "Nobody's home:" nil); Prints Nobody's home: nil

(+ 1900 84)
(/ (+ 1984 2010) 2)
;;(verb argument argument argument...)
(+ 1000 500 500 1); Evaluates to 2001.
(- 2000 10 4 2); Evaluates to 1984;

(def first-name "Russ")
(def the-average (/ (+ 20 40.0) 2.0))

;; Symbolic Rules?
;; As I say, there are very few rules about the characters that can
;; go into a symbol. But there are some: You can’t, for example
;; include parentheses, square brackets, or braces in your symbols
;; since these all have a special meaning to Clojure. For the same
;; reason, you can’t use the @ and ^ characters in your symbols.
;; There are also some special rules for the first character of your
;; symbols: you can’t kick your symbol off with a digit—it would be
;; too easily confused with a number—and symbols that start with
;; a colon are not actually symbols but rather keywords, which we’ll
;; talk about in Maps, Keywords, and Sets

(defn hello-world []
  (println "Hello, world!"))
(hello-world) ;;"Hello, world!"

;; Sans Tabs?
;; Why no tabs? Because one of the great mysteries of programming
;; is the exchange rate between tabs and spaces. Is it four spaces to
;; a tab? Eight? Three? It’s safer to stick to spaces.

(defn say-welcome [what]
  (println "Welcome to" what))
(say-welcome "Clojure") ;"Welcome to Clojure"

(defn average [a b]
  (/ (+ a b) 2.0))
(average 5.0 10.0); Returns 7.5

(defn chatty-average [a b]
  (println "chatty-average function called")
  (println "** first argument:" a)
  (println "** second argument:" b)
  (/ (+ a b) 2.0)) 
(chatty-average 10 20) 
;** first argument: 10
;** second argument: 20
;15.0

;;new Project
;;$ lein new app blottsbooks

;$ lein run

;; Declaration First?
;; There is a way to get around the you gotta define it before you use
;; it rule: you can use declare , as in (declare say-welcome) , to do a sort of
;; predefinition of a function.
;; Mostly Clojurists stick to defining their functions before they use
;; them, and reserve declare for sticky situations like mutually recursive functions.

