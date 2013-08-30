(ns bluehood.routes.home
  (:use compojure.core
        [taoensso.timbre :only [info]])
  (:require [bluehood.views.layout :as layout]
            [noir.session :as session]
            [bluehood.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
