#!/bin/sh
CLASSPATH=classes:src:test

for f in lib/*.jar; do
    CLASSPATH=$CLASSPATH:$f
done

#java -cp $CLASSPATH jline.ConsoleRunner clojure.main -e "(use 'test) (run-tests!)"
java -cp $CLASSPATH jline.ConsoleRunner clojure.main -e '(load "new_probe_test")'
