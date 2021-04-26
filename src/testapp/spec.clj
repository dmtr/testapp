(ns testapp.spec
  (:require [clojure.spec.alpha :as s]))

(def date-regex #"^[0-9]{4}-[0-9]{2}-[0-9]{2}$")
(s/def ::date-type (s/and string? #(re-matches date-regex %)))

(s/def ::title string?) 
(s/def ::desc string?) 
(s/def ::reporter string?) 
(s/def ::assignee string?) 
(s/def ::date ::date-type)

(s/def :unq/request
  (s/keys :req-un [::title ::desc ::reporter ::assignee ::date]))

(defn validate-request [request]
  (s/explain-data :unq/request request))
