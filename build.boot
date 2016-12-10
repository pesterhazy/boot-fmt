(set-env! :resource-paths #{"src" "dev"}
          :dependencies '[[zprint "0.2.9"] [org.clojure/tools.nrepl "0.2.12"]
                          [org.clojure/tools.namespace "0.2.11"]])

(require '[boot-fmt.core :refer [fmt]] '[boot-fmt.impl :as impl])

(def +version+ "0.1.1")

(task-options! pom
               {:project 'boot-fmt/boot-fmt,
                :version +version+,
                :description "Boot task to auto-format Clojure(Script) code",
                :url "https://github.com/pesterhazy/boot-fmt",
                :scm {:url "https://github.com/pesterhazy/boot-fmt"},
                :license {"Eclipse Public License"
                          "http://www.eclipse.org/legal/epl-v10.html"}})


(set-env! :repositories
          [["clojars"
            (cond-> {:url "https://clojars.org/repo/"}
              (System/getenv "CLOJARS_USER")
              (merge {:username (System/getenv "CLOJARS_USER"),
                      :password (System/getenv "CLOJARS_PASS")}))]])

(task-options! fmt
               {:mode :diff
                :options {:fn-map {":require" :force-nl-body, "ns" :arg1-body},
                          :style :community,
                          :fn-force-nl #{:force-nl :noarg1 :noarg1-body
                                         :force-nl-body :binding}}})

(deftask
 dogfood
 "Reformat this very repository"
 [m mode MODE kw "mode" r really bool "really"]
 (fmt :files #{"src" "build.boot"} :mode (or mode :diff) :really really))

(deftask build [] (comp (pom) (jar) (install)))

(deftask deploy
         []
         (comp (build)
               (push :repo
                     "clojars"
                     :gpg-sign
                     false
                     #_(not (.endsWith +version+ "-SNAPSHOT")))))

(deftask example
         []
         (println "Reading from stdin...\n")
         (let [code (slurp *in*)]
           (impl/example code)))
