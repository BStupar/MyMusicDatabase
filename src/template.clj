(ns template
  (:require [noir.session :as session])
  (:use  [hiccup.core :only [html]]
         [hiccup.page :only [include-css doctype]]
         [mongodb :only [get-latest-albums]]))

(defn not-logged-in-menu
  "Generates the menu for all pages when the user is not logged in."
  []
  [:ul#nav 
   [:li#current 
    [:a {:href "/"} "Home"]]
   [:li 
    [:a {:href "/login"} "Login"]]
   [:li 
    [:a {:href "/register"} "Register"]]])

(defn logged-in-menu
  "Generates the menu for all pages when the user is logged in."
  []
  [:ul#nav 
   [:li#current 
    [:a {:href "/"} "Home"]]
   [:li 
    [:a {:href "/newalbum"} "New album"]]
   [:li 
    [:a {:href "/logout"} "Logout"]]])

(defn show-one-album
  "Generates HTML for one album."
  [album]
  [:li
    [:a {:href "#"} 
     [:img {:src  (:album-cover-image album) :width "105px" :height "105px" :alt ""}]]
    [:h2 (:album-title album)]])
  
(defn get-new-albums
  "Retrieves the latest five albums and displays them."
  []
  [:ul
   (let [albums (get-latest-albums)]
   (for [album albums]
		(show-one-album album)))])

(defn get-template
  "Generates template for all pages in application."
  [title content]
  (html
    (doctype :xhtml-transitional)
    [:html {:xmlns "http://www.w3.org/1999/xhtml" "xml:lang" "en" :lang "en"} 
      [:head
        (include-css "/css/style.css")
        [:meta {:charset "UTF-8"}]
        [:title title]]
      [:body
       (let [user (session/get :user)] 
         (if-not user (not-logged-in-menu) (logged-in-menu)))
         [:h1#title "My Music Database"]
         [:div#wrapper 
          [:div#top]
          [:div#main
           [:h1 "New Albums"]
           (get-new-albums)]
          content
          [:div#footer "Author: Branka Stupar"]]]]))
