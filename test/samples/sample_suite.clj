(ns expect-test
  (:use new_probe)
  (:use probe-core)
  (:use expect)
  (:use clojure.contrib.seq-utils)
  (:use nested-printer))

(defn add-two [x] (+ 2 x))

(testing "showcasing some core behavior"
  (it "works" (expect (+ 2 1) to (equal 3)))
  (it "fails" (expect (+ 1 1) to (equal 3)))

  (it "works for multiple assertions"
       (expect (+ 2 1)
               to (equal 3)
               to (< 2)))
  (let [x 2]
    (it "captures scope"
         (expect (add-two x)
                 to (equal 4))))

  (context "predicate functions"
    (it "works with =" (expect (+ 1 1) to (= 2)))
    (it "fails with =" (expect (+ 1 2) to (= 2))))

  (context "multiple expectations"
    (it "works"
         (expect (+ 1 1) to (= 2))
         (expect (+ 2 2) to (equal 4)))
    (it "fails"
         (expect (+ 1 1) to (= 2))
         (expect (+ 2 3) to (equal 4))))

  (context "not - expect the inverse of an assertion"
    (it "works for simple predicates"
        (expect (+ 1 1) to (not! (= 3))))
    (it "works for framework assertions"
        (expect (+ 2 3) to (not! (equal 3))))))
