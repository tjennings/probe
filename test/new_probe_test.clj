(ns new-probe-test
  (:use new_probe))

(context "outer"
  (context "inner"
    (it "should add 1 and 1" (= 2 (+ 1 1)))))

(defmacro is-eql [x y]
  `(if (= ~x ~y)
       (print ".")
     (do (println)
	 (println "FAIL: " '~y " is " ~y ", not " ~x))))

(defn empty-context
  "returns an pending struct with no doc" []
  (is-eql {:doc "" :pending? true} (context)))

(defn empty-it
  "returns an pending struct with no doc " []
  (is-eql {:doc "" :pending? true} (it)))

(defn pending-context
  "returns an pending flag and doc" []
  (is-eql {:doc "is pending" :pending? true} (context "is pending")))

(defn context-with-pending-test
  "returns an pending flag and doc for context and test" []
  (is-eql {:doc "pending with test" :pending? true
           :tests [{:doc "is pending" :pending? true}]}
          (context "pending with test" (it "is pending"))))

(defn context-with-test
  "returns a map containing the context and test" []
  (is-eql {:doc "with test" :pending? false
           :tests [{:doc "plus" :pending? false :tests '(= 2 (+ 1 1))}]}
          (context "with test"
                   (it "plus"
                       (= 2 (+ 1 1))))))

(def expected-passing-test
  {:doc "plus" :pending? false :passed true :tests '(= 2 (+ 1 1))})
(defn executing-passing-test []
  (is-eql expected-passing-test
          (run-one (it "plus" (= 2 (+ 1 1))))))

(def expected-failing-test
  {:doc "plus fails" :pending? false :passed false :tests '(= 2 (+ 1 2))})
(defn executing-failing-test []
  (is-eql expected-failing-test
          (run-one (it "plus fails" (= 2 (+ 1 2))))))

(def a-passed-test 
  {:doc "plus" :pending? false :passed true :tests '(= 2 (+ 1 1))})
(defn print-a-test []
  (is-eql (str green "plus" default)
          (nested-printer a-passed-test)))

(def a-failed-test 
  {:doc "plus" :pending? false :passed false :tests '(assert-equality 2 (+ 1 1))})
(defn print-a-test []
  (is-eql (str red "plus" default)
          (nested-printer a-failed-test)))

(testing "basic framework features"
  (it "preserves assertion forms"
     (assert-equality
       '(= 1 1)
       (:tests (run-one (it "passes" (= 1 1))))))

  (it "reports passing tests"
     (assert-equality
       (str green "  - passes" default "\n")
       (nested-printer (run-one (it "passes" (= 1 1))))))

  (it "reports failing tests"
    (assert-equality
      (str red "  - fails expected \"1\" but was \"2\"" default "\n")
      (nested-printer (run-one (it "fails" (assert-equality 1 2))))))

  (it "allows let forms in the test body"
     (assert-equality true
     (:passed (run-one
       (it "passes"
         (let [x 1]
           (= 1 x)))))))

  (it "it outputs nested context values correctly"
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

  (context "is pending")
    
)
