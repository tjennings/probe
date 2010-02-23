(ns expect
  (:use clojure.contrib.seq-utils))

(defn remove-noise-words [tos]
  (filter #(not (= 'to %)) tos)) ;;TODO - ugly! what's the inverse of filter?

(defn execute-expect [expect]
  )

(defmacro quoted-fn [& args] `(list 'fn [~@args]))

;;TODO - There MUST be a lib function for this
(defn append [s e] (reverse (cons e (reverse s))))

(defn reorder-and-wrap-with-fn [test]
  (fn [assertion] 
    (append (quoted-fn) (append assertion test)))) 

(defn build-tests [test assertions]
  (let [noise-less (remove-noise-words assertions)
        tests (map (reorder-and-wrap-with-fn test) noise-less)]
    tests))

(defmacro expect [test & tos]
  (let [expectations (build-tests test tos)]
    `{:type :expect
     :expectations (list ~@expectations)}))

(defmacro not! [assertion test]
  '(try
     (not ~(conj assertion [test]))
   (catch probe.AssertionFailed e
     true)))
              
(defn fail [message]
  (throw (new probe.AssertionFailed
              message
              [nil nil])))

(defn equal [expected actual]
  (if (= expected actual)
    true
    (fail (str "Expected " expected " but was " actual))))

(defmacro throw-error [exception-type form]
  `(let [result# (try 
                   ~form
                   false
                 (catch ~exception-type e#
                   true))]
    (if result#
      true
      (fail (str "Expected " ~exception-type " was not thrown.")))))
  
