package introsde.project.adopter.recombee.init;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.recombee.api_client.api_requests.AddRating;
import com.recombee.api_client.api_requests.Request;
import com.recombee.api_client.api_requests.SetItemValues;

import introsde.project.adopter.recombee.model.RecombeeDBType;
import introsde.project.adopter.recombee.soap.RecombeeImpl;

public class Init {
	
	public static boolean initMovieDB() {
		
		JSONParser parser = new JSONParser();
		ArrayList<Request> interactions = new ArrayList<>();
        
		
		/////////////// movie database initialization
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader("movie_rating.json"));
			for (Object o : a) {
				JSONObject interaction = (JSONObject) o;
				String Type= (String) interaction.get("item_type");
				String location= (String) interaction.get("location");
				
				SetItemValues r2= new SetItemValues((String) interaction.get("item_name"), 
						new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;
						{
                            put("Type", Type);
                            put("Location", location);
						}}
						).setCascadeCreate(true);
				interactions.add(r2);
				
				double rating =Double.valueOf((String) interaction.get("item_rating"));
				Date time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse((String)interaction.get("timestamp"));
				
				AddRating r = new AddRating((String) interaction.get("user_name")
						,(String) interaction.get("item_name")
						,rating
						)
						.setTimestamp(time)
						.setCascadeCreate(true);
				interactions.add(r);
	                
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return RecombeeImpl.addNewBatch(RecombeeDBType.movieDB,interactions);
	}
		
	public static boolean initFoodDB() {
		
		ArrayList<Request> interactions = new ArrayList<>();
		JSONParser parser = new JSONParser();
        
		//////////// food Recombee database initialization
		
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader("food_rating.json"));
			for (Object o : a) {
				JSONObject interaction = (JSONObject) o;
				String Type= (String) interaction.get("item_type");
				String location= (String) interaction.get("location");
				
				double rating =Double.valueOf((String) interaction.get("item_rating"));
				Date time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse((String)interaction.get("timestamp"));
				
				//set Type value for given item id
//				//it will create a new item if doesn't exist already
				
				SetItemValues r2= new SetItemValues((String) interaction.get("item_name"), 
						new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;
						{
                            put("Type", Type);
                            put("Location", location);
						}}
						).setCascadeCreate(true);
				interactions.add(r2);
				
				AddRating r = new AddRating((String) interaction.get("user_name")
						,(String) interaction.get("item_name")
						,rating
						)
						.setTimestamp(time)
						.setCascadeCreate(true);
				interactions.add(r);
	                
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return RecombeeImpl.addNewBatch(RecombeeDBType.foodDB,interactions);
		
            
	}

}
