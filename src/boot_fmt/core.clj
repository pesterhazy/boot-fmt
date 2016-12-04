(ns boot-fmt.core
  "Reformat Clojure(script) source files"
  {:boot/export-tasks true}
  (:require [boot-fmt.impl :as impl] [boot.core :as bc] [boot.util :as bu]))

(bc/deftask
  fmt
  "Reformat Clojure(script) source files, like gofmt"
  [m mode MODE kw "Mode" r really bool "Really act?" f files VAL #{str}
   "files or directories to format"]
  (let [mode (or mode :print)]
    (assert (seq files) "At least one filename needs to be provided.")
    (assert (#{:print :list :diff :overwrite} mode) "Invalid mode")
    (assert (or (not= :overwrite mode) really)
            "In overwrite mode, add the --really flag")
    (bc/with-pre-wrap
      fileset
      (let [files* (some->> files
                            (map clojure.java.io/file)
                            (mapcat (fn [f]
                                      (if (.isDirectory f) (file-seq f) [f])))
                            (filter impl/clj-file?))]
        (impl/process-many {:mode mode} files*)
        fileset))))
