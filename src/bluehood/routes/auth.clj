(ns bluehood.routes.auth
  (:use [compojure.core]
        [bluehood.validations.user :only [validate valid?]])
  (:require [bluehood.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [bluehood.models.user :as user]
            [bluehood.services.user-registration :as user-registration]))

(defn get-register [& [user]]
  (layout/render "registration.html" {:user user}))

(defn redirect-user [user]
  (if (valid? user)
    (resp/redirect "/")
    (get-register user)))

(defn post-register [name email password password-confirmation]
  (-> (user-registration/execute name email password password-confirmation)
      (redirect-user)))

(defn profile []
  (layout/render
    "profile.html"
    {:user (user/find-by-id (session/get :id))}))

(defn update-profile [{:keys [first-name last-name email]}]
  (user/update (session/get :id) first-name last-name email)
  (profile))

(defn show-login []
  (layout/render
    "login.html"))

(defn handle-login [email password]
  (let [user (user/find-by-email email)]
    (if (and user (crypt/compare password (:password user)))
      (user-registration/set-user-session user))
    (resp/redirect "/")))

(defn logout []
  (session/clear!)
  (resp/redirect "/"))

(defroutes auth-routes
  (GET "/register" []
       (get-register))

  (POST "/register" [name email password password-confirmation]
        (post-register name email password password-confirmation))

  (GET "/profile" [] (profile))

  (POST "/update-profile" {params :params} (update-profile params))

  (GET "/login" [] (show-login))

  (POST "/login" [email password]
        (handle-login email password))

  (GET "/logout" []
       (logout)))
