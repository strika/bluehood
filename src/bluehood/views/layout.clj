(ns bluehood.views.layout
  (:use noir.request)
  (:require [selmer.parser :as parser]
            [noir.session :as session]))

(def template-path "bluehood/views/templates/")

(defn render [template & [params]]
  (parser/render-file (str template-path template)
                      (assoc params
                             :servlet-context (:context *request*)
                             :username (session/get :username))))
