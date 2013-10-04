(ns bluehood.models.user
  (:require [korma.core :as k]))

(k/defentity users)

(defn create [user]
  (k/insert users (k/values user)))

(defn find [id]
  (first
    (k/select users
      (k/where {:id id})
      (k/limit 1))))

(defn find-by-email [email]
  (first (k/select users
           (k/where {:email email})
           (k/limit 1))))

(defn update [])
