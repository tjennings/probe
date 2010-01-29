(ns new_probe)
(use 'clojure.contrib.seq-utils)

(defn- is-pending [test]
  (if (or (nil? test) (empty? test))
     false
     (:pending? test)))

(defstruct test-info 
  :doc
  :tests
  :pending?
  :result)

(defstruct assertion-result
  :type :assertion
  :result
  :message)

(defn assert-equality-message [result left right]
  (if result
    ()
    (str "expected \"" left "\" but was \"" right "\"")))

(defn assert-equality [left right]
  (let [result (= left right)]
    (struct-map assertion-result
      :result result
      :message (assert-equality-message result left right))))

(defn run-one [test]
  (let [result (eval (:tests test))]
    (if (contains? result :message) ;hacking this in for now
      (do
        (-> test
        (assoc :passed (:result result))
        (assoc :assertions result)))
      (assoc test :passed result))))

(defn run [context]
  (assoc context :tests
    (map run-one (flatten [(:tests context)]))))

(def red     "\033[31m")
(def green   "\033[32m")
(def green   "\033[32m")
(def brown   "\033[33m")
(def default "\033[0m")

(defn nested-test-printer [context]
  (if (:passed context)
    (str green "  - " (:doc context) default)
    (if-let [assertion (:assertions context)]
      (str red "  - " (:doc context) " " (:message assertion) default)
      (str red "  - " (:doc context) " " (:tests context) default))))

(defn context-color [context]
  (if (:pending? context)
    brown
    default))

(defn nested-context-printer [context]
  (str (context-color context) (:doc context) default "\n"
    (apply str (map nested-printer (:tests context)))))

(defn nested-printer [context]
    (if (= :context (:type context))
      (nested-context-printer context)
      (if (= :test (:type context))
        (str (nested-test-printer context) "\n"))))

(defn context
  ([] {:type :context :doc "" :pending? true})
  ([doc] {:type :context :doc doc :pending? true})
  ([doc & children] {:type :context :doc doc :pending? (is-pending children) :tests (flatten [children])}))

(defmacro it 
  ([] (context))
  ([doc] (context doc))
  ([doc test] `{:type :test :doc ~doc :pending? false :tests '~test}))

(defn testing
  ([] (context))
  ([doc] (context doc))
  ([doc & children]
   (println (nested-printer (run (context doc children))))))
