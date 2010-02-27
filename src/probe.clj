(ns probe)
(use 'clojure.contrib.seq-utils)
(use 'probe.util)
 
(migrate
 {'probe.core '[context it pit run is-pending],
  'probe.expect '[expect equal throw-error not!],
  'probe.nested-runner '[testing],
  'probe.runner '[run]})

