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
     [:h3 "Description"]
     [:p  (:desc request)]
     [:p [:b "Assignee: "] (:assignee request)]
     [:p [:b "Reporter: "] (:reporter request)]
     [:p (:date request)]]])

(defn requests-list []
  (let [requests @(subscribe [:requests]) total @(subscribe [:total])]
    [:div 
      [:h1 "Requests total:" " " total]
      [:ul#requests-list
        (for [request requests]
         ^{:key (:id request)} [request-item request])]]))

(defn request-form []
  (let [default {:title "" :desc "" :reporter "reporter1" :assignee "assignee1" :date ""}
        request (reagent/atom default)
        post-request (fn [event request]
                       (.preventDefault event)
                       (dispatch [:post-request @request])
                       )]
    [:div.form-container
      [:h1 "New Request"]
      [:form
        [:fieldset
          [:legend "Request"]

          [:input {:type          "text"
                   :placeholder   "Title"
                   :default-value ""
                   :on-change     #(swap! request assoc :title (-> % .-target .-value))}]

          [:textarea {:rows          "3"
                      :placeholder   "Description"
                      :default-value ""
                      :on-change     #(swap! request assoc :desc (-> % .-target .-value))}]
          [:div.form-group
              [:label "Reporter:"]
              [:select.form-control {:field :list :id :many.options :on-change #(swap! request assoc :reporter (-> % .-target .-value))}
               [:option {:key :reporter1} "reporter1"]
               [:option {:key :reporter2} "reporter2"]]]

          [:div.form-group
              [:label "Assignee:"]
              [:select.form-control {:field :list :id :many.options :on-change #(swap! request assoc :assignee (-> % .-target .-value))}
               [:option {:key :assignee1} "assignee1"]
               [:option {:key :assignee2} "assignee2"]]]

          [:input {:type          "date"
                   :placeholder   "Date"
                   :default-value ""
                   :on-change     #(swap! request assoc :date (-> % .-target .-value))}]]
        [:button#form-button {:on-click #(post-request % request)} "Create request"]
        ]]))

(defn errors-list []
  (let [errors @(subscribe [:errors])]
    [:ul.error-messages
     (for [[key val] errors]
       ^{:key key} [:li (str key " " val)])]))

(defn request-app []
 [:div.container
  [request-form]
  [errors-list]
  [requests-list]])

(defn ^:export run []
  (dispatch-sync [:get-requests])
  (rdom/render [request-app] (js/document.getElementById "app")))
