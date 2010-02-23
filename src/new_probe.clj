(ns new_probe)
(use 'nested-printer)
(use 'probe-core)
(use 'probe.runner)
(use 'clojure.contrib.seq-utils)

(defstruct context-info
  :type
  :doc
  :tests)

;; TODO: There has to be a reasonable way to do this
;; in clojure
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

(defn testing
  "acts as the outer context of all tests, meant to be hooked and overridden by runners"
  [& args]
  (let [results (run (apply context args))]
    (println (nested-printer results))
    (println (summary-printer (summary results)))))
