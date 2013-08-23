(ns bluehood.routes.auth
  (:use compojure.core)
  (:require [bluehood.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [bluehood.models.db :as db]))

(defn valid? [username password password-confirmation]
  (vali/rule (vali/has-value? username)
             [username "Username is required"])
  (vali/rule (vali/min-length? password 5)
             [:password "Password must be at least 5 characters"])
  (vali/rule (= password password-confirmation)
             [:password-confirmation "Entered passwords do not match"])
  (not (vali/errors? :username :password :password-confirmation)))

(defn register [& [username]]
  (layout/render
    "registration.html"
    {:id username
     :username-error (vali/on-error :username first)
     :password-error (vali/on-error :password first)
     :password-confirmation-error (vali/on-error :password-confirmation first)}))

(defn handle-registration [username password password-confirmation]
  (if (valid? username password password-confirmation)
    (try
      (do
        (db/create-user {:id username :pass (crypt/encrypt password)})
        (session/put! :username username)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:username (.getMessage ex)])
        (register)))
    (register username)))

(defn profile []
  (layout/render
    "profile.html"
    {:user (db/get-user (session/get :username))}))

(defn update-profile [{:keys [first-name last-name email]}]
  (db/update-user (session/get :username) first-name last-name email)
  (profile))

(defn handle-login [username password]
  (let [user (db/get-user username)]
    (if (and user (crypt/compare password (:pass user)))
      (session/put! :username username))
    (resp/redirect "/")))

(defn logout []
  (session/clear!)
  (resp/redirect "/"))

(defroutes auth-routes
  (GET "/register" []
       (register))

  (POST "/register" [username password password-confirmation]
        (handle-registration username password password-confirmation))

  (GET "/profile" [] (profile))
  
  (POST "/update-profile" {params :params} (update-profile params))
  
  (POST "/login" [username password]
        (handle-login username password))

  (GET "/logout" []
        (logout)))
