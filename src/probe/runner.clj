(ns probe.runner)
(use 'probe.core)
(use 'probe.util)
(use 'clojure.contrib.seq-utils)

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
