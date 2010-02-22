(ns probe-core)
(use 'clojure.contrib.seq-utils)

(defmulti pending-for-type :type)

(defmethod pending-for-type :context [context]
  (let [tests (:tests context)]
    (if (empty? tests)
      true
      (find-first pending-for-type tests))))

(defmethod pending-for-type :test [test]
  (empty? (:tests test)))

(defmethod pending-for-type :expects [test]
  (empty? (:tests test)))

(defn is-pending [coll]
  (let [result (find-first pending-for-type (flatten [coll]))]
    (not (or (nil? result) (empty? result)))))
