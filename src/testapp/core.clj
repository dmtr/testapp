(ns testapp.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :as route])
  (:gen-class))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello World!"})

(defroutes app
  (GET "/hello" request (handler request))
  (route/not-found "Page not found"))

(defn parse-args
  [args]
  (let [port (first args)]
    {:port (if (nil? port) 8080 (Integer/parseInt port))}))

(defn -main
  "Start web app"
  [& args]
  (let [params (parse-args args)]
    (run-jetty app {:port (:port params) :join? true})))
