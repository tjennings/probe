(ns all-tests)
(use 'probe)
(use 'probe.test-finder)
(use 'probe.nested-runner)

(def collected-tests (ref []))

(defn collecting-tests
  "This function will override the 'testing' function from core
   allowing us to hook into test collection across multiple test
   suites"
  [& args]
    (let [context (apply context args)]
      (dosync (alter collected-tests conj context))))

(defn run-collected-tests
  "We'll just supply the default arguments to
   the original testing function
   by creating an artificial context in which all of our
   collected tests will be contained"
  [tests]
  (let [args (cons "All-Tests" tests)]
    (apply testing args)))

(binding [testing collecting-tests]
  (doseq [file (find-tests "./test")]
    (load file)))
    
(run-collected-tests (deref collected-tests))
