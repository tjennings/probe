(defmacro claim-equal [x y]
  `(if (= ~x ~y)
       (print ".")
     (do (println)
	 (println "FAIL: " '~y " is " ~y ", not " ~x))))

;(defn flatten [list]
;  (cond (empty? list) 
;          list
;        (sequential? (first list))
;	  (concat (flatten (first list)) (flatten (rest list)))
;        true 
;	  (cons (first list) (flatten (rest list))))
;)


(defn looking-at-context? [list]
  (string? (first list)))

(defn deeper [prefix]
  (str "  " prefix))

(defn failure? [result]
  (= (first result) false))

(defn one-failure-result-description [failure prefix]
  (str prefix (rest failure)))

(def one-context-description) ; forward
(defn one-forall-result-description [[expression & binding-results] prefix]
   (let [failures (filter failure? binding-results)]
	(if (empty? failures) 
	    (str prefix expression " OK")
         (cons (str prefix expression " fails for:")
	       (map #(one-failure-result-description % (deeper prefix))
		    failures))))
)


(defn one-context-body-element [context-or-result prefix]
  (let [prefix (deeper prefix)]
       (if (looking-at-context? context-or-result)
	   (one-context-description context-or-result prefix)
	   (one-forall-result-description context-or-result prefix)))
)


(defn one-context-description [list prefix]
   (cons (str prefix (first list))
	 (map #(one-context-body-element % prefix) (rest list)))
)

(defn top-level-context-description [context]
  (one-context-description context "")
)

(defn result-strings [context-list]
  (flatten (map top-level-context-description context-list))
)

;;; This produces no output. (Laziness? - if so I don't know how to force
;;; evaluation) 
(defn print-results [context-list]
  (map println (result-strings context-list))
  nil)

; But a recursive version works. The helper method is because I don't know how to 
; do a let-binding for a recursive function. (The recursive call doesn't find the 
; function itself in scope.

(defn print-results-1 [string-list]
  (if (not (empty? string-list))
      (do 
       (println (first string-list))
       (print-results-1 (rest string-list))))
)

(defn print-results [context-list]
  (print-results-1 (result-strings context-list)))


(def *empty-test-results* '())
(def *one-empty-context* '(  ("outermost context") ))
(def *sequential-empty-contexts*
     '(  ("outermost context") ("next outermost context") ))
(def *nested-empty-contexts*
     '(  ("outermost context"
	  ("inner context"))
	 ("next outermost context")))

(def *context-with-one-failure* 
     '( ("outermost context"
	 ( (is a b) (true) (false 1 2)) ) ))

(def *context-with-no-failures*
    '( ("outermost context"
	( (is a b) (true) (true) (true)))))

(def *context-with-typical-results* 
     '( ("outermost context"
	  ( (= x x) (true))
	  ( (= y x) (false "hi" "bear") (false [1] {1 2}))
          ("inner context" 
	   ( (= (+ x x) (* x 2)) (true) (false 2))
	   ( (something x y)     (false 1 3) (true) (true) (true))))))

(defn run-tests []
  (claim-equal '() (result-strings *empty-test-results*))
  (claim-equal '("outermost context")
	       (result-strings *one-empty-context*))
  (claim-equal '("outermost context" "next outermost context")
	       (result-strings *sequential-empty-contexts*))
  (claim-equal '("outermost context" "  inner context" "next outermost context")
	       (result-strings *nested-empty-contexts*))


  (claim-equal true (looking-at-context? '("context" 1 2 3)))
  (claim-equal false (looking-at-context? '((= (+ x y)))))


  (claim-equal '("outermost context" 
		 "  (is a b) fails for:"
		 "    (1 2)")
	       (result-strings *context-with-one-failure*))

  (claim-equal '("outermost context" 
		 "  (is a b) fails for:"
		 "    (1 2)")
	       (result-strings *context-with-one-failure*))

  (claim-equal '("outermost context" 
		 "  (is a b) OK")
	       (result-strings *context-with-no-failures*))
  (claim-equal '("outermost context" 
                 "  (= x x) OK"
                 "  (= y x) fails for:"
                 "    (\"hi\" \"bear\")"
		 "    ([1] {1 2})"
                 "  inner context"
                 "    (= (+ x x) (* x 2)) fails for:"
                 "      (2)"
                 "    (something x y) fails for:"
                 "      (1 3)")
	       (result-strings *context-with-typical-results*))
  (println)

)
  
