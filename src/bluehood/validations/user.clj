(ns bluehood.validations.user
  (:require [noir.validation :as v]))

(defn validate-name [user]
  (if (v/has-value? (:name user))
    user
    (update-in user [:errors :name] conj "Name is required")))

(defn validate-email [user]
  (if (v/has-value? (:email user))
    user
    (update-in user [:errors :email] conj "Email is required")))

(defn validate-password-confirmation [user]
  (if (= (:password user) (:password-confirmation user))
    user
    (update-in user [:errors :password-confirmation] conj "Entered passwords do not match")))

(defn validate [user]
  (-> (assoc user :errors {})
      (validate-name)
      (validate-email)
      (validate-password-confirmation)))

(defn valid? [user]
  (empty? (:errors user)))
