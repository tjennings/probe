(ns test-finder-test)
(use 'probe)
(use 'probe.nested-runner)
(use 'probe.test-finder)
(use 'clojure.contrib.seq-utils)

(println (find-tests "./test/runner_tests"))

(testing "the test finder"
  (it "finds the tests in the runner_tests folder"
    (expect (empty? (find-tests "./test/runner_tests")) to (equal false))
    ;; TODO - we should have a more natural way to write this. 
    (expect (includes? (find-tests "./test/runner_tests") "./test/runner_tests/nested/c_test.clj") to (equal true))
    (expect (includes? (find-tests "./test/runner_tests") "./test/runner_tests/a_test.clj") to (equal true))
    (expect (includes? (find-tests "./test/runner_tests") "./test/runner_tests/b_test.clj") to (equal true)))
  (it "does not find files that do not end in _test"
    (expect (includes? (find-tests "./test/runner_tests") "./test/runner_tests/test_i_am_not.clj") to (equal false)))
)