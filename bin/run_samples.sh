#!/bin/sh
CLASSPATH=classes:src:samples

for f in lib/*.jar; do
    CLASSPATH=$CLASSPATH:$f
done

java -cp $CLASSPATH jline.ConsoleRunner clojure.main -e '(load "sample_suite")'
