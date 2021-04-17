(ns testapp.core-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [testapp.core :refer [app]]
            [testapp.db :refer [init-db add-request clear-db]]
            [testapp.utils :as utils]))

(defn db-fixture [f]
  (init-db)
  (f)
  (clear-db))

(use-fixtures :each db-fixture)

(def request-body {:title "test"
                   :desc "descr"
                   :reporter "Smith" 
                   :assignee "Snow" 
                   :date "02-02-2021"})

(deftest create-request-test
  (let [res (app (-> (mock/request :post "/requests")
                  (mock/json-body request-body)))]
    (is (= (:status res) 201 ))
    (is (contains? (json/read-str (:body res) ) "id"))))


(def request {:request/title "new request"
              :request/desc "important" 
              :request/reporter "John"
              :request/assignee "Nick" 
              :request/date "02-02-2021"
              :request/id (utils/uuid)})

(deftest list-requests-test
  (let [_ (add-request request)
        res (app (-> (mock/request :get "/requests")))
        body (json/read-str (:body res))
        headers (:headers res)
        count_ (get body "count")
        req (first (get body "results"))]
    (is (= (:status res) 200))
    (is (=  (get headers "Content-Type") "application/json; charset=utf-8"))
    (is (= count_ 1))
    (is (= (get req "title") (:request/title request)))))
