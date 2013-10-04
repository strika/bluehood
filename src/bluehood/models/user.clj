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

(defn valid? [user]
  (v/rule (v/has-value? (:name user))
             [:name "Name is required"])
  (v/rule (v/has-value? (:email user))
             [:email "Email is required"])
  (v/rule (= (:password user) (:password-confirmation user))
             [:password-confirmation "Entered passwords do not match"])
  (not (v/errors? :name :email :password :password-confirmation)))
