(ns probe-props
  (:use probe))
Hello world

;; a pending property
;(property
;  "hey, someone implement me")
;
;; a simple truth with no generators
;(property
;  "testing the language"
;  (= 2 (+ 1 1)))
;
;; a simple failure with no generators
;(property
;  "testing the language"
;  (= 2 (+ 1 2)))
;
;(println (macroexpand-1 '(property
;  "let's try addition with a generator"
;  [gen-int x]
;  (= (+ x x) (+ x x)))))
;
;(property
;  "let's try addition with a generator"
;  [gen-int x]
;  (= (+ x x) (+ x x)))


(defn g [] [0 1 2 3])

(testing
  (context "a suite of statements evaluating some doodad"
    (context "making some blanket assertions about arithmetic in clojure"
      (it "A pending spec")
      (for-all g [x] (= (* x 2) (+ x x)))
      (for-all g [x] (< (* x 2) (+ x 2)))
    )

    (context "making some blanket assertions about arithmetic in clojure"
      (for-all g [x] (= (* x 2) (+ x x)))
      (for-all g [x] (< (* x 2) (+ x 2)))
    )
))

;(println (for-all [an-integer an-integer] [x y] (is (< (* x 2) (+ x x)))))

;(for-all [x Integer, y Integer]
;(for-all [x
;(property
;
;  "Checking edges"
;  [fat-tail [-1 0 1]]
;  [gen-int x]
;  (= ... ))
