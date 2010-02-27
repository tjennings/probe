(ns probe.util)

(defn any?
  "TODO: This has to be in lib somewhere?
   Returns true if any element in the collection returns true for the predicate
   function"
  [pred coll]
  (not (empty? (filter #(pred %) coll))))
  
; Blatenly stolen from Stu H's circumspec
(defn migrate
  "Like immigrate, but takes a map of ns -> varnames."
  [ns-var-map]
  (doseq [[ns syms] ns-var-map]
    (require ns)
    (doseq [sym syms]
      (let [var (ns-resolve ns sym)]
        (let [sym (with-meta sym (assoc (meta var) :ns *ns*))]
          (if (.hasRoot var)
            (intern *ns* sym (.getRoot var))
            (intern *ns* sym)))))))
