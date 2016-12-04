(ns boot-fmt.core)

(def test-fname "test/files/response.clj")
(def test-out-fname "test/files/response-out.clj")

(defn transform [s])

(defn run []
  (->> test-fname
       slurp
       transform
       (spit test-out-fname)))
