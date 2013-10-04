(ns bluehood.test.models.user
  (:use clojure.test)
  (:require [noir.validation :as v]
            [bluehood.models.user :as user]))

(defn build-user []
  {:name "John Doe" :email "john.doe@example.com" :password "topsecret" :password-confirmation "topsecret"})

(deftest validation
  (binding [v/*errors* (atom {})]

    (testing "when user is valid"
      (let [user (build-user)]
        (is (true? (user/valid? user)))))

    (testing "when name is missing"
      (let [user (assoc (build-user) :name nil)]
        (is (false? (user/valid? user)))))
    
    (testing "when email is missing"
      (let [user (assoc (build-user) :email nil)]
        (is (false? (user/valid? user)))))
    
    (testing "when passwords don't match"
      (let [user (assoc (build-user) :password "p1234" :password-confirmation "p5555")]
        (is (false? (user/valid? user)))))))
