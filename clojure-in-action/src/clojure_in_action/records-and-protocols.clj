(ns clojure-in-action.records-and-protocols)


;**The Trouble with Maps**
;-Maps are flexible and speedy but the deal with flexibility has a runtime penalty, example process huge amounts of data
;-Maps is their cost in terms of code coherence and documentation because you can put annything into any map

;Since you can put anything into any map, the only way to discern the intent of maps like these
;A fictional character.
(defn get-watson-1 []
  {:name "John Watson"
   :appears-in "Sign of the Four"
   :author "Doyle"})

(get-watson-1) ;{:name "John Watson", :appears-in "Sign of the Four", :author "Doyle"}
;or
; A Jeopardy playing computer.
(defn get-watson-2 [] {:cpu "Power7" :no-cpus 2880 :storage-gb 4000})


;**Striking a More Specific Bargain with Records**

;Records as maps with some predefined keys.
(defrecord FictionalCharacter [name appears-in author])
;Behind the scenes, defrecord creates a couple of functions:->FictionalCharacter and map->FictionalCharacter .

;; Instant Vars
;; Since defrecord begins with def it’s reasonable to assume that it is in
;; the var-creation business. In fact, defrecord creates a number of vars.
;; There’s one for the record type, and one each for the two factory
;; functions. The same is true of defprotocol , which we’ll meet presently.

(def watson (->FictionalCharacter "John Watson" "Sign of the Four" "Doyle"))

(prn watson) ;#clojure_in_action.records_and_protocols.FictionalCharacter{:name "John Watson", :appears-in "Sign of the Four", :author "Doyle"}

(def elizabeth (map->FictionalCharacter
                {:name "Elizabeth Bennet"
                 :appears-in "Pride & Prejudice"
                 :author "Austen"}))

(prn elizabeth)#clojure_in_action.records_and_protocols.FictionalCharacter{:name "Elizabeth Bennet", :appears-in "Pride & Prejudice", :author "Austen"}

(def watson (->FictionalCharacter "John Watson" "Sign of the Four" "Doyle" "fasdf"))
(prn watson) ;Execution error... Wrong number of args (4) passed to

(def elizabeth (map->FictionalCharacter
                {:name "Elizabeth"
                 :family "Bennet"
                 :appears-in "Pride & Prejudice"
                 :author "Austen"}))
(prn elizabeth) ;FictionalCharacter {:name "Elizabeth", :appears-in "Pride & Prejudice", :author "Austen", :family "Bennet"}

;**Records Are Maps**
(:name elizabeth) ;"Elizabeth"
(:appears-in watson) ;"Sign of the Four"
(count elizabeth) ;4
(keys watson) ;(:name :appears-in :author)
(def specific-watson (assoc watson :appears-in "Sign of the Four"))
(prn specific-watson) ;FictionalCharacter {:name "John Watson", :appears-in "Sign of the Four", :author "Doyle"}

;**The Record Advantage**
;-the performance advantages when dealing with large mounds of data(because it is hard-wired fields)
;-code clearer

;the 'class' function, which will return the type of the record.
(defrecord FictionalCharacter [name appears-in author])
(defrecord SuperComputer [cpu no-cpus storage-gb])
(def watson-2 (->SuperComputer "Power7" 2880 4000))
(class watson-2) ;SuperComputer
(instance? SuperComputer watson-2) ;true

;; Don't do this!
;; (defn process-thing [x]
;;   (if (= (instance? FictionalCharacter x))
;;     (process-fictional-character x)
;;     (process-computer x)))

;; Class?
;; The class function works for all values, not just records. A good
;; rainy-day programming project is to spend some time feeding
;; values into class to see what comes out.

;**Protocols**
;A Clojure protocol is a set of how to behave rules expressed in code

(defrecord Employee [first-name last-name department])
(def alice (->Employee "Alice" "Smith" "Engineering"))
(defrecord FictionalCharacter [name appears-in author])
(def watson (->FictionalCharacter "John Watson" "Sign of the Four" "Doyle"))
;Here’s a protocol definition that we might use to leverage the common aspects of employees and fictional characters:
(defprotocol Person
  (full-name [this])
  (greeting [this msg])
  (description [this]))

