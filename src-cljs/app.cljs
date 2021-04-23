(ns testapp.app
  (:require [reagent.dom :as rdom]
            [re-frame.core :refer [subscribe dispatch-sync]]
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

(defn request-app []
 [:div 
  [requests-list]])

(defn ^:export run []
  (dispatch-sync [:get-requests])
  (rdom/render [request-app] (js/document.getElementById "app")))
