(ns bluehood.test.models.user
  (:use clojure.test)
  (:require [noir.validation :as v]
            [bluehood.models.user :as user]))

(defn build-user []
  {:name "John Doe"
   :email "john.doe@example.com"
   :password "topsecret"
   :password-confirmation "topsecret"})

(deftest validation
  (testing "when user is valid"
    (let [user (build-user)]
      (is (empty? (:errors (user/validate user))))
      (is (true? (user/valid? user)))))

  (testing "when name is missing"
    (let [user (assoc (build-user) :name nil)]
      (is (= (:name (:errors (user/validate user))) ["Name is required"]))
      (is (false? (user/valid? user)))))
  
  (testing "when email is missing"
    (let [user (assoc (build-user) :email nil)]
      (is (= (:email (:errors (user/validate user))) ["Email is required"]))
      (is (false? (user/valid? user)))))
  
  (testing "when passwords don't match"
    (let [user (assoc (build-user) :password "p1234" :password-confirmation "p5555")]
      (is (= (:password-confirmation (:errors (user/validate user))) ["Entered passwords do not match"]))
      (is (false? (user/valid? user))))))
