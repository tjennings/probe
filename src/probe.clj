(ns probe)
(use 'clojure.contrib.seq-utils)

(defmacro property
  ([doc]
  `(println "Pending - " ~doc))

  ([doc test]
  `(if ~test
     (println "passed! " '~test)
     (println "failed! " '~test)))

  ([doc data test]
   `(let [x 10]
      (if ~test
       (println "passed! " '~test)
       (println "failed! " '~test)))))

(defn run-one [raw-arglists assertion original-expression]
  (map (fn [raw-arglist]
       (let [arglist (if (sequential? raw-arglist) raw-arglist [raw-arglist])]
         (if (apply assertion arglist)
           [:success arglist]
           [:failure arglist])
       ))
       raw-arglists)
)

(defn run-all [raw-arglists assertion original-expression]
  (let [result (run-one raw-arglists assertion original-expression)]
    {:original-expression original-expression :result result})
)

(defstruct context-info
           :doc
           :cases)

(defstruct case-info
           :orig-expr
           :test
           :args
           :data)

(defmacro for-all [generator bindings expr]
 `(run-all
    (~generator) (fn [~@bindings] ~expr) '~expr)
)

(defn context [doc & rest]
  {:doc doc :body rest})

(def red     "\033[31m")
(def green   "\033[32m")
(def brown   "\033[33m")
(def default "\033[0m")
(def white "\033[0m")

(defn my-pretty-print [stuff prefix]
  (println stuff)
  (cond 
    (map? stuff)
      (do (println ansi-default prefix (:doc stuff))
          (my-pretty-print (:body stuff) (str prefix "  ")))
    (sequential? stuff)
      (if (map? (first stuff))
        (do 
          (doseq [ele stuff] 
            (my-pretty-print ele (str prefix "  "))) stuff)
        (do 
          (doseq [for-all-result stuff]
            (println red prefix for-all-result)))
          ))
   (print white))

(defn testing [rest]
  (my-pretty-print rest "")
  "That's all, folks")
