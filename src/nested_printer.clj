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
