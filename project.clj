;; The only requirement of the project.clj file is that it includes a
;; defproject form. It can have other code in it as well, including
;; loading other task definitions.

(defproject probe "0.0.1-SNAPSHOT"
  :description "A BDD inspired testing library"
  :url "http://github.com/tjennings/probe"
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
                 [jline "0.9.94"]]
  :dev-dependencies [[lein-clojars "0.5.0-SNAPSHOT"]] )
  
