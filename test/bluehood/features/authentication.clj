(ns bluehood.features.authentication
  (:use kerodon.core
        kerodon.test
        clojure.test)
  (:require [bluehood.handler :refer [app]]
            [bluehood.models.schema :refer [db-spec]]))

(use-fixtures :each
              (fn [f]
                (clojure.java.jdbc/with-connection db-spec
                  (clojure.java.jdbc/transaction
                    (clojure.java.jdbc/set-rollback-only)
                    (f)))))

(deftest user-can-see-sign-in-form-on-the-homepage
  (-> (session app)
      (visit "/")
      (within [:h1]
        (has (text? "Welcome to bluehood")))
      (has (value? [:#id] ""))
      (has (value? [:#pass] ""))))

(deftest user-sign-up
  (let [user {:username "mike" :password "topsecret"}]
    (-> (session app)
      (visit "/")
      (follow "Sign Up")
      (fill-in "Username" (:username user))
      (fill-in "Password" (:password user))
      (fill-in "Password Confirmation" (:password user))
      (press "Sign Up")
      (follow-redirect)
      (within [:#welcome]
        (has (text? (str "Signed in as " (:username user))))))))
