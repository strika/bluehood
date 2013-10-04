(ns bluehood.features.pages.sign-in-page
  (:use kerodon.core
        kerodon.test))

(def sign-in-page "/login")
(def sign-out-page "/logout")

(defn sign-in [session user]
  (-> session
    (visit sign-in-page)
    (fill-in "Email" (:email user))
    (fill-in "Password" (:password user))
    (press "Sign In")
    (follow-redirect)))

(defn sign-out [session]
  (-> session
    (visit sign-out-page)))

(defn user-should-be-signed-in [session user]
  (-> session
    (within [:#welcome]
      (has (text? (str "Signed in as " (:name user)))))))
