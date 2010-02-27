(ns probe.nested-runner)
(use 'probe.core)
(use 'probe.nested-printer)
(use 'probe.runner)

(defn testing
  "acts as the outer context of all tests, meant to be hooked and overridden by runners"
  [& args]
  (let [results (run (apply context args))]
    (println (nested-printer results))
    (println (failure-printer results))
    (println (summary-printer (summary results)))))
