(ns bluehood.routes.auth
  (:use compojure.core)
  (:require [bluehood.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [bluehood.models.user :as user]))

(defn register [& [name email]]
  (layout/render
    "registration.html"
    {:name name
     :email email
     :name-error (vali/on-error :name first)
     :email-error (vali/on-error :email first)
     :password-error (vali/on-error :password first)
     :password-confirmation-error (vali/on-error :password-confirmation first)}))

(defn set-user-session [user]
  (session/put! :id (:id user))
  (session/put! :name (:name user)))

(defn handle-registration [name email password password-confirmation]
  (let [user (user/build name email password password-confirmation)]
    (if (user/valid? user)
      (try
        (do
          (let [user (user/create {:name name :email email :password (crypt/encrypt password)})]
            (set-user-session user)
            (resp/redirect "/")))
        (catch Exception ex
          (vali/rule false [:id (.getMessage ex)])
          (register)))
      (register name email))))

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
      (set-user-session user))
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

  (POST "/login" [email password]
        (handle-login email password))

  (GET "/logout" []
       (logout)))