;-The protocol is tacked on to the end of each record definition
;-These method definitions,such as 'full-name' without the defn
;-Each method needs to have at least one parameter
;-Have more than one of this parameter then 'this' must be the first parameter.
(defrecord FictionalCharacter [name appears-in author]
  Person
  (full-name [this] (:name this))
  (greeting [this msg] (str msg " " (:name this)))
  (description [this]
    (str (:name this) " is a character in " (:appears-in this))))

(defrecord Employee [first-name last-name department]
  Person
  (full-name [this] (str first-name " " last-name))
  (greeting [this msg] (str msg " " (:first-name this)))
  (description [this]
    (str (:first-name this) " works in " (:department this))))

(def sam (->FictionalCharacter "Sam Weller" "The Pickwick Papers" "Dickens"))
(def sofia (->Employee "Sofia" "Diego" "Finance"))

(:first-name sofia) ;"Sofia"
(full-name sofia) ;"Sofia Diego"
(greeting sofia "Hello!");"Hello! Sofia" 

;**Decentralized Polymorphism**
;-You can define and implement Protoclo after the fact.

(defprotocol Marketable
  (make-slogan [this]))

;You go back and modify Records, but you don't have to.Armed with Clojure’s extend-protocol
(extend-protocol Marketable
  Employee
  (make-slogan [e] (str (:first-name e) " is the BEST employee!"))
  FictionalCharacter
  (make-slogan [fc] (str (:name fc) " is the GREATEST character!"))
  SuperComputer
  (make-slogan [sc] (str "This computer has " (:no-cpus sc) " CPUs!")))

;You can even extend your protocol to embrace data types that aren’t records:
(extend-protocol Marketable
  String
  (make-slogan [s] (str \" s \" " is a string! WOW!"))
  Boolean
  (make-slogan [b] (str b " is one of the two surviving Booleans!")))

(make-slogan "StringInstance") ;"\"StringInstance\" is a string! WOW!"

;**Record Confusion**

;-Record types and their instances do look a lot like the classes and objects that you find in object-oriented languages.
;-Protocols clearly resemble the abstract types or interfaces that you also find in the object-oriented world.
;-Records are Clojure’s approach to building a structured, composite data type with predefined fields.
;-Protocols are type-based polymorphism—you can have a single operation implemented in different ways by different types

;Protocol vs Multi-method
;-Each multimethod defines a single, stand-alone operation
;-Multi-methods support a completely arbitrary dispatch mechanism.
;-Protocols can include a whole bundle of related operations.
;-Protocols dispatch based on a type mechanism.
;-If you don’t need all of the generality of a multimethod you are better off using a protocol.


;**Staying Out of Trouble**

;-While this code may look plausible, it’s
nonsense:
(map->FictionalCharacter {:full-name "Elizabeth Bennet"
                          :book "Pride & Prejudice"
                          :written-by "Austen"})
;FictionalCharacter {:name nil, :appears-in nil, :author nil, :full-name "Elizabeth Bennet", :book "Pride & Prejudice", :written-by "Austen"}
(assoc elizabeth :book "Pride & Prejudice")
;FictionalCharacter {:name "Elizabeth", :appears-in "Pride & Prejudice", :author "Austen", :family "Bennet", :book "Pride & Prejudice"}


;-about protocol
(defprotocol Person-2
  (name [this])
  (greeting-2[this msg])
  (description-2 [this])) ;;; WARNING: name already refers to...
;;name is already a built-in function that comes with Clojure.
(defprotocol Person-3
  (name-3 [this])
  (greeting-3 [this msg])
  (description-2 [this])) ;;WARNING:...Person-3 is overwriting method description-2 of protocol Person-2

;=>put each protocol in its own namespace

;deftype: Records with a fair bit of built-in behavior
;