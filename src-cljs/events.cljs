(ns testapp.events
  (:require [re-frame.core :refer [reg-sub reg-event-fx reg-event-db]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [json-request-format json-response-format]]))

(defn index-by [key coll]
  (into {} (map (juxt key identity) coll)))

(def default-db {:requests (hash-map)})

(reg-sub
  :requests
  (fn [db _]
    (vals (:requests db))))

(reg-event-fx
  :get-requests
  (fn [{:keys [db]} [_ params]]
    {:http-xhrio {:method          :get
                  :uri             "/api/requests" 
                  :params          params
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-requests-success]
                  :on-failure      [:api-request-error :get-requests]}
     :db          (-> db
                    (assoc-in [:loading :requests] true)
                    (assoc-in [:filter :offset] (:offset params)))}))

(reg-event-db
 :get-requests-success
 (fn [db [_ {requests :results}]]
   (-> db
       (assoc-in [:loading :requests] false)
       (assoc  :requests (index-by :id requests)))))   

(reg-event-db
 :api-request-error
 (fn [db [_ request-type res]]
   (-> db
       (assoc-in [:errors request-type] (:response res))
       (assoc-in [:loading request-type] false))))

(reg-event-fx
 :post-request
 (fn [{:keys [db]} [_ body]]
   {:db         (assoc-in db [:loading :requests] true)
    :http-xhrio {:method          :post
                 :uri             "/api/requests" 
                 :params          body
                 :format          (json-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [:post-request-success]
                 :on-failure      [:api-request-error :request]}}))


(reg-event-fx
 :post-request-success
 (fn [{:keys [db]} [_ _]]
   {:db       (-> db
                  (assoc-in [:loading :requests] false)
                  (update-in [:errors] dissoc :request))
    :dispatch [:get-requests]}))

(reg-sub
 :errors ;; usage: 
 (fn [db _]
   (:errors db)))
