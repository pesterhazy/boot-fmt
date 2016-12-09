(ns boot-fmt.core
  "Reformat Clojure(script) source files"
  {:boot/export-tasks true}
  (:require [boot-fmt.impl :as impl] [boot.core :as bc] [boot.util :as bu]))

(bc/deftask
  fmt
  "Reformat Clojure(script) source files, like gofmt

Print reformatted source code to standard output. Parameters specified using
`-f` can be files or directories. Directories are scanned recursively for
Clojure(Script) source files.

Specify the operation using the --mode paramter:

--mode print (default)

  Print reformatted code to standard output

--mode diff

  When reformatted code is different from original, print diff to standard output.

--mode list

  Where reformatted code is different from original, print filename standard output.

--mode overwrite

  Overwrite files with reformatted code. As this is a potentially dangerous
  operation, you need to specify the --really flag in addition to setting
  the --mode parameter"

  [m mode MODE kw "Mode of operation, i.e. print, list, diff or overwrite. Defaults to print"
   r really bool "In overwrite mode, files are overwritten only if the --really flag is set as well"
   f files VAL #{str} "The list of files or directories to format"]
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
