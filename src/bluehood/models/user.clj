(ns bluehood.models.user
  (:use [bluehood.models.db])
  (:require [korma.core :as k]))

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
