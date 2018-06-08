# Recombee Adopter Layer

NAME: Cheema Danish Asghar  
EMAIL: danishasghar.cheema@studenti.unitn.it  
  
Group Partner:  
NAME: Main muhammad faheem - Jan  
EMAIL:main.jan@unitn.it  
  
heroku WSDL ULR:  https://recombee-introsde-2018.herokuapp.com/ws/recombee?wsdl  
Git ripo:  https://github.com/introsde-2017-project/RecombeeAdapterService  
Project WIKI: https://github.com/introsde-2017-project/Wiki  
   

## introduction:



## Architecture
There are three packages in this layer.    
* init -> use to persist the initial data to recombee DB.  
* model -> consist of all the POJO and Business logic classes required to persist and retrive the data from recombee and return it to other layers in project.  
* soap -> it defines all the methods visible to service clients.

  
## Implementation:  

### init package
* In the `init` package there is `Init.java` class consist of two methods:    
  * initMovieDB() : it initializes the Movie database reading data from Json file, in my project call by Business Layer.  
  * initFoodDB() : it initializes the Food database reading data from Json file, in my project call by Business Layer.  

### model package
In the `model` package there are 4 classes.   
#### `Recombee.java` 
* its one of the most important class defines all the methods to retrive persist modify update the data to Remote Recombee DataBase.  
* List of important Methods:  
  * `addNewUser(RecombeeClient, userId, ListOfPreferences)`  
  -> used to add new user to Recombee BD with its favourite Preferences throws ApiException if unsuccessful.  
  * `addNewRating(RecombeeClient , user_id, item_id, item_rating, time)`  
  -> adds rating for   given item by user at given time.  
  * `getItemRatings(RecombeeClient , item_id)`  
  -> gets item rating given item ID.  
  * `getUserRatings(RecombeeClient client, String user_id)`  
  -> returns all the ratings given by a user identified by userId.   
  * `getRecommendations(RecombeeClient , userId, quantity)`  
  -> gets recommendation according to user preferences.  
  * `getItemsByType(RecombeeClient ,ItemType)`  
  -> this method returns item list by its type. 
  * `getAllItem(RecombeeClient)`  
  -> it returns the list of all the items.  
  * `setRecombeeClient(RecombeeClientId, password)`  
  -> here setting the recombee client . it resets reset DataBase, adds item property and then add user property.  
`NOTE: here RecombeeClient is of com.recombee.api_client.RecombeeClient type, which is differnt from RecombeeDBType, check RecombeeDBType.java description below for more detail.`  
    
#### `Evaluation.java` 
* its a pojo class used for sending the Rating details to service client.  
* Attributes  
    
     |String userId |String itemId  |double rating  |Date time  |  
     |--------------|---------------|---------------|-----------|  

#### `ItemObject.java`
* its a pojo class used for sending the food/movie details to client side. 
* Attributes
    
     |String itemId |String location|String ItemType |double angRating|  
     |--------------|---------------|----------------|----------------| 
#### `RecombeeDBType.java` 
* its a enum class with foodDB and movieDB strings, just for the convenience of the client to have more freedom to chose which DB they want to interact to.
  
### Soap package
In the `soap` package there are soap classes used to define the services availabe to clients.
#### `RecombeeInterface.java`

#### `RecombeeImpl.java`
* This Class Implements RecombeeInterface Class which overrides all the methods defined in that class for soap webService.  
* In the constructor it initalized the RecombeeFood and RecombeeMovie remote DataBases. 
* List of Services for client:  
  * `addUser(RecombeeDBType, userId, ListOfPreferences)`  
  -> DBType can be RecombeeFood/RecombeeMovie and preferences can be list of FoodTypes/MovieGenres.  
  * `addNewRating(RecombeeDBType, Evaluation )`  
  -> add new rating to given recombee DB. Evaluation Object contains all the required data, check the POJO class above.  
  * `getUserRatings(RecombeeDBType, userId)` 
  -> returns all the ratings given by a user identified by userId. 
  * `getRecommendations(RecombeeDBType, userId, quantity)`  
  -> get the recommendations from recombee and returns a list of ItemObjects.     
  * `getItemsByType(RecombeeDBType ,ItemType)`  
  -> this method returns item list by its type.     
  * `getAllItem(RecombeeDBType )`  
  -> it first checks if the asked items are from movie or from food. then it returns the list of all the items.    
  * `getItemRatings(RecombeeDBType, itemId)`  
  -> this method returns the list of all the ratings of the item.  
  * `initDB(RecombeeDBType)`  
  -> call `inti.java` class and initialize data to Recombee DB by it given movieDB/foodDB type.



   

 









