(ns bluehood.features.authentication
  (:use kerodon.core
        kerodon.test
        clojure.test)
  (:require [bluehood.handler :refer [app]]))

(deftest user-can-see-sign-in-form
  (-> (session app)
      (visit "/")
      (has (value? "id" "user id"))
      (has (value? "pass" "password"))))
