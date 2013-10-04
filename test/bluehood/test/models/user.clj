(ns bluehood.test.models.user
  (:use clojure.test)
  (:require [bluehood.models.user :as user]))

(deftest validation
  (testing "when user is valid"
    (let [user {:name "John Doe" :email "john.doe@example.com" :password "topsecret" :password-confirmation "topsecret"}]
      (is (true? (user/valid? user))))))
