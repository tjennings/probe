(ns probe.test-finder)
(use 'clojure.contrib.seq-utils)
(use 'clojure.contrib.str-utils)
(import '(java.io File))

(defn file-with-path [f]
  (str (.getPath f)))

(defn find-files [dir]
  (let [recur-fn (fn [f] (if (.isDirectory f) 
                                (find-files (file-with-path f))
                                (file-with-path f)))]
    (flatten (map recur-fn (.listFiles (File. dir))))))

(defn strip-extension [string]
  (re-gsub #"\.clj$" "" string))
  
(defn strip-prefix [prefix]
  (let [pattern (re-pattern (str "^" prefix "/"))]
    (fn [string] (re-gsub pattern "" string))))

(defn find-tests [dir]
  (->> (find-files dir)
       (filter (fn [file] (re-matches #".*_test\.clj$" file)))
       (map strip-extension)
       (map (strip-prefix dir))))
