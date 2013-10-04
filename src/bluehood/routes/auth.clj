(ns bluehood.routes.auth
  (:use compojure.core)
  (:require [bluehood.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [bluehood.models.db :as db]))

(defn valid? [name email password password-confirmation]
  (vali/rule (vali/has-value? name)
             [name "Name is required"])
  (vali/rule (vali/has-value? email)
             [email "Email is required"])
  (vali/rule (vali/min-length? password 5)
             [:password "Password must be at least 5 characters"])
  (vali/rule (= password password-confirmation)
             [:password-confirmation "Entered passwords do not match"])
  (not (vali/errors? :name :email :password :password-confirmation)))

(defn register [& [name email]]
  (layout/render
    "registration.html"
    {:name name
     :email email
     :name-error (vali/on-error :name first)
     :email-error (vali/on-error :email first)
     :password-error (vali/on-error :password first)
     :password-confirmation-error (vali/on-error :password-confirmation first)}))

(defn handle-registration [name email password password-confirmation]
  (if (valid? name email password password-confirmation)
    (try
      (do
        (let [user (db/create-user {:name name :email email :password (crypt/encrypt password)})]
          (session/put! :id (:id user))
          (session/put! :name (:name user))
          (resp/redirect "/")))
      (catch Exception ex
        (vali/rule false [:id (.getMessage ex)])
        (register)))
    (register name email)))

(defn profile []
  (layout/render
    "profile.html"
    {:user (db/get-user (session/get :id))}))

(defn update-profile [{:keys [first-name last-name email]}]
  (db/update-user (session/get :username) first-name last-name email)
  (profile))

(defn show-login []
  (layout/render
    "login.html"))

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

  (POST "/register" [name email password password-confirmation]
        (handle-registration name email password password-confirmation))

  (GET "/profile" [] (profile))

  (POST "/update-profile" {params :params} (update-profile params))

  (GET "/login" [] (show-login))

  (POST "/login" [username password]
        (handle-login username password))

  (GET "/logout" []
       (logout)))
