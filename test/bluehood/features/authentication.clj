(ns bluehood.features.authentication
  (:use kerodon.core
        kerodon.test
        clojure.test)
  (:require [bluehood.handler :refer [app]]
            [bluehood.models.schema :refer [db-spec]]
            [bluehood.features.pages.sign-up-page :as sign-up-page]
            [bluehood.features.pages.sign-in-page :as sign-in-page]))

(use-fixtures :each
  (fn [f]
    (clojure.java.jdbc/with-connection db-spec
      (clojure.java.jdbc/transaction
        (clojure.java.jdbc/set-rollback-only)
        (f)))))

(deftest user-can-see-sign-in-form
  (-> (session app)
      (visit "/")
      (follow "Sign In")
      (has (value? [:#email] ""))
      (has (value? [:#password] ""))))

(deftest user-sign-in
  (let [user {:name "john" :email "john@example.com" :password "topsecret"}]
    (-> (session app)
      (sign-up-page/sign-up user)
      (sign-in-page/sign-out)
      (sign-in-page/sign-in user)
      (sign-in-page/user-should-be-signed-in user))))

(deftest user-sign-up
  (let [user {:name "john" :email "john@example.com" :password "topsecret"}]
    (-> (session app)
      (sign-up-page/sign-up user)
      (sign-in-page/user-should-be-signed-in user))))
