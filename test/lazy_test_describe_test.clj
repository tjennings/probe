(ns lazy-test-describe-test)
(use 'probe)

'(println (macroexpand '(describe "a" (= (+ 1 1) 2))))
'(println (macroexpand '(describe "plus"
              (= (+ 1 1) 3)
              "minus"
              (= (- 2 1) 1)
              "divide"
              (= (/ 4 2) 2))))

(testing "describe syntax"
  (describe "single assertion"
           (= (+ 1 1) 2))

  (context "many assertions"
    (describe "plus"
              (= (+ 1 1) 2)
              "minus"
              (= (- 2 1) 1)
              "divide"
              (= (/ 4 2) 2)))

  (context "a 'with' (or given)"
    (with [x 2 z 4]
      (describe "x and z are in my scope"
                (= (+ x z) (+ x z))

                "but it fails if I do my math wrong"
                (= (+ x z) (+ x 2))))))
