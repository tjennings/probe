(defmacro hash-messin [h]
  `(println '~h))

(hash-messin {x "z" b "y"})
