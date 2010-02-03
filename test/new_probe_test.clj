(ns new-probe-test
  (:use new_probe)
  (:use probe-core)
  (:use nested-printer))

(testing "basic framework features"
  (it "preserves assertion forms"
     (assert-equality
       '(= 1 1)
       (first (:tests (run-one (it "passes" (= 1 1)))))))

  (it "reports passing tests"
     (assert-equality
       (str green "- passes" default "\n")
       (nested-printer (run-one (it "passes" (= 1 1))))))

  (it "reports failing tests"
    (assert-equality
      (str red "- fails expected \"1\" but was \"2\"" default "\n")
      (nested-printer (run-one (it "fails" (assert-equality 1 2))))))

  (it "allows let forms in the test body"
     (assert-equality true
     (:passed (run-one
       (it "passes"
         (let [x 1]
           (= 1 x)))))))

  (it "outputs nested context values correctly"
    (assert-equality (str default "outer" default "\n"
                          green "  - " "inner" default "\n")
                     (nested-printer
                       (run
                         (context "outer"
                           (it "inner" (assert-equality 1 1)))))))

  (it "allows pending contexts"
    (let [actual (run (context "outer"))]
      (assert-equality (str brown "outer" default "\n")
                       (nested-printer actual))))

  (it "allows pending tests in a context"
      (assert-equality (str brown "c" default "\n"
                            brown "  - inner" default "\n")
                       (nested-printer (run (context "c" (it "inner"))))))
)

(testing "nested printer"
  (it "prints a nested context with a test correctly"
    (assert-equality (str brown "a" default "\n"
                          brown "  b" default "\n"
                          default "  c" default "\n"
                          green "    - test" default "\n")
                     (nested-printer
                       (run
                         (context "a"
                            (context "b")
                            (context "c"
                              (it "test" (= 1 1))))))))
)

(defn two-fn [] 2)
(def two 2)

(testing "it"
 (it "captures the local scope" (= two (two-fn))))

(testing "is-pending"
  (it "returns true for a pending test"
    (= true (is-pending (it "is pending"))))

  (it "returns false for an implemented test"
    (= false (is-pending (it "passes" (= 2 2)))))

  (it "returns true for a pending context"
    (= true (is-pending (context "pending"))))

  (it "returns true for a pending nested context"
    (= true (is-pending (context "pending" (context "still pending")))))

  (it "returns true for a pending nested context with a test"
    (= true (is-pending (context "pending"
                          (context "still pending"
                            (it "isn't workin"))))))

  (it "returns true for a nested context with any pending children "
    (= true (is-pending 
              (run
                (context "pending"
                  (context "pending")
                  (context "notpending"
                    (it "is true" (= 1 1))))))))
)

(testing "exception handling")

(testing "evaluation of properties"
  (context "generators")
  (context "defininig properties")
  (context "performance evaluation"
    (context "executing time")
    (context "big-O evaluation")))

;; Crazy syntax BHAG
'(testing "the syntax"
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
