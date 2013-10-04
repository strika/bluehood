(ns bluehood.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [bluehood.models.schema :as schema]))

(defdb db schema/db-spec)
