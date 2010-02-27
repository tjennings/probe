(ns probe.util)

(defn any?
  "TODO: This has to be in lib somewhere?
   Returns true if any element in the collection returns true for the predicate
   function"
  [pred coll]
  (not (empty? (filter #(pred %) coll))))
