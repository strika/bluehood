(ns bluehood.features.pages.sign-up-page
  (:use kerodon.core))

(defn sign-up [session user]
  (-> session
    (visit "/")
    (follow "Sign Up")
    (fill-in "Name" (:name user))
    (fill-in "Email" (:email user))
    (fill-in "Password" (:password user))
    (fill-in "Password Confirmation" (:password user))
    (press "Sign Up")
    (follow-redirect)))
