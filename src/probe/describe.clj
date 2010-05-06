(ns probe.describe)
(use 'probe)
(use 'probe.expect)
(use 'clojure.contrib.seq-utils)

(defn to-it [doc expectation]
  (list 'it doc
         {:type :expect
          :expectations [(list 'fn [] expectation)]}))

(defmacro strip-seq [a-seq]
  (subvec a-seq 1 (- 1 (count a-seq))))

(defmacro describe [& pairs]
  (let [partitioned (partition 2 pairs)
        its (map (fn [p] (to-it (first p) (second p))) partitioned) ]
    `[~@its]))
