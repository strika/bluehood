(ns bluehood.features.pages.sign-up-page
  (:use kerodon.core))

(def sign-up-page "/register")

(defn sign-up [session user]
  (-> session
    (visit sign-up-page)
    (fill-in "Name" (:name user))
    (fill-in "Email" (:email user))
    (fill-in "Password" (:password user))
    (fill-in "Password Confirmation" (:password user))
    (press "Sign Up")))
