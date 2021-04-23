(ns testapp.app
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [day8.re-frame.http-fx]
            [cljs.pprint :refer [pprint]]
            [testapp.events]))

(defn request-item [request]
  [:li
    [:div 
     [:h1 (:title request)]
     [:p (:desc request)]
     [:p (:assignee request)]
     [:p (:reporter request)]
     [:p (:date request)]]])

(defn requests-list []
  (let [requests @(subscribe [:requests])]
    [:ul#requests-list
      (for [request requests]
       ^{:key (:id request)} [request-item request])]))

(defn request-form []
  (let [default {:title "" :desc "" :reporter "" :assignee "" :date ""}
        request (reagent/atom default)
        post-request (fn [event request]
                       (.preventDefault event)
                       (dispatch [:post-request @request])
                       )]
    [:div 
      [:h1 "New request"] 
        [:form
          [:fieldset
            [:input {:type          "text"
                     :placeholder   "Title"
                     :default-value ""
                     :on-change     #(swap! request assoc :title (-> % .-target .-value))}]]
          [:fieldset
            [:textarea {:rows          "4"
                        :placeholder   "Description"
                        :default-value ""
                        :on-change     #(swap! request assoc :desc (-> % .-target .-value))}]]
          [:fieldset
            [:input {:type          "text"
                     :placeholder   "Reporter"
                     :default-value ""
                     :on-change     #(swap! request assoc :reporter (-> % .-target .-value))}]]
          [:fieldset
            [:input {:type          "text"
                     :placeholder   "Assignee"
                     :default-value ""
                     :on-change     #(swap! request assoc :assignee (-> % .-target .-value))}]]
          [:fieldset
            [:input {:type          "text"
                     :placeholder   "Date"
                     :default-value ""
                     :on-change     #(swap! request assoc :date (-> % .-target .-value))}]]
          [:button#form-button {:on-click #(post-request % request)} "Create request"]
          ]]))


(defn request-app []
 [:div 
  [request-form]
  [requests-list]])

(defn ^:export run []
  (dispatch-sync [:get-requests])
  (rdom/render [request-app] (js/document.getElementById "app")))
