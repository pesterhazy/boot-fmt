(ns repl.user)

(defn read-lines []
  (clojure.string/join "\n"
                       (loop [lines []]
                         (let [line (read-line)]
                           (if (clojure.string/blank? line)
                             lines
                             (recur (conj lines line)))))))
