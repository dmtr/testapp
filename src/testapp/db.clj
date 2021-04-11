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

(def test-q '[:find ?request-title :where [_ :request/title ?request-title]])

(defn get-requests []
  (d/q {:query test-q :limit 2 :args [(d/db @*conn)]}))
