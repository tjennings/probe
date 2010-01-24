(defn add_ten [x] (+ x 10))

(property
  "adding ten to x does what it says"
  [decimal x]
  (check (= (add_ten x) (+ x 10))))

(for-all
  "seq variants"
  (using [a-seq x]
    (when (> x 0) (= (% x 2) 0))
  (property
    "reversing twice returns the original seq"
    (= x (reverse (reverse x))))))

;define a custom generator
(defgen positive-decimal decimal (when (> x 0)))
