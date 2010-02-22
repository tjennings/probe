(ns probe.AssertionFailed
  (:gen-class
   :extends RuntimeException
   :init init
   :state details
   :constructors {[String Object] [String]}))

(defn -init [msg details]
  [[msg] details])
