(ns new_probe)
(use 'nested-printer)
(use 'probe-core)
(use 'clojure.contrib.seq-utils)

(defstruct context-info
  :type
  :doc
  :tests)

(def default-context
  (struct-map context-info
    :type :context
    :doc ""
    :tests []))

(def default-test
  (struct-map context-info
    :type :test
    :doc ""
    :tests []))

(defstruct assertion-result
  :type
  :result
  :message)

(defn assert-equality-message [result left right]
  (if (not result)
    (str "expected \"" left "\" but was \"" right "\"")))

(defn assert-equality [left right]
  (let [result (= left right)]
    (struct-map assertion-result
      :result result
      :message (assert-equality-message result left right))))

(defmulti run-one :type)

(defmethod run-one :context [context]
  (assoc context :tests
    (map run-one (flatten [(:tests context)]))))

(defmethod run-one :test [test]
  (let [result (eval (first (:tests test)))]
    (if (contains? result :message) ;hacking this in for now
      (do
        (-> test
        (assoc :passed (:result result))
        (assoc :assertions result)))
      (assoc test :passed result))))

(defn run [context] (run-one context))

(defn context
  ([doc] (assoc default-context :doc doc))
  ([doc & children]
   (assoc default-context :doc doc :tests children)))

(defmacro it 
  ([doc] `(assoc default-test :doc ~doc))
  ([doc & tests]
   `(assoc default-test :doc ~doc :tests '[~@tests])))


(defmacro pit 
  ([doc & args] `(assoc default-test :doc ~doc)))

(defn testing
  "acts as the outer context of all tests, meant to be hooked and overridden by runners"
  [& args]
  (println (nested-printer (run (apply context args)))))
