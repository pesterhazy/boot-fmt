(ns boot-fmt.core-test
  (:require [boot-fmt.core :refer :all]
            [clojure.test :refer :all]))

(deftest exercise []
  (process-many ["test/files/response.clj"])
  (is (= 1 1)))
