(ns album
  (:require [noir.session :as session]
            [ring.util.response :as response])
  (:use [template :only [get-template]]
        [hiccup.form :only [form-to label text-field submit-button]]
        [mongodb :only [insert-new-album]]))

(defn check-album-data
  "Checks album data."
  [album-name album-artist album-year album-cover-image]
  (cond
    (> 1 (.length album-name)) "Album name must be at least 1 character long."
    (> 1 (.length album-artist)) "Album artist must be at least 1 character long."
    (> 1 (.length album-year)) "Album year must be at least 1 character long."
    (> 5 (.length album-cover-image)) "Album cover image path must be at least 5 characters long."
    :else true))

(defn add-new-album
  "Adds new album."
  [album-name album-artist album-year album-cover-image]
  (let [album-error-message (check-album-data album-name album-artist album-year album-cover-image)]
    (if-not (string? album-error-message)
      (do 
        (insert-new-album album-name album-artist album-year album-cover-image)
        (response/redirect "/"))
      (do
        (session/flash-put! :album-error album-error-message)
        (session/flash-put! :album-name album-name)
        (session/flash-put! :album-artist album-artist)
        (session/flash-put! :album-year album-year)
        (session/flash-put! :album-cover-image album-cover-image)
        (response/redirect "/newalbum")))))
                
   
(defn album-page
  "Show album page"
  []
  (get-template "Album page"
   [:div.content
    [:p.albumtitle "Enter  information about new album!"]
     [:p.albumerror (session/flash-get :album-error)]
    (form-to [:post "/newalbum"]
             [:div.newalbumform
              [:div
              (label {:class "albumlabel"} :album-name "Album name")
               (text-field :album-name (session/flash-get :album-name))]
              [:div
               (label {:class "albumlabel"} :album-artist "Artist")
                (text-field :album-artist (session/flash-get :album-artist))]
              [:div
               (label {:class "albumlabel"} :album-year "Year")
                (text-field :album-year (session/flash-get :album-year))]
              [:div
               (label {:class "albumlabel"} :album-cover-image "Path to cover image")
                (text-field :album-cover-image (session/flash-get :album-cover-image))]
               [:div
                (submit-button "Save album")]])]))
                
                                    
 
