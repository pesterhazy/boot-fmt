(ns boot-fmt.core
  (:require [zprint.core :as zp]))

(def test-fname "test/files/response.clj")
(def test-out-fname "test/files/response-out.clj")

(defn transform [contents]
  (str (zp/zprint-str contents {:parse-string-all? true
                                :parse {:interpose "\n\n"}
                                :style :community})
       "\n"))

(defn run []
  (->> test-fname
       slurp
       transform
       (spit test-out-fname)))
