(ns testapp.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [testapp.db :as db])
  (:gen-class))

(defn create-request [request]
  (let [{:keys [body]} request]
    (db/add-request {:request/title (:title body) :request/desc (:desc body)})
    {:status 201
     :body body}))

(defn list-requests [request]
  (let [res (db/get-requests)]
  {:status 200
   :body (vector res)}))

(defroutes app
  (GET "/requests" [] (-> list-requests
                          wrap-json-response))
  (POST "/requests" [] (-> create-request
                           (wrap-json-body {:keywords? true})
                            wrap-json-response))
  (route/not-found "Page not found"))

(defn parse-args
  [args]
  (let [port (first args)]
    {:port (if (nil? port) 8080 (Integer/parseInt port))}))

(defn -main
  "Start web app"
  [& args]
  (let [params (parse-args args)]
    (db/init-db)
    (run-jetty app {:port (:port params) :join? true})))
