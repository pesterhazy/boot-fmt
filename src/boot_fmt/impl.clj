(ns boot-fmt.impl
  (:require [zprint.core :as zp] [boot.core :as bc] [boot.util :as bu]))

(defn transform
  [contents]
  (str (zp/zprint-str contents
                      {:parse-string-all? true, :parse {:interpose "\n\n"}})
       "\n"))

(defn mangle
  [fname nam]
  (let [basename (-> (clojure.string/split fname #"/")
                     last)
        mangled (clojure.string/replace-first basename
                                              #"(\.[^.]*$)"
                                              (str "." nam "$1"))]
    (if (= mangled basename) (str basename "." nam) mangled)))

(defmulti act (fn [opts params] (:mode opts)))

(defmethod act :print
  [opts {:keys [file old-content new-content]}]
  (println new-content))

(defmethod act :diff
  [opts {:keys [file old-content new-content]}]
  (let [dir (bc/tmp-dir!)
        old-f (java.io.File. dir (mangle (.getName file) "old"))
        new-f (java.io.File. dir (mangle (.getName file) "new"))]
    (spit old-f old-content)
    (spit new-f new-content)
    (-> (clojure.java.shell/sh "git"
                               "diff"
                               "--no-index"
                               "--color"
                               (.getAbsolutePath old-f)
                               (.getAbsolutePath new-f))
        :out
        println)))

(defmethod act :list
  [opts {:keys [old-content new-content file]}]
  (when (not= old-content new-content)
    (println "File changed:" (.getName file))))

(defmethod act :overwrite
  [opts {:keys [old-content new-content file]}]
  (when (not= old-content new-content)
    (println "Overwriting file:" (.getName file))
    (spit file new-content)))

(defn process
  [file {:keys [mode], :as info}]
  (let [old-content (slurp file)
        new-content (transform old-content)]
    (act info {:file file, :old-content old-content, :new-content new-content})
    {:file file, :changed? (not= old-content new-content)}))

(defn process-many
  [opts files]
  (when-not (seq files) (throw (RuntimeException. "No files found")))
  (let [changes (mapv (fn [file] (bu/dbug "Processing %s\n" file) (process file opts))
                      files)]
    #_(if-not (->> changes
                   (filter :changed?)
                   seq)
        (println "No changes."))))

(defn clj-file?
  [f]
  (and (.exists f)
       (.isFile f)
       (not (.isHidden f))
       (contains? #{"clj" "cljs" "cljc" "cljx" "boot"}
                  (last (.split (.toLowerCase (.getName f)) "\\.")))))
