# MyMusicDatabase

This is a small web application in Clojure wich uses libraries: Ring, 
Mongo-session, Compojure, lib-noir, Hiccup, clj-time, CongoMongo. 
In this application you can store your music albums in music database. 
You can log in with test user and password.
And if you like you can, also, register yourself and add some album in your database. 
Album page displays albums of artist with name and year.


## Setup instructions  for running locally

Download and install Leiningen.

Download and install MongoDB.

Start MongoDB.

Start your application (use command line), go to the project directory and run lein run.



##References

Practical Clojure, Luke VanderHart and Stuart Sierra

Developing and Deploying a Simple Clojure Web Application and A brief overview of the Clojure web stack for learning Ring, Compojure and Hiccup

Programming Collective Intelligence, Toby Seagaran 

CongoMongo library for using MongoDB with Clojure - mongo.clj

Hickory library for parsing HTML used to extract data from web page - extract_data.clj.


## License

Distributed under the Eclipse Public License, the same as Clojure.
