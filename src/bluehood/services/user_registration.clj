(ns bluehood.services.user-registration
  (:use [bluehood.validations.user :only [validate valid?]])
  (:require [noir.util.crypt :as crypt]
            [noir.session :as session]
            [bluehood.models.user :as user]))

(defn save-user [{:keys [name email password] :as user}]
  (if (valid? user)
    (user/create {:name name :email email :password (crypt/encrypt password)}))
  user)

(defn set-user-session [user]
  (if (valid? user)
    (do
      (session/put! :id (:id user))
      (session/put! :name (:name user))))
  user)

(defn execute [name email password password-confirmation]
  (-> (user/build name email password password-confirmation)
      (validate)
      (save-user)
      (set-user-session)))
