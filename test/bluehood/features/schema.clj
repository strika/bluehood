(ns bluehood.features.schema
  (:use clojure.test
        [korma.db :only (defdb)])
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/bluehood-test"
   :user "strika"
   :password "strika"})

(defdb db db-spec)

(use-fixtures :each
              (fn [f]
                (clojure.java.jdbc/transaction
                  (clojure.java.jdbc/set-rollback-only)
                  (f))))
