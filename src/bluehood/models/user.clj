(ns bluehood.models.user
  (:require [korma.core :as k]
            [noir.validation :as v]))

(k/defentity users)

(defn build [name email password password-confirmation]
  {:name name :email email :password password :password-confirmation password-confirmation})

(defn create [user]
  (k/insert users (k/values user)))

(defn find-by-id [id]
  (first
    (k/select users
      (k/where {:id id})
      (k/limit 1))))

(defn find-by-email [email]
  (first (k/select users
           (k/where {:email email})
           (k/limit 1))))

(defn update [])

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
  (empty? (:errors (validate user))))
