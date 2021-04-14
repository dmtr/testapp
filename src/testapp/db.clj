(ns testapp.db
  (:require [datomic.client.api :as d]
            [testapp.utils :as utils]))

(def client (d/client {:server-type :dev-local :system "dev" :storage-dir :mem}))

(def db-name "requests")

(def *conn (atom nil))
                     
(def requests-schema 
  [{:db/ident :request/id 
    :db/valueType :db.type/uuid 
    :db/cardinality 
    :db.cardinality/one :db/doc "Request ID" :db/unique :db.unique/identity} 
   {:db/ident :request/title 
    :db/valueType :db.type/string 
    :db/cardinality 
    :db.cardinality/one :db/doc "Request title"} 
   {:db/ident :request/desc 
    :db/valueType :db.type/string 
    :db/cardinality :db.cardinality/one :db/doc "Request description"}
   {:db/ident :request/reporter 
    :db/valueType :db.type/string 
    :db/cardinality :db.cardinality/one :db/doc "Request reporter"}
   {:db/ident :request/assignee 
    :db/valueType :db.type/string 
    :db/cardinality :db.cardinality/one :db/doc "Request assignee"}
   {:db/ident :request/date 
    :db/valueType :db.type/string 
    :db/cardinality :db.cardinality/one :db/doc "Request date"}])

(defn init-db []
  (d/create-database client {:db-name db-name})
  (reset! *conn (d/connect client {:db-name db-name}))
  (d/transact @*conn {:tx-data requests-schema}))

(defn add-request [request]
  (d/transact @*conn {:tx-data [request]}))

(def requests-query '[:find ?request-id ?request-title ?request-desc
                            ?request-reporter ?request-assignee ?request-date
                      :keys id title desc reporter assignee date
                      :where [?id :request/id ?request-id]
                      [?title :request/title ?request-title]
                      [?desc :request/desc ?request-desc]
                      [?reporter :request/reporter ?request-reporter]
                      [?assignee :request/assignee ?request-assignee]
                      [?date :request/date ?request-date]])

(defn get-requests [limit offset]
  (d/q {:query requests-query
        :limit (utils/parse-int-safe limit 10)
        :offset (utils/parse-int-safe offset 0)
        :args [(d/db @*conn)]}))
