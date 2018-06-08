# Identification:  
NAME: Cheema Danish Asghar  
EMAIL: danishasghar.cheema@studenti.unitn.it  
  
Client Code Done by:  
NAME: Main muhammad faheem - Jan  
EMAIL:main.jan@unitn.it  
  
Server heroku ULR:  
Server Git ripo:  
  
Client Git riop: 

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
* List of Methods:  
  * `addNewUser(RecombeeClient, userId, ListOfPreferences)` -> used to add new user to Recombee BD with its favourite Preferences throws ApiException if unsuccessful.  
  * `addNewRating(RecombeeClient , user_id, item_id, item_rating, time)` adds rating for   given item by user at given time.  
  * `getItemRatings(RecombeeClient , item_id)`  gets item rating given item ID.  
  * `getRecommendations(RecombeeClient , userId, quantity)` gets recommendation according to user preferences.  
  * `setRecombeeClient(RecombeeClientId, password)` here setting the recombee client . it resets reset DataBase, adds item property and then add user property.  
    
#### `Evaluation.java` 
* its a pojo class used for sending the Rating details to service client.  
* Attributes  
    
     |String userId |String itemId  |double rating  |Date time  |  
     |--------------|---------------|---------------|-----------|  

#### ItemObject.java
* its a pojo class used for sending the food/movie details to client side. 
* Attributes
    
     |String itemId |String location|String ItemType |double angRating|  
     |--------------|---------------|----------------|----------------| 
#### RecombeeDBType.java  
* its a enum class with foodDB and movieDB strings, just for the convenience of the client to have more freedom to chose which DB they want to interact to.
  
### Soap package
In the `soap` package there are soap classes used to define the services availabe to clients.
#### RecombeeInterface.java

#### RecombeeImpl.java
* This Class Implements RecombeeInterface Class which overrides all the methods defined in that class for soap webService.  
* In the constructor it initalized the RecombeeFood and RecombeeMovie remote DataBases. 
* List of Methods:
  * `addUser(DBType, userId, preferences)` DBType can be RecombeeFood/RecombeeMovie and preferences can be list of FoodTypes/MovieGenres.  
  * `addNewRating(RecombeeDBType db, Evaluation rating)` through this method items are rated by user. this method is used to save data like who rated which item at what time and stores it in the database.   

      |UserId |ItemId  |Rating  |Time        |     
      |-------|--------|--------|------------|    
      |501    |101     |4.5     |timestamp   |    
      |502    |102     |3.5     |timestamp   |    

   * `getRecommendations(RecombeeDBType db, String userId, int quantity)` This method recommends number of items to the user   with Userid.First it checks if the database is food or movie then recommend items accordingly.  

        |String itemId |String location|String ItemType |double angRating|           
        |--------------|---------------|----------------|----------------|           
        |Pasta|505   |5        |     
        |Ceci |   |5        |     


        Or if the database is Movie then: 

        |Movie|UserId|Quantity|          
        |-----|------|--------|          
        |War|777   |4       |          
        |Cartoon|992   |8       |    

    * `getItemsByType(RecombeeDBType db, String ItemType)` this method returns item list by its type. first it checks if the   item is from Movie or from food.     
    * `getAllItem(RecombeeDBType db)` it first checks if the asked items are from movie or from food. then it returns the list   of all the items.    
    * `getItemRatings(RecombeeDBType db, String itemId)` this method returns the list of all the ratings of the item.  
    * `initDB(RecombeeDBType db)` This method first check if the parameter db is movie it initialize the movie database   otherwise it initialize the Food database.



   

 









