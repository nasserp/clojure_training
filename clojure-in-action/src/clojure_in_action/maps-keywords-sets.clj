(ns clojure-in-action.maps-keywords-sets)

;;-------------------Maps-------------------------
{"title" "Oliver Twist" "author" "Dickens" "published" 1838}
(hash-map "title" "Oliver Twist"
          "author" "Dickens"
          "published" 1838);;{"author" "Dickens", "published" 1838, "title" "Oliver Twist"} Notic:No order

;; Why Not Just map?
;; Yes, the name of the function that manufactures new maps is hash-
;; map , not map . There is a map function, which we’ll meet presently
;; but it does something different.

(def book {"title" "Oliver Twist"
           "author" "Dickens"
           "published" 1838})
(get book "published"); Returns 1838.
(book "published") ; Returns 1838. Notic: It's equal (get book "published")


;;-------------------Keywords-------------------------
;;Key-words are a basic data type that comes packaged with Clojure.
:title
:author
:published
:word-count
:preface&introduction
:chapter-1-and-2

;; Keywords Behind the Scenes
;; Technically, keywords are interned strings, similar to symbols in
;; Ruby and distant cousins to the individual items that go into
;; enumerated types in other languages.

(def book
  {:title "Oliver Twist" :author "Dickens" :published 1838})
(str "title:" (book :title)) ;;"title:Oliver Twist"
(:title book)
(assoc book :page-count 362) ;;{:title "Oliver Twist", :author "Dickens", :published 1838, :page-count 362}
(assoc book :page-count 362 :title "War & Peace")
(dissoc book :published) ;;{:title "Oliver Twist", :author "Dickens"}
(dissoc book :title :author :published) ;;{}
(dissoc book :paperback :illustrator :favorite-zoo-animal) ;;{:title "Oliver Twist", :author "Dickens", :published 1838}

;; Associative Vectors
;; Vectors and maps have a lot in common. They both associate keys
;; with values, the difference being that with vectors the keys are
;; limited to integers while in maps the keys can be more or less
;; anything.
;; That is, in fact, how Clojure looks at vectors—which means that
;; many of the functions that work with maps will also work with
;; vectors. For example, assoc and dissoc work fine on vectors. Thus
;; (assoc [:title :by :published] 1 :author) will give you [:title :author :published] .

(vals book)
(keys book)

;; Sorted Maps
;; There is a second flavor of map that keeps its keys sorted. You
;; can make one of these sorted maps with the aptly named function
;; sorted-map .



;;------------------------Sets-------------------------------------
(def genres #{:sci-fi :romance :mystery})
(def authors #{"Dickens" "Austen" "King"})
;;#{"Dickens" "Austen" "Dickens"} ----> Duplicate key: Dickens
(contains? authors "Austen") ; => true
(contains? genres "Austen") ; => false
(authors "Austen") ; => "Austen" !OPS
(genres :historical) ; => nil
(:sci-fi genres) ; => :sci-fi
(:historical genres) ; => nil

(conj authors "Clarke") ;;#{"King" "Dickens" "Clarke" "Austen"}
(disj authors "King") ;;#{"Dickens" "Austen"}