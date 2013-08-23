(ns bluehood.features.authentication
  (:use kerodon.core
        kerodon.test
        clojure.test
        bluehood.features.schema)
  (:require [bluehood.handler :refer [app]]))

(deftest user-can-see-sign-in-form-on-the-homepage
  (-> (session app)
      (visit "/")
      (within [:h1]
        (has (text? "Welcome to bluehood")))
      (has (value? [:#id] ""))
      (has (value? [:#pass] ""))))
