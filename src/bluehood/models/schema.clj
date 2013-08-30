(ns bluehood.models.schema
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/bluehood"
   :user "strika"
   :password "strika"})
