(ns boot-fmt.core
  (:require [zprint.core :as zp]
            [boot.core :as bc]
            [boot.util :as bu]))

(defn transform [contents]
  (str (zp/zprint-str contents {:parse-string-all? true
                                :parse {:interpose "\n\n"}
                                :style :community})
       "\n"))

(defn mangle [fname nam]
  (let [basename (-> (clojure.string/split fname #"/") last)
        mangled (clojure.string/replace-first basename #"(\.[^.]*$)" (str "." nam "$1"))]
    (if (= mangled basename)
      (str basename "." nam)
      mangled)))

(defn print-diff [file old nu]
  (let [dir (bc/tmp-dir!)
        old-f (java.io.File. dir (mangle (.getName file) "old"))
        nu-f (java.io.File. dir (mangle (.getName file) "new"))]
    (spit old-f old)
    (spit nu-f nu)
    (-> (clojure.java.shell/sh "git" "diff" "--no-index"
                               "--color"
                               (.getAbsolutePath old-f) (.getAbsolutePath nu-f))
        :out
        println)))

(defn process [file {:keys [mode]}]
  (let [content (slurp file)
        output (transform content)]
    (cond
      (= :diff mode) (print-diff file content output)
      (= :list mode) (when (not= content output) (println "File changed:" (.getName file))))
    {:file file
     :changed? (not= content output)}))

(defn process-many [opts files]
  (let [changes (mapv (fn [file]
                        (bu/info "Processing %s\n" file)
                        (process file opts))
                      files)]))

(defn clj-file? [f]
  (and (.exists f) (.isFile f) (not (.isHidden f))
       (contains? #{"clj" "cljs" "cljc" "cljx" "boot"}
                  (last (.split (.toLowerCase (.getName f)) "\\.")))))

(bc/deftask fmt
  "Format Clojure source files"
  [m mode MODE kw "Mode"
   f files VAL #{str} "file(s) to format"]
  (let [mode (or mode :list)]
    (assert (seq files) "At least one filename needs to be provided.")
    (assert (#{:list :diff} mode) "Invalid mode")
    (bc/with-pre-wrap fileset
      (let [files* (some->> files
                            (map clojure.java.io/file)
                            (mapcat (fn [f]
                                      (if (.isDirectory f) (file-seq f) [f])))
                            (filter clj-file?))]
        (process-many {:mode mode} files*)
        fileset))))
