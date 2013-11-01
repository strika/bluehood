(ns bluehood.features.profile
  (:use kerodon.core
        kerodon.test
        clojure.test)
  (:require [bluehood.handler :refer [app]]
            [bluehood.models.schema :refer [db-spec]]
            [bluehood.features.pages.sign-up-page :as sign-up-page]
            [bluehood.features.pages.profile-page :as profile-page]
            [bluehood.features.pages.sign-in-page :as sign-in-page]))

(use-fixtures :each
  (fn [f]
    (clojure.java.jdbc/with-connection db-spec
      (clojure.java.jdbc/transaction
        (clojure.java.jdbc/set-rollback-only)
        (f)))))

(deftest user-can-see-name-on-the-profile
  (let [user {:name "john" :email "john@example.com" :password "topsecret"}]
    (-> (session app)
        (sign-up-page/sign-up user)
        (follow-redirect)
        (within [:#welcome]
          (has (text? (str "Signed in as " (:name user)))))
        (within [:h2]
          (has (text? (str "User details for " (:name user))))))))
