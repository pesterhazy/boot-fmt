(ns boot-fmt.core
  (:require [boot-fmt.impl :as impl]
            [boot.core :as bc]
            [boot.util :as bu]))

(bc/deftask fmt
  "Reformat Clojure(script) source files, like gofmt"
  [m mode MODE kw "Mode"
   r really bool "Really act?"
   f files VAL #{str} "file(s) to format"]
  (let [mode (or mode :list)]
    (assert (seq files) "At least one filename needs to be provided.")
    (assert (#{:list :diff :overwrite} mode) "Invalid mode")
    (assert (or (not= :overwrite mode)
                really)
            "In overwrite mode, add the --really flag")
    (bc/with-pre-wrap fileset
      (let [files* (some->> files
                            (map clojure.java.io/file)
                            (mapcat (fn [f]
                                      (if (.isDirectory f) (file-seq f) [f])))
                            (filter impl/clj-file?))]
        (impl/process-many {:mode mode} files*)
        fileset))))
