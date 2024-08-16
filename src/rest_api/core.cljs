;; src/rest_api/core.cljs
(ns rest-api.core
  (:require [cljs.nodejs :as nodejs]
            ["express" :as express]
            [rest-api.routes.user-routes :as user-routes]))

(def dotenv (nodejs/require "dotenv"))
(.config dotenv)

(defn health [req res]
  (println "From hello")
  (.send res "Hello, ClojureScript!"))

(defn main []
  (let [app (express)]
    (.use app (.json express))
    (.use app "/api/user" user-routes/router)
    (.get app "/api/health" health)
    (.listen app 5000 #(println "Server is running on port 5000"))))
