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

In this repository there are some files and packages and classes. There are three packages in this this project.  
1.init  
2.model  
3.soap  

In the init package there are two classes:  
1.init.java : it has two Methods    
- boolean initMovieDB() : it initializes the Movie database.    
- boolean initFoodDB() : it initializes the Food database.    
2.RecombeePublisher.java : It publishes things on the server. it has following attributes.    
- SERVER_URL = "http://localhost";     
- PORT = "6908"; - BASE_URL = "/ws/recombee";    
there is a method:    
String getEndpointURL() which returns the end url    
and then there is the main method which calls on the EndpointURL.and publish things on that end point URL.    

in the "model" package there are 4 classes.       
1.Evaluation.java :     
It has the following attributes      
 -String userId;    
 -String itemId;    
 -double rating;    
 -Date time;    
 There is the constructor which access and gives some values to these attributes. then there are some getters and setters     
 to set and get these attributes. the goal of this class to make it possibile for a specific user at a specific time to evaluate an   item.      
2.ItemObject.java    
3.Recombee.java    
4.RecombeeDBType.java    



### RecombeeImpl.java  
This Class Implements RecombeeInterface Class which overrides all the methods defined in that class for soap webService.  
  
* at first deply to heroku it initialize the data to recombee DB by reading the json files.  
here is how the recombee User and item data structured:  
//TODO add tables of recombee DB.  

* In the constructor it initalized the RecombeeFood and RecombeeMovie remote DataBases.  
* Method #1: `addUser(DBType, userId, preferences)` DBType can be RecombeeFood/RecombeeMovie and preferences can be list of FoodTypes/MovieGenres.  
 
* Method #2:  `addNewRating(RecombeeDBType db, Evaluation rating)` through this method items are rated by user with id UserId . this method tells us who rated which item at what time and stores it in the database.   

|UserId |ItemId  |Rating  |Time    |     
|-------|--------|--------|--------|    
|123    |777     |9.5     |12.00   |    
|333    |999     |7.9     |10.00   |    

* Method #3: `getRecommendations(RecombeeDBType db, String userId, int quantity)` This method recommends number of items to the user   with Userid.First it checks if the database is food or movie then recommend items accordingly.  

|Food |UserId|Quanity  |           
|-----|------|---------|           
|Pasta|9115  |7        |     
|Ceci |999   |6        |     


Or if the database is Movie then: 

|Movie|UserId|Quantity|          
|-----|------|--------|          
|War|777   |4       |          
|Cartoon|992   |8       |    

* Method #4: `getItemsByType(RecombeeDBType db, String ItemType)` this method returns item list by its type. first it checks if the   item is from Movie or from food.     
* Method #5 `getAllItem(RecombeeDBType db)` it first checks if the asked items are from movie or from food. then it returns the list   of all the items.    
* Method #6 `getItemRatings(RecombeeDBType db, String itemId)` this method returns the list of all the ratings of the item.  
* Method # 7 `initDB(RecombeeDBType db)` This method first check if the parameter db is movie it initialize the movie database   otherwise it initialize the Food database.   

### Recombee.java
* Method #1: `addNewUser(RecombeeClient, userId , preferences)` it persists the data to Remote Recombee DB, Recombee throws ApiException if unsuccessfull.  
* Method #2: `addNewUser(RecombeeClient client, String userId, ArrayList<String> preitem)` Adds new user with prefered items.
* Method #3" `addNewRating(RecombeeClient client, String user_id, String item_id, double item_rating, Date time)` adds rating for   given item by user at given time.    
* Method #4 `getItemRatings(RecombeeClient client, String item_id)`  gets item rating given item ID.  
* Method #5 `getRec4User(RecombeeClient client, String userId, int quantity)` gets recommendation according to user preferences.  
* Method #6 `setRecombeeClient(String clientId, String password)` here setting the recombee client . it resets reset DataBase,  
adds item property and then add user property.  

### RecombeeDBType.java  
It has two database type 1)Food 2)Movie. it has an attribute "String name" for which there is  getter and setter.  
* Method #1 `getAll()` which returns the list of all databases.  
* Method #2 `fromString(String text)` it returns the type of database i.e movie or Food.  











