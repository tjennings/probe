(ns probe.nested-printer)
(use 'clojure.contrib.seq-utils)
(use 'probe.core)

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
  ([context] (summary context default-summary)) ([context sums] (reduce combine-sums (map summary (:tests context))))) 

(defmethod summary :expects [it]
  (assoc default-summary 
    :time 0 ;; TODO - implement this
    :success (if (:passed it) 1 0)
    :failed (if (not (:passed it)) 1 0)
    :errors (if (:errors it) 1 0)
  ))

(defn summary-printer [sum]
  (str (:success sum) " Success " 
       (:failed sum) " Failed " 
       (:errors sum) " Errors "))

;; how to print a back trace:
;; (reduce (fn [a b] (str a "\n" b)) (.getStackTrace error))
;;
(defn format-errors [error]
  (if error
    (str "\n    " (.getMessage error))))

(defmulti failure-printer :type)

(defmethod failure-printer :context
  ([context] (failure-printer context ""))
  ([context doc]
  (apply str (doall (map #(failure-printer % (str doc " " (:doc context))) (:tests context))))))

(defmethod failure-printer :expects
  ([it] (failure-printer it ""))
  ([it doc]
    (if (not (:passed it))
      (str red doc " " (:doc it) default
           (format-errors (:error it))
           "\n\n"))))

(defmulti nested-printer :type)

(defmethod nested-printer :expects
  ([context] (nested-printer context ""))
  ([context depth]
    (if (is-pending context)
      (str brown depth "- " (:doc context) default "\n")
      (if (:passed context)
        (str green depth "- " (:doc context) default "\n")
        (str red depth "- " (:doc context) " " default "\n")))))

(defmethod nested-printer :context 
  ([context] (nested-printer context ""))
  ([context depth]
    (str (context-color context) depth (:doc context) default "\n"
      (apply str (doall (map #(nested-printer % (str "  " depth)) (:tests context)))))))
