(ns probe-test
  (:use probe)
  (:use probe.core)
  (:use probe.runner)
  (:use expect)
  (:use nested-printer))

(testing "contexts"
  (it "outputs nested context values correctly"
    (expect
      (nested-printer
        (run
          (context "outer"
            (it "inner" (expect (+ 1 1) to (equal 2))))))
    to (equal
      (str default "outer" default "\n"
           green "  - " "inner" default "\n"))))

  (it "allows pending contexts"
    (expect
      (nested-printer (run (context "outer")))
    to (equal (str brown "outer" default "\n"))))

  (it "allows pending tests in a context"
      (expect
        (nested-printer (run (context "c" (it "inner"))))
      to (equal
        (str brown "c" default "\n"
             brown "  - inner" default "\n"))))
)

(testing "nested printer"
  (it "prints a nested context with a test correctly"
    (expect
      (nested-printer
        (run
          (context "a"
            (context "b")
            (context "c"
              (it "test" (expect (= 1 1) to (= true)))))))
    to (equal 
      (str brown "a" default "\n"
           brown "  b" default "\n"
           default "  c" default "\n"
           green "    - test" default "\n")))))

(testing "is-pending"
  (it "returns true for a pending test"
    (expect (is-pending (it "is pending")) to (= true)))

  (it "returns false for an implemented test"
    (expect  (is-pending (it "passes" (= 2 2))) to (= false)))

  (it "returns true for a pending context"
    (expect (is-pending (context "pending")) to (= true)))

  (it "returns true for a pending nested context"
    (expect
      (is-pending (context "pending" (context "still pending")))
     to (equal true)))

  (it "returns true for a pending nested context with a test"
    (expect
      (is-pending (context "pending"
                          (context "still pending"
                            (it "isn't workin"))))
    to (equal true)))

  (it "returns true for a nested context with any pending children "
    (expect 
      (is-pending 
        (run
          (context "pending"
            (context "pending")
            (context "notpending"
              (it "is true" (expect (= 1 1) to (= true)))))))
     to (equal true))))

(testing "expect"
  (it "returns true when the assertion passes"
      (expect (equal 3 3) to (= true)))
  (it "throws an AssertionFailedException when the assertion fails"
      (expect (equal 2 3) to (throw-error probe.AssertionFailed))))

(testing "exception handling"
  (it "catches errors and reports them")
  (it "allows you to expect an exception")
  (it "allows you to expect that an exception is NOT thrown"))

(testing "evaluation of properties"
  (context "generators")
  (context "defininig properties")
  (context "performance evaluation"
    (context "timing execution")
    (context "big-O evaluation")))

;; Crazy syntax BHAG
'(testing "the syntax"
   (context "properties"
     (property "first of a list is same as last of the reversal of the list"
        (generators [a-seq (gen-seq gen-int)])
        (expect (first a-seq))
        (to (equal (last (reverse a-seq))))))

   (context "variations on its"
     (define [z 5])

     (it "can do some amazing things"
       (define [x 1
                y 4])
       (expect (+ x y ))
         (to (equal z))
         (to (> x)))

     (it "can expect exceptions"
       (expect (/ 1 0 ))
       (to (throw RuntimeException)))

     (it "can assert performance"
       (expect (/ 1 0 ))
       (to (perform-in O(1))))))
