(ns bluehood.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [bluehood.models.schema :as schema]))

(defdb db schema/db-spec)

(defentity users)

(defn create-user [user]
  (insert users
          (values user)))

(defn update-user [id first-name last-name email]
  (update users
  (set-fields {:first_name first-name
               :last_name last-name
               :email email})
  (where {:id id})))

(defn find-user [id]
  (first (select users
                 (where {:id id})
                 (limit 1))))

(defn find-user-by-email [email]
  (first (select users
                 (where {:email email})
                 (limit 1))))
