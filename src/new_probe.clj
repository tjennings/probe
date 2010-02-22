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

(defn any?
  "TODO: This has to be in lib somewhere?
   Returns true if any element in the collection returns true for the predicate
   function"
  [pred coll]
  (not (empty? (filter #(pred %) coll))))

(defmulti run :type)

(defmethod run :context [context]
  (assoc context :tests
    (map run (flatten [(:tests context)]))))

(defmethod run :expects [it]
  (try
    (let [tests (:tests it)
          expectations (flatten (map (fn [t] (:expectations t)) tests))
          results (map (fn [e] (e)) expectations)
          failed (any? #(not (= true %)) results)]
      (assoc it :passed (not failed)))
  (catch Exception e
    (assoc it :passed false :error e))))

(defn context
  ([doc] (assoc default-context :doc doc))
  ([doc & children]
   (assoc default-context :doc doc :tests children)))

(defn it
  ([doc] (assoc default-test :type :expects :doc doc))
  ([doc & tests] (assoc default-test :type :expects :doc doc :tests tests)))

(defmacro pit 
  "Mark a test pending"
  ([doc & args] `(assoc default-test :doc ~doc)))

(defn testing
  "acts as the outer context of all tests, meant to be hooked and overridden by runners"
  [& args]
  (println (nested-printer (run (apply context args)))))
