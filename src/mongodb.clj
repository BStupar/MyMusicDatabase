(ns mongodb
  (:require [noir.session :as session]
            [clj-time.format :as time-format]
            [clj-time.core :as time])
  (:use [somnium.congomongo]))

(def conn 
  (make-connection "my_music_database"))

(set-connection! conn)

(defn- generate-id [collection]
  "Generate entity identifier." 
  (:seq (fetch-and-modify :sequences {:_id collection} {:$inc {:seq 1}}
                          :return-new? true :upsert? true)))

(defn- insert-entity [collection values]
   "Insert an entity into database."
  (insert! collection (assoc values :_id (generate-id collection))))

(defn insert-user
  [name email username password]
  "Insert user into database." 
  (insert-entity :users 
                  {:name name
                   :email email
                   :username username
                   :password password}))

(defn get-user-by-username [username]
  "Find user by username."  
  (fetch-one :users :where {:username username}))

(defn get-user-by-email [email]
  "Find user by email."  
  (fetch-one :users :where {:email email}))

(defn insert-user-album
  [album-title album-artist album-year album-cover-image date-added user-id]
  "Insert album into database." 
  (insert-entity :user-albums 
                  {:album-title album-title
                   :album-artist album-artist
                   :album-year album-year
                   :album-cover-image album-cover-image
                   :date-added date-added
                   :user-id user-id}))

(defn get-latest-albums []
  "Find the latest five albums." 
  (fetch :user-albums :sort {:date-added -1} :limit 5))

(defn get-all-albums []
  "Return all albums fro the database." 
  (fetch :user-albums :sort {:date-added -1}))

(def parser-formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss"))

(defn insert-new-album
  "Inserts data for new album into data base."
  [album-title album-artist album-year album-cover-image]
  (let [user (session/get :user)]
    (insert-entity :user-albums 
                   {:album-title album-title
                   :album-artist album-artist
                   :album-year album-year
                   :album-cover-image album-cover-image
                   :date-added (time-format/unparse parser-formatter (time/now))
                   :user-id (:_id user)})))