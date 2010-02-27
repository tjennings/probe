(ns all-tests)
(use 'probe)
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
  ;;TOOD: Obv. this needs to be generalized into some kind of 
  ;;collecting test runner we can easily invoke
  (load "probe_test")
  (load "test_finder_test")
  )

(run-collected-tests (deref collected-tests))
