# Identification:  
NAME: Cheema Danish Asghar  
EMAIL: danishasghar.cheema@studenti.unitn.it  
  
Client Code Done by:  
NAME: Main muhammad faheem - Jan  
EMAIL:main.jan@unitn.it  
  
Server heroku ULR:  
Server Git ripo:  
  
Client Git riop:  
  
## Implementation:  
### RecombeeImpl.java
This Class Implements RecombeeInterface Class which overrides all the methods defined in that class for soap webService.  
  
* at first deply to heroku it initialize the data to recombee DB by reading the json files.  
here is how the recombee User and item data structured:  
//TODO add tables of recombee DB.  

* In the constructor it initalized the RecombeeFood and RecombeeMovie remote DataBases.  
* Method #1: `addNewUser(DBType, userId, preferences)` DBType can be RecombeeFood/RecombeeMovie and preferences can be list of FoodTypes/MovieGenres.  

* Method #2:  

### Recombee.java
* Method #1: `addNewUser(RecombeeClient, userId , preferences)` it persists the data to Remote Recombee DB, Recombee throws ApiException if unsuccessfull.  
* Method #2:










In this reipository there are some files and packages and classes. There are three packages in this this project.
1.init
2.model
3.soap

In the init package there are two classes:
1.init.java : it has two Methods
- boolean initMovieDB() : it initializes the Movie database.
- boolean initFoodDB() : it initializes the Food database. 2.RecombeePublisher.java : It publishes things on the server. it has following attributes.
- SERVER_URL = "http://localhost"; - PORT = "6908"; - BASE_URL = "/ws/recombee";
there is a method
String getEndpointURL() which returns the end url
and then there is the main method which calls on the EndpointURL.and publish things on that end point URL.

in the model package there are 4 classes. 1.Evaluation.java : It has the following attributes -String userId; -String itemId; -double rating; -Date time; There is the constructor which access and gives some values to these attributes. then there are some getters and setters to set and get these attributes. the goal of this class to make it possibile for a specific user at a specific time to evaluate an item.
2.ItemObject.java************************** To Be Continued******************************************** 3.Recombee.java 4.RecombeeDBType.java
