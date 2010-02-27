(ns probe.test-finder)
(use 'clojure.contrib.seq-utils)
(import '(java.io File))

(defn file-with-path [f]
  (str (.getPath f)))

(defn find-files [dir]
  (let [recur-fn (fn [f] (if (.isDirectory f) 
                                (find-files (file-with-path f))
                                (file-with-path f)))]
    (flatten (map recur-fn (.listFiles (File. dir))))))
  
(defn find-tests [dir]
  (filter (fn [file] (re-matches #".*_test\.clj$" file)) (find-files dir)))
