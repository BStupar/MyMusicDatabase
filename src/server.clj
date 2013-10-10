(ns server
  (:require [compojure.route :as route]
            [noir.session :as session]
				    [ring.util.response :as response])
  (:use [compojure.core :only [defroutes GET POST DELETE PUT]]
        [ring.adapter.jetty :only [run-jetty]]
        [main :only [main-page]]
        [login :only [login-page do-login do-logout]]
        [register :only [register-page do-register]]
        [mongodb :only [insert-user insert-user-album get-user-by-username]]
        [album :only [album-page add-new-album]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [ring.middleware.params :only [wrap-params]]))

(defroutes handler
  (GET "/" [] (main-page))
  (GET "/login" [] (let [user (session/get :user)] (if-not user (login-page) (main-page))))
  (POST "/login" [username password] (do-login username password))
  (GET "/logout" [] (do (do-logout) (response/redirect "/")))
  (GET "/register" [] (let [user (session/get :user)] (if-not user (register-page) (main-page))))
  (POST "/register" [first-name last-name email username password confirm-password] (do-register first-name last-name email username password confirm-password)) 
  (GET "/newalbum" [] (let [user (session/get :user)] (if user (album-page) (login-page))))
  (POST "/newalbum" [album-name album-artist album-year album-cover-image] (add-new-album album-name album-artist album-year album-cover-image))
  (route/resources "/")
  (route/not-found "Page not found."))

 (def app
  (-> #'handler
    (wrap-reload)
    (wrap-params)
    (session/wrap-noir-flash)
    (session/wrap-noir-session)
    (wrap-stacktrace)))


 (defn start-jetty-server []
   (run-jetty #'app {:port 9000 :join? false}))
 
 (defn insert-test-user [] 
  (insert-user "Test User" "test@test.com" "test" "test"))
 
 (defn insert-test-data [] 
   (let [user (get-user-by-username "test")]
     (do
       (insert-user-album "Thriller" "Michael Jackson" "2001" "images/album-cover1.jpg" "2013-10-05" (:_id user))
       (insert-user-album "The Doors" "The Dorrs" "2002" "images/album-cover2.jpg" "2013-10-04" (:_id user))
       (insert-user-album "One Love" "David Guetta" "2003" "images/album-cover3.jpg" "2013-10-03" (:_id user))
       (insert-user-album "Born This Way" "Lady Gaga" "2004" "images/album-cover4.jpg" "2013-10-02" (:_id user))
       (insert-user-album "Can't be Tamed" "Miley Cyrus" "2005" "images/album-cover5.jpg" "2013-10-01" (:_id user)))))

 (defn -main [& args]
   (do
     (start-jetty-server)
      (let [user (get-user-by-username "test")]
       (if-not user (do (insert-test-user) (insert-test-data))))))
 