(ns boot-fmt.core
  (:require [zprint.core :as zp]
            [boot.core :as bc]
            [boot.util :as bu]
            [clojure.test :refer [deftest is]]))

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

(defn print-diff [fname old nu]
  (let [dir (bc/tmp-dir!)
        old-f (java.io.File. dir (mangle fname "old"))
        nu-f (java.io.File. dir (mangle fname "new"))]
    (spit old-f old)
    (spit nu-f nu)
    (-> (clojure.java.shell/sh "git" "diff" "--no-index"
                               "--color"
                               (.getAbsolutePath old-f) (.getAbsolutePath nu-f))
        :out
        println)))

(defn process [fname]
  (let [content (slurp fname)
        output (transform content)]
    (print-diff fname content output)))

(defn process-many [fnames]
  (doseq [fname fnames]
    (bu/info "Working on %s\n" fname)
    (process fname)))
