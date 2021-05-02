(ns clojure-in-action.destructuring)

;**Pry Open Your Data**

(def artists [:monet :austen])

(let [painter (first artists)
      novelist (second artists)]
  (println painter novelist)) ;:monet :austen

;->destructuring:
(let [[painter novelist] artists]
  (println painter novelist)) ;:monet :austen

;**Getting Less than Everything**

(let [[painter novelist composer] artists]
  (println painter) ;:monet
  (println novelist) ;:austen
  (println composer)) ; nil

(let [[dummy novelist] artists]
  (println novelist)) ;::austen

(let [[painter] artists]
  (println painter)) ;:monet

(let [[_ novelist] artists]
  (println novelist)) ;::austen

;->Example of two-level vector
(def pairs [[:monet :austen] [:beethoven :dickinson]])

(let [[painter composer] pairs]
  (println painter) ;[:monet :austen]
  (println composer)) ;[:beethoven :dickinson]

(let [[[painter] [composer]] pairs]
  (println painter) ;:monet
  (println  composer)) ; :beethoven

(let [[[painter] [_ poet]] pairs]
  (println painter) ;:monet
  (println poet)) ;:dickinson

;**Destructuring in Sequence**

(def artist-list '(:monet :austen :beethoven :dickinson))

(let [[painter novelist composer] artist-list]
  (println painter) ;:monet
  (println novelist) ;:austen
  (println composer)) ;:beethoven

(let [[c1 c2 c3 c4] "Jane"]
  (println c1) ;j
  (println c2) ;a
  (println c3) ;n
  (println c4)) ;e

;**Digging into Maps**

(def artist-map {:painter :monet
                 :novelist :austen})

(let [{painter :painter writer :novelist} artist-map]
  (println painter) ;:monet
  (println writer)) ;:austen

(let [{writer :novelist painter :painter} artist-map]
  (println painter) ;:monet
  (println writer)) ;:austen

;**Diving into Nested Maps**

(def austen {:name "Jane Austen"
             :parents {:father "George" :mother "Cassandra"}
             :dates {:born 1775 :died 1817}})

(let [{name :name parents :parents dates :dates} austen]
  (println name) ;Jane Austen
  (println parents) ;{:father George, :mother Cassandra}
  (println dates)) ;{:born 1775, :died 1817}

(let [{name :name
       {father :father mother :mother} :parents
       {born :born died :died} :dates} austen]
  (println name) ;Jane Austen
  (println father) ;George
  (println born)) ;1775

(let [{{dad :father mom :mother} :parents} austen]
  (println dad) ;George
  (println mom)) ;Cassandra

(let [{name :name
       {mom :mother} :parents
       {dob :born} :dates} austen]
  (println name "was born in" dob) ;Jane Austen was born in 1775
  (println name "mother's name was" mom)) ;Jane Austen mother's name was Cassandra

;**The Final Frontier: Mixing and Matching**

;map-vector-map
(def author {:name "Jane Austen"
             :books [{:title "Sense and Sensibility" :published 1811}
                     {:title "Emma" :published 1815}]})

(let [{name :name
      books :books} author]
  (println name) ;Jane Austen
  (println books)) ;[{:title Sense and Sensibility, :published 1811} {:title Emma, :published 1815}]

(let [{name :name
       [book1 book2] :books} author]
  (println name) ;Jane Austen
  (println book1)) ;{:title Sense and Sensibility, :published 1811}

(let [{name :name
       [{book1-title :title book1-published :published}
        {book2-title :title book2-published :published}] :books} author]
  (println name) ;Jane Austen
  (println book2-title)) ;Emma

;->a couple of maps inside of a vector
(def authors
  [{:name "Jane Austen" :born 1775}
   {:name "Charles Dickens" :born 1812}])

(let [[{auther1-name :name auther1-born :born}
       {auther2-name :name auther2-born :born}] authors]
  (println auther2-name)) ;Charles Dickens


;**Going Further**

(def map1 {:name "Romeo" :age 16 :gender :male})

(defn character-desc [{name :name age :age gender :gender}] ;NOTIC: Destructure arguments
  (str "Name: " name " age: " age " gender: " gender))

(character-desc map1)

;easier
(defn character-desc [{:keys [name age gender]}] ;It says that you are going with the convention of using the keyword names as your local names
  (str "Name: " name " age: " age " gender: " gender))

(character-desc map1)

;->Even better
(defn character-desc [{:keys [name gender] age-in-years :age}]
  (str "Name: " name " age: " age-in-years " gender: " gender))


(defn add-greeting [character]
  (let [{:keys [name age]} character]
    (assoc character
           :greeting
           (str "Hello, my name is " name " and I am " age "."))))

(add-greeting {:name "naser" :age 36}) ;{:name "naser", :age 36, :greeting "Hello, my name is naser and I am 36."}

;-->shortcut with :as
;destructure in 'argument' NOT in 'let'
(defn add-greeting [{:keys [name age] :as character}]
  (assoc character
         :greeting
         (str "Hello, my name is " name " and I am " age ".")))

(add-greeting {:name "naser" :age 36 :additional "additional"}) ;{:name "naser", :age 36, :greeting "Hello, my name is naser and I am 36."}









