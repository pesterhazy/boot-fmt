(ns boot-fmt.core
  "Reformat Clojure(script) source files"
  {:boot/export-tasks true}
  (:require [clojure.set]
            [clojure.string]
            [clojure.java.shell]
            [boot-fmt.impl :as impl]
            [boot.core :as bc]
            [boot.util :as bu]))

(defn find-files-git []
  (let [{:keys [exit out err]} (clojure.java.shell/sh "git" "ls-fileszzz" "-z" "*.clj" "*.cljs" "*.cljc" "*.boot")]
    (when (not= exit 0)
      (throw (ex-info "git ls-files failed"
                      {:err err})))
    (-> out
        (clojure.string/split #"\000")
        set)))

(defn find-files-source []
  (clojure.set/union (bc/get-env :source-paths) (bc/get-env :resource-paths)))

;!zprint {:format :skip}

(bc/deftask fmt
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
   f files VAL #{str} "The list of files or directories to format"
   s source bool "Automatically scan for source files in boot source directories"
   g git bool "Automatically scan for source files in current git repository"
   o options OPTS edn "zprint options"]
  (let [mode (or mode :print)
        files (cond-> files
                source (clojure.set/union (find-files-source))
                git (clojure.set/union (find-files-git)))]
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
                            (filter impl/clj-file?)
                            set
                            sort)]
        (impl/process-many {:mode mode
                            :zprint-options options} files*)
        fileset))))
