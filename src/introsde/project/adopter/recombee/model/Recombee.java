package introsde.project.adopter.recombee.model;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.*;
import com.recombee.api_client.bindings.RecommendationResponse;
import com.recombee.api_client.bindings.User;
import com.recombee.api_client.bindings.Item;
import com.recombee.api_client.bindings.Rating;
import com.recombee.api_client.bindings.Recommendation;
import com.recombee.api_client.exceptions.ApiException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.concurrent.TimeUnit;

public class Recombee{

	public Recombee() {
	}
	
	
	//add new user with given preferred items 
		public static boolean addNewUser(RecombeeClient client, String userId, ArrayList<String> preitem) {
			
			
			try {
				//check if user already exists
				if(getUser(client,userId)!=null)
					throw new Exception();
				
				HashMap<String, Object> map= new HashMap<String,Object>();
				map.put("PreferredTypes", preitem);
				client.send(new AddUser(userId));
				//TimeUnit.SECONDS.sleep(5);
				client.send(new SetUserValues(userId, map).setCascadeCreate(true));
				return true;
			} catch (ApiException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}



	//add rating for given item by user
    public static boolean addNewRating(RecombeeClient client, String user_id, String item_id, double item_rating, Date time){
    	//Date time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(LocalDateTime.now().toString());
    	
    	try {
    		ItemObject item =getItem(client,item_id); 
    		//if user and item doesn't exist in DB return false
    		if(item==null || getUser(client,user_id)==null)
    			return false;
    		
    		//if the user already rate this item it will return false
    		for(Rating r:client.send(new ListUserRatings(user_id))){
    			if(item_id.equals(r.getItemId())) {
    				return false;
				}
    		}
    		
    		
    		
    		//modify avg rating in recombee
    		item.setAvgRating(ItemObject.addToAvg(
    				item_rating,
					item.getAvgRating(),
					item.getNumberUserRating()
					));
    		item.setNumberUserRating(item.getNumberUserRating()+1);
    		
    		client.send(new SetItemValues((String) item.getItemId(), 
					new HashMap<String, Object>() {
						private static final long serialVersionUID = 1L;
					{
		                put("Type", item.getItemType());
		                put("Location", item.getLocation());
		                put("RatingAvg", item.getAvgRating());
					}}
					).setCascadeCreate(true));
    		
    		
    		
    		
    		//add rating to Recombee
    		client.send(new AddRating(user_id,item_id,item_rating).setTimestamp(time).setCascadeCreate(true));
    		return true;
    	} catch (ApiException e) {
			e.printStackTrace();
		}
    	return false;
		
	}
    


	//get item rating given item ID
	public static Rating[] getItemRatings(RecombeeClient client, String item_id) {
		Rating[] result = null;
		try {
			result = client.send(new ListItemRatings(item_id));
		} catch (ApiException e) {
			e.printStackTrace();
		}
		//for(Rating rec: result2) System.out.println("itemId->"+rec.getItemId()+" userId->"+rec.getUserId()+" rating->"+rec.getRating());
		return result;
	}
	
	//get item rating given item ID
	public static Rating[] getUserRatings(RecombeeClient client, String user_id) {
		Rating[] result = null;
		try {
			result = client.send(new ListUserRatings(user_id));
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	//get recommendation according to user preferences
	public static List<ItemObject> getRec4User(RecombeeClient client, String userId, int quantity) {
		List<ItemObject> list= new LinkedList<>();
		
		RecommendationResponse recommended = null;
		StringBuffer filterString= new StringBuffer();
		int flag =0;
		try {
			if(getUser(client,userId)==null)
				throw new Exception();
			
			//prepare filter String with given item preferences
			@SuppressWarnings("unchecked")
			ArrayList<String> preferences= (ArrayList<String>) client.send(new GetUserValues(userId)).get("PreferredTypes");
			for(Object type:preferences.toArray()) {
				if(flag==1)
					filterString.append(" or ");
				filterString.append("\""+type+"\" in 'Type'");
				flag=1;
			}
			//get recommendations
			recommended = client.send(new RecommendItemsToUser(userId,quantity)
					.setFilter(filterString.toString())
					.setReturnProperties(true)
					);
			for(Recommendation rec: recommended) {
				System.out.println(rec.getId());
				ItemObject item= new ItemObject();
				item.setItemId(rec.getId());
				for(Map.Entry<String,Object> entry:rec.getValues().entrySet()) {
					if(entry.getKey().equals("Location"))
						item.setLocation(entry.getValue().toString());
					if(entry.getKey().equals("Type"))
						item.setItemType(entry.getValue().toString());
					if(entry.getKey().equals("RatingAvg"))
						item.setAvgRating(Double.valueOf(entry.getValue().toString()));
					if(entry.getKey().equals("NumberOfUserRated"))
						item.setNumberUserRating(Integer.valueOf(entry.getValue().toString()));
				}
				list.add(item);
			}
			return list;
			
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	//System.out.println("recommended item to given user");
    	//for(Recommendation rec: recommended) System.out.println(rec.getId());
		return null;
		
	}
	
	public static List<ItemObject> getItemsByType(RecombeeClient client, String itemType) {
		List<ItemObject> item= new LinkedList<ItemObject>();
		try {
			for(Item l: client.send(new ListItems().setFilter("\""+itemType+"\" in 'Type'").setReturnProperties(true))){
				item.add(itemToItemObject(l));
				
			}
			return item;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static List<ItemObject> getAllItems(RecombeeClient client) {
		List<ItemObject> item= new LinkedList<ItemObject>();
		try {
			for(Item l: client.send(new ListItems().setReturnProperties(true))){
				item.add(itemToItemObject(l));
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return item;
	}

	public static RecombeeClient setRecombeeClient(String clientId, String password){
		RecombeeClient client= new RecombeeClient(clientId,password);
		try {
			//reset DataBase
			client.send(new ResetDatabase());
			//add item property
			client.send(new AddItemProperty("Type", "string"));
			client.send(new AddItemProperty("Location", "string"));
			client.send(new AddItemProperty("RatingAvg", "double"));
			client.send(new AddItemProperty("NumberOfUserRated", "int"));
			//add user property
			client.send(new AddUserProperty("PreferredTypes", "set"));
			return client;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public static boolean resetDB(RecombeeClient client) {
		try {
			//reset DataBase
			client.send(new ResetDatabase());
			//add item property
			client.send(new AddItemProperty("Type", "string"));
			client.send(new AddItemProperty("Location", "string"));
			client.send(new AddItemProperty("RatingAvg", "double"));
			client.send(new AddItemProperty("NumberOfUserRated", "int"));
			//add user property
			client.send(new AddUserProperty("PreferredTypes", "set"));
			return true;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	//get item given client and item id 
	public static ItemObject getItem(RecombeeClient client, String itemId) {
		try {
			for(Item u:client.send(new ListItems().setReturnProperties(true))) {
				if(u.getItemId().equalsIgnoreCase(itemId)) {
						return itemToItemObject(u);
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ItemObject itemToItemObject(Item u) {
		ItemObject item= new ItemObject();
		for(Map.Entry<String, Object> entry:u.getValues().entrySet()) {
			if(entry.getKey().equals("itemId")) 
				item.setItemId(entry.getValue().toString());
			if(entry.getKey().equals("Location"))
				item.setLocation(entry.getValue().toString());
			if(entry.getKey().equals("Type"))
				item.setItemType(entry.getValue().toString());
			if(entry.getKey().equals("RatingAvg"))
				item.setAvgRating(Double.valueOf(entry.getValue().toString()));
			if(entry.getKey().equals("NumberOfUserRated"))
				item.setNumberUserRating(Integer.valueOf(entry.getValue().toString()));
		}
		return item;
	}


	//get user given user id 
	public static User getUser(RecombeeClient client, String userId) {
		try {
			//TimeUnit.SECONDS.sleep(6);
			for(User u:client.send(new ListUsers())) {					
				if(userId.equals(u.getUserId())) {
					return u;
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
//  //modify rating
//  public static boolean modifyRating(RecombeeClient client, String user_id, String item_id, double item_rating, Date time){
//  	//Date time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(LocalDateTime.now().toString());
//  	try {
//  		//TimeUnit.SECONDS.sleep(1);
//  		//if user and item doesn't exist in DB return false
//  		if(getItem(client,item_id)==null || getUser(client,user_id)==null)
//  			return false;
//  		
//  		//find the old ratings and modify it
//  		for(Rating r:client.send(new ListUserRatings(user_id))){
//  			if(item_id.equals(r.getItemId())) {
//  				client.send(new DeleteRating(user_id, item_id));
//  				client.send(new AddRating(user_id,item_id,item_rating).setTimestamp(time));
//  				return true;
//  			}
//  		}
//  	} catch (ApiException e) {
//			e.printStackTrace();
//		}
//  	return false;
//  }

//	//add new item
//	public static ItemObject addNewItem(RecombeeClient client, String itemId, String Type, String location) {
//		//check if item already exists
//		if(getItem(client,itemId)!=null)
//			return null;
//		
//		HashMap<String, Object> map= new HashMap<String,Object>();
//		map.put("Type", Type);
//		map.put("Location", location);
//	   	SetItemValues r= new SetItemValues(itemId,map).setCascadeCreate(true);
//    	try {
//			client.send(r);
//			//TimeUnit.SECONDS.sleep(5);
//			return getItem(client,itemId);
//		} catch (ApiException e) {
//			e.printStackTrace();
//		} 
//		return null;
//	}
	

//    @SuppressWarnings({ "serial" })
//	public   void main(String[] args) throws ApiException, FileNotFoundException, IOException, org.json.simple.parser.ParseException, InterruptedException, ParseException {
//    	Recombee recm= new Recombee();
//    	
//    	RecombeeClient client = new RecombeeClient("introsde-food", "rntjKxWkHt56geGH7uF25kVSt4dQhAHx9jPS3v1yId7zZwEQUMlxvpFxxGLn3OMc");
//    	String detail_views="[\n" + 
//    			"	{\"user_id\": \"user-1\", \"item_id\":\"item-1\", \"Type\":\"Italian\", \"item_rating\":\"0.8\",\"timestamp\":\"2018-01-12T12:40:42+02:00\"},\n" + 
//    			"	{\"user_id\": \"user-2\", \"item_id\":\"item-2\", \"Type\":\"Indian\", \"item_rating\":\"0.6\",\"timestamp\":\"2018-01-12T12:40:42+03:00\"},\n" + 
//    			"	{\"user_id\": \"user-3\", \"item_id\":\"item-2\", \"Type\":\"Indian\", \"item_rating\":\"0.9\",\"timestamp\":\"2018-01-12T12:40:42+04:00\"},\n" + 
//    			"	{\"user_id\": \"user-1\", \"item_id\":\"item-3\", \"Type\":\"Chinese\", \"item_rating\":\"0.8\",\"timestamp\":\"2018-01-12T12:40:42+05:00\"},\n" + 
//    			"	{\"user_id\": \"user-4\", \"item_id\":\"item-4\", \"Type\":\"Italian\", \"item_rating\":\"0.9\",\"timestamp\":\"2018-01-12T12:40:42+06:00\"},\n" + 
//    			"	{\"user_id\": \"user-2\", \"item_id\":\"item-3\", \"Type\":\"Indian\", \"item_rating\":\"0.9\",\"timestamp\":\"2018-01-12T12:40:42+07:00\"},\n" + 
//    			"	{\"user_id\": \"user-3\", \"item_id\":\"item-1\", \"Type\":\"Italian\", \"item_rating\":\"1\",\"timestamp\":\"2018-01-12T12:40:42+09:00\"},\n" + 
//    			"	{\"user_id\": \"user-6\", \"item_id\":\"item-5\", \"Type\":\"Italian\", \"item_rating\":\"0.7\",\"timestamp\":\"2018-01-12T12:40:42+12:00\"},\n" + 
//    			"	{\"user_id\": \"user-5\", \"item_id\":\"item-6\", \"Type\":\"Chinese\", \"item_rating\":\"0.9\",\"timestamp\":\"2018-01-12T12:40:42+13:00\"},\n" + 
//    			"	{\"user_id\": \"user-1\", \"item_id\":\"item-7\", \"Type\":\"Mexican\", \"item_rating\":\"0.8\",\"timestamp\":\"2018-01-12T12:40:42+14:00\"},\n" + 
//    			"	{\"user_id\": \"user-5\", \"item_id\":\"item-8\", \"Type\":\"Thai\", \"item_rating\":\"0.4\",\"timestamp\":\"2018-01-12T12:40:42+15:00\"},\n" + 
//    			"	{\"user_id\": \"user-7\", \"item_id\":\"item-6\", \"Type\":\"Chinese\", \"item_rating\":\"0.7\",\"timestamp\":\"2018-01-12T12:40:42+16:00\"},\n" + 
//    			"	{\"user_id\": \"user-8\", \"item_id\":\"item-7\", \"Type\":\"Mexican\", \"item_rating\":\"0.8\",\"timestamp\":\"2018-01-12T12:40:42+17:00\"},\n" + 
//    			"	{\"user_id\": \"user-1\", \"item_id\":\"item-2\", \"Type\":\"Indian\", \"item_rating\":\"0.8\",\"timestamp\":\"2018-01-12T12:40:42+18:00\"}\n" + 
//    			"]";
//    	
//    	//array of interaction used to send all the requests via Batch method
//    			ArrayList<Request> interactions = new ArrayList<>();
//    			//set of all PreferredTypes related to each user
//    			Map<String,ArrayList<String>> userPreferrences= new HashMap<>();
//
//    			JSONParser parser = new JSONParser();
//    			try {
//    				//converting string to array
//    				JSONArray a = (JSONArray) parser.parse(detail_views);
//    				
//    				for (Object o : a) {
//    					JSONObject interaction = (JSONObject) o;
//    					
//    					Date time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse((String)interaction.get("timestamp"));
//    					String user_id= (String) interaction.get("user_id");
//    					String item_id= (String) interaction.get("item_id");
//    					String Type= (String) interaction.get("Type");
//    					double item_rating= Double.valueOf((String) interaction.get("item_rating"));
//    					
//    					//add Type to related user's PreferredTypes set
//    					if(!userPreferrences.containsKey(user_id)) {
//    						userPreferrences.put(user_id, new ArrayList<>());
//    						userPreferrences.get(user_id).add(Type);
//    					}
//    					else 
//    						userPreferrences.get(user_id).add(Type);
//    					
//    					//set Type value for given item id
//    					//it will create a new item if doesn't exist already
//    					SetItemValues r2= new SetItemValues(item_id, 
//    							new HashMap<String, Object>() {{
//    	                            put("Type", Type);
//    							}}
//    							).setCascadeCreate(true);
//    					interactions.add(r2);
//    					
//    					//adding rating given by related user to given item
//    					AddRating r3 = new AddRating(user_id,item_id,item_rating).setTimestamp(time).setCascadeCreate(true);
//    					interactions.add(r3);
//    					
//    				}
//    			} catch (org.json.simple.parser.ParseException e) {
//    				e.printStackTrace();
//    			} catch (ParseException e) {
//    				e.printStackTrace();
//    			}
//    	
//    	
//    	
//    	//save old data to DB
//    	if(recm.initDB(client,interactions,userPreferrences)==-1)
//    		System.out.println("init unSuccessful");
//    	
//    	//get five recommendations for user-3 
//    	RecommendationResponse rec=recm.getRec4User(client,"user-1",5);
//
//    	TimeUnit.SECONDS.sleep(8);
//    	Rating[] rating=recm.getItemRating(client, "item-3");
//    	
//    	//add new user
//    	User newU= recm.addNewUser(client,"user-9",new ArrayList<String>() {{
//    		add("Italian");
//    		add("Indian");
//    	}});
//    	
//    	newU=recm.addNewTypeUser(client,"user-9","Maxican");
//    	
//    	//add new item
//    	Item newI= recm.addNewItem(client,"item-9","Italian");
//    	
//    	//get recommendation for new user
//    	RecommendationResponse rec2= recm.getRec4User(client,newU.getUserId(),5);
//    	
//    	//add new ratings
//    	Date time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(LocalDateTime.now().toString());
//    	if(recm.addNewRating(client,newU.getUserId(),newI.getItemId(),0.9,time)==1) {
//    		System.out.println("rating added");
//    	}
//    	
//    	//modify rating
//    	if(recm.modifyRating(client,newU.getUserId(),newI.getItemId(),0.8,time)==1) {
//    		System.out.println("rating modified");
//    	}
//
//    }
    
  

	//Initialize the data to recombee DB
//  	@SuppressWarnings("serial")
//  	public   int initDB(RecombeeClient client, ArrayList<Request> interactions,
//  			Map<String, ArrayList<String>> userPreferrences) {
//  		
//  		try {
//  			//reset DataBase
//  			client.send(new ResetDatabase());
//  			//add item property
//  			client.send(new AddItemProperty("Type", "string"));
//  			//add user property
//  			client.send(new AddUserProperty("PreferredTypes", "set"));
//  		
//  						
//  			//set PreferredTypes to every related user. 
//  			for(String user_id: userPreferrences.keySet()) {
//  				//System.out.println(userPreferrences.get(user_id).toString());
//  				SetUserValues r= new SetUserValues(user_id,
//  					new HashMap<String, Object>() {{
//  		            put("PreferredTypes", userPreferrences.get(user_id));
//  					}}
//  					).setCascadeCreate(true);
//  				interactions.add(r);
//  			}				
//  		
//  			//Save interactions to DB, send interactions requests to recombee server
//  			for(BatchResponse r: client.send(new Batch(interactions))){
//  				if(r.getStatusCode()!=200) {
//  					//if any of the one request response is unsuccessful it will return -1
//  					return -1;
//  				}
//  			}
//  		} catch (ApiException e) {
//  			e.printStackTrace();
//  		}
//  		
//  		return 1;
//  	}
		

}


