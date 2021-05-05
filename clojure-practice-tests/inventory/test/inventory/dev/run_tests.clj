(require '[inventory.core-test :as ct])

(ct/test-finding-books)

;; Three ways to run the tests in a namespace.
(test/run-tests) ;it will run all the tests in the current namespace
(test/run-tests *ns*)
(test/run-tests 'inventory.core-test)

;to run all the tests in all the namespaces in your project:
;$ lein test
