(ns testapp.db
  (:require [datomic.client.api :as d]))

(def client (d/client {:server-type :dev-local :system "dev" :storage-dir :mem}))

(def db-name "requests")

(def *conn (atom nil))
                     
(def requests-schema 
  [{:db/ident :request/title 
    :db/valueType :db.type/string 
    :db/cardinality 
    :db.cardinality/one :db/doc "Request title"} 
   {:db/ident :request/desc 
    :db/valueType :db.type/string 
    :db/cardinality :db.cardinality/one :db/doc "Request description"}])

(defn init-db []
  (d/create-database client {:db-name db-name})
  (reset! *conn (d/connect client {:db-name db-name}))
  (d/transact @*conn {:tx-data requests-schema}))

(defn add-request [request]
  (d/transact @*conn {:tx-data [request]}))

(def requests-query '[:find ?request-title ?request-desc
                      :keys title desc
                      :where [?title :request/title ?request-title]
                      [?desc :request/desc ?request-desc]])

(defn get-requests [limit offset]
  (d/q {:query requests-query
        :limit (if (nil? limit) 10 (Integer/parseInt limit))
        :offset (if (nil? offset) 0 (Integer/parseInt offset))
        :args [(d/db @*conn)]}))
