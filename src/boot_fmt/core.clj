(ns boot-fmt.core
  (:require [zprint.core :as zp]
            [boot.core :as bc]
            [clojure.test :refer [deftest is]]))

(defn transform [contents]
  (str (zp/zprint-str contents {:parse-string-all? true
                                :parse {:interpose "\n\n"}
                                :style :community})
       "\n"))

(defn print-diff [old nu]
  (let [dir (bc/tmp-dir!)
        old-f (java.io.File. dir "old")
        nu-f (java.io.File. dir "new")]
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
    (print-diff content output)))

(defn process-many [fnames]
  (doseq [fname fnames]
    (process fname)))
