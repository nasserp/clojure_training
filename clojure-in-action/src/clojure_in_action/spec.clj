(ns clojure-in-action.spec
  (:require [clojure.spec.alpha :as s]))

;clojure.spec , a library that enables you to validate the shape of your values.

;**This Is the Data You’re Looking For**

{:title "Getting Clojure" :author "Olsen" :copies 1000000}

;the code-it-by-hand approach to validating data
(defn book? [x]
  (and
   (map? x)
   (string? (:author x))
   (string? (:title x))
   (pos-int? (:copies x))))

;use spec
(s/valid? number? 44) ;true
(s/valid? number? :hello) ;false

;; Clojure Dot Spec Dot Alpha?
;; As I write these words clojure.spec is in the process of being finished
;; off, which explains the alpha in the namespace name. Depending on
;; when you’re reading this, that alpha may or may not still be there.
;; Note also that while clojure.spec is well integrated with Clojure, it is
;; delivered as a separate library. Thus if you are using Leiningen
;; you will need an additional dependency entry in your project file.


(def n-gt-10 (s/and number? #(> % 10)))
(s/valid? n-gt-10 1) ;false
(s/valid? n-gt-10 10) ;false
(s/valid? n-gt-10 11) ;true

(def n-gt-10-lt-100
  (s/and number? #(> % 10) #(< % 100)))

(def n-or-s (s/or :a-number number? :a-string string?))
(s/valid? n-or-s "Hello!") ;true
(s/valid? n-or-s 99) ;true
(s/valid? n-or-s 'foo) ;false
;The keyword is required to help in producing coherent feedback when a spec fails to match.

(def n-gt-10-or-s (s/or :greater-10 n-gt-10 :a-symbol symbol?))

;**Spec’ing Collections**

(def coll-of-strings (s/coll-of string?))
(s/valid? coll-of-strings '("Alice" "In" "Wonderland")) ;true
(s/valid? coll-of-strings ["Alice" "In" "Wonderland"]) ;tru
(s/valid? coll-of-strings '("Alice" "In" 5));false

;->'cat' lets you specify this should follow that in a collection.
;Note that like or , cat requires descriptive keywords.
(def s-n-s-n (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))
(s/valid? s-n-s-n ["Emma" 1815 "Jaws" 1974]) ;true
(s/valid? s-n-s-n ["Emma" 1815 "Jaws" 1974 "hey"]) ;false

;-The 'keys' function is using for specs of Map 
(def book-s
  (s/keys :req-un [:title
                   :author
                   :copies]))
(s/valid? book-s {:title "Emma" :author "Austen" :copies 10})
;=>all keys must be namespace-qualified keywords

(def book-s
  (s/keys :req-un [:my-namespace/title
                   :my-namespace/author
                   :my-namespace/copies]))
(s/valid? book-s {:title "Emma" :author "Austen" :copies 10}) ;true
(s/valid? book-s {:title "Arabian Nights" :copies 17}) ;false
(s/valid? book-s {:title "2001" :author "Clarke" :copies 1 :published 1968}) ;true OPS!

;**Registering Specs**

;-clojure.spec/def is to allow you to register your spec in a JVM-wide registry

(s/def
  :my-namespace/book
  (s/keys
   :req-un
   [:my-namespace/title :my-namespace/author :my-namespace/copies]))

(s/valid? :my-namespace/book {:title "Dracula" :author "Stoker" :copies 10}) ;true

;-current namespace
(s/def ::book (s/keys :req-un [::title ::author ::copies]))

(s/valid? ::book {:title "Dracula" :author "Stoker" :copies 10}) ;true

;**Spec’ing Maps (Again)**

(s/def ::title string?)
(s/def ::author string?)
(s/def ::copies int?)
(s/def ::book (s/keys :req-un [::title ::author ::copies]))

(s/valid? ::book {:title 1234 :author false :copies "many"}) ;false

;**Why? Why? Why?**

(s/valid? ::book {:author :austen :title :emma})