(ns nested-printer)
(use 'clojure.contrib.seq-utils)
(use 'probe-core)

(def red     "\033[31m")
(def green   "\033[32m")
(def brown   "\033[33m")
(def default "\033[0m")

(defn context-color [context]
  (if (is-pending context)
    brown
    default))

(def default-summary {:time 0 :success 0 :failed 0 :errors 0})

(defn combine-sums
  ([] default-summary)
  ([a, b]
    {:time (+ (:time a) (:time b))
     :success (+ (:success a) (:success b))
     :failed (+ (:failed a) (:failed b))
     :errors (+ (:errors a) (:errors b))}))

(defmulti summary :type)

(defmethod summary :context
  ([context] (summary context default-summary))
  ([context sums] (reduce combine-sums (map summary (:tests context)))))

(defmethod summary :expects [it]
  (assoc default-summary 
    :time 0 ;; TODO - implement this
    :success (if (:passed it) 1 0)
    :failed (if (not (:passed it)) 1 0)
    :errors (if (:errors it) 1 0)
  ))

(defn summary-printer [sum]
  (str "Success " (:success sum)
       " Failed " (:failed sum)
       " Errors " (:errors sum)))

(defmulti nested-printer :type)

(defmethod nested-printer :expects
  ([context] (nested-printer context ""))
  ([context depth]
    (if (is-pending context)
      (str brown depth "- " (:doc context) default "\n")
      (if (:passed context)
        (str green depth "- " (:doc context) default "\n")
        (str red depth "- " (:doc context) " " default "\n")))))

(defmethod nested-printer :test
  ([context] (nested-printer context ""))
  ([context depth]
    (if (is-pending context)
      (str brown depth "- " (:doc context) default "\n")
      (if (:passed context)
        (str green depth "- " (:doc context) default "\n")
        (if-let [assertion (:assertions context)]
          (str red depth "- " (:doc context) " " (:message assertion) default "\n")
          (str red depth "- " (:doc context) " " default "\n"))))))

(defmethod nested-printer :context 
  ([context] (nested-printer context ""))
  ([context depth]
    (str (context-color context) depth (:doc context) default "\n"
      (apply str (doall (map #(nested-printer % (str "  " depth)) (:tests context)))))))
