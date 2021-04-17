(ns testapp.core-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [testapp.core :refer [app]]
            [testapp.db :refer [init-db]]))

(defn db-fixture [f]
  (init-db)
  (f))

(use-fixtures :each db-fixture)

(def request-body {:title "test"
                   :desc "descr"
                   :reporter "Smith" 
                   :assignee "Snow" 
                   :date "02-02-2020"})

(deftest create-request-test
  (let [res (app (-> (mock/request :post "/requests")
                  (mock/json-body request-body)))]
    (is (= (:status res) 201 ))
    (is (contains? (json/read-str (:body res) ) "id"))))
