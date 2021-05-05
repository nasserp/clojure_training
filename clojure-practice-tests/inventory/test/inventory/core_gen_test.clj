(tc/quick-check 50
                (prop/for-all [i-and-b inventory-and-book-gen]
                              (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
                                 (:book i-and-b))))


;defspec
(ctest/defspec find-by-title-finds-books 50
  (prop/for-all [i-and-b inventory-and-book-gen]
                (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
                   (:book i-and-b))))

