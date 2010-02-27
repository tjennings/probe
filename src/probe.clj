(ns probe)
(use 'clojure.contrib.seq-utils)
(use 'probe.util)
 
(migrate
 {'probe.core '[context it pit run is-pending],
  'probe.runner '[run]})

