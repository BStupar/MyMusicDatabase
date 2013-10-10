(ns main
  (:use  [template :only [get-template]]
         [mongodb :only [get-all-albums]]))

(defn show-one-album
  "Generates HTML for one album."
  [album]
  [:div.albuminfo
   [:img {:src (:album-cover-image album) :width "180px" :height "180px" :alt ""}]
    [:h2 (:album-title album)]
    [:p.albumartist (str "Artist: "(:album-artist album))]
    [:p.albumyear (str "Year: "(:album-year album))]
    ])
  
(defn show-all-albums
  "Retrieves all albums from database and displays them."
  []
  [:div.albuminfo
   (let [albums (get-all-albums)]
   (for [album albums]
		(show-one-album album)))])

(defn main-page 
  "Displays main page."
  []
  (get-template "My Music Database" 
   [:div.content
    (show-all-albums)
     ]))

