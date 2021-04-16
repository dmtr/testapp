(ns testapp.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [clojure.tools.logging :refer [info]]
            [testapp.db :as db]
            [testapp.utils :as utils]
            [testapp.spec :refer [validate-request]])
  (:gen-class))

(defn create-request [request]
  (let [{:keys [body]} request
        errors? (validate-request body)
        id (utils/uuid)
        [status response] (if errors? [400 errors?] [201 {:id id}])]
    (when (nil? errors?)
      (db/add-request {:request/id id
                       :request/title (:title body)
                       :request/desc (:desc body)
                       :request/reporter (:reporter body)
                       :request/assignee (:assignee body)
                       :request/date (:date body)}))
      {:status status :body response}
    ))

(defn list-requests [request]
  (let [limit (get-in request [:params :limit])
        offset (get-in request [:params :offset])
        res (db/get-requests limit offset)]
  {:status 200
   :body res}))

(defroutes app
  (GET "/requests" [] (-> list-requests
                          wrap-keyword-params
                          wrap-params
                          wrap-json-response))
  (POST "/requests" [] (-> create-request
                           (wrap-json-body {:keywords? true})
                            wrap-json-response))
  (route/not-found "Page not found"))

(defn parse-args
  [args]
  (let [port (first args)]
    {:port (utils/parse-int-safe port 8080)}))

(defn -main
  "Start web app"
  [& args]
  (let [params (parse-args args)]
    (info "Init DB") 
    (db/init-db)
    (info "Start web app") 
    (run-jetty app {:port (:port params) :join? true})))
