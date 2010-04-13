(ns probe.core)
(use 'probe.core)
(use 'probe.runner)
(use 'clojure.contrib.seq-utils)

;; The two default data structures are defined below
;; Everything is stored as either a context or a test.

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
    :type :expects
    :doc ""
    :tests []))

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
    (not (or (nil? result)
             (empty? result)))))


(defn context
  ([doc] (assoc default-context :doc doc))
  ([doc & children]
   (assoc default-context :doc doc :tests children)))

(defn it
  ([doc] (assoc default-test :type :expects :doc doc))
  ([doc & tests] (assoc default-test :type :expects :doc doc :tests tests)))

(defmacro pit 
  "Mark a test pending"
  ([doc & args] `(assoc default-test :type :expects :doc ~doc)))

