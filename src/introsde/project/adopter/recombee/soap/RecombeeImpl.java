package introsde.project.adopter.recombee.soap;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.Batch;
import com.recombee.api_client.api_requests.Request;
import com.recombee.api_client.bindings.BatchResponse;
import com.recombee.api_client.bindings.Rating;
import com.recombee.api_client.exceptions.ApiException;

import introsde.project.adopter.recombee.init.Init;
import introsde.project.adopter.recombee.model.Evaluation;
import introsde.project.adopter.recombee.model.ItemObject;
import introsde.project.adopter.recombee.model.Recombee;
import introsde.project.adopter.recombee.model.RecombeeDBType;

@WebService(endpointInterface = "introsde.project.adopter.recombee.soap.RecombeeInterface")

public class RecombeeImpl implements RecombeeInterface{
	static RecombeeClient foodDB;
	static RecombeeClient movieDB;
	
	public RecombeeImpl() {

		foodDB= Recombee.setRecombeeClient("introsde-food","rntjKxWkHt56geGH7uF25kVSt4dQhAHx9jPS3v1yId7zZwEQUMlxvpFxxGLn3OMc");
		movieDB= Recombee.setRecombeeClient("introsde-movie","vFHY4J18WnyMsM3kA550soX5HIGiID0ctFiBvFAHcbdu13EY9G7Gh1jr60cUN7Pg");
		
	}

	@Override
	public boolean addUser(RecombeeDBType db, String userId, ArrayList<String> preitem) {
		if(db.equals(RecombeeDBType.foodDB)) 
			return Recombee.addNewUser(foodDB, userId,preitem);
		else
			return Recombee.addNewUser(movieDB, userId,preitem);
	}



	@Override
	public boolean addNewRating(RecombeeDBType db, Evaluation rating) {
		if(db.equals(RecombeeDBType.foodDB))
			return Recombee.addNewRating(foodDB, rating.getUserId(), rating.getItemId(), rating.getRating(), rating.getTime());
		else
			return Recombee.addNewRating(movieDB, rating.getUserId(), rating.getItemId(), rating.getRating(), rating.getTime());
	}



	@Override
	public List<Evaluation> getUserRatings(RecombeeDBType db, String userId) {
		List<Evaluation> list = new ArrayList<Evaluation>();
		if(db.equals(RecombeeDBType.foodDB)) {
			for(Rating r:Recombee.getUserRatings(foodDB, userId)) 
				list.add(new Evaluation(r.getUserId(),r.getItemId(),r.getRating(),r.getTimestamp()));
			return list;
		}
		else {
			for(Rating r:Recombee.getUserRatings(movieDB, userId)) 
				list.add(new Evaluation(r.getUserId(),r.getItemId(),r.getRating(),r.getTimestamp()));
			return list;
		}
	}

	@Override
	public List<ItemObject> getRecommendations(RecombeeDBType db, String userId, int quantity) {
		if(db.equals(RecombeeDBType.foodDB)) {
			return Recombee.getRec4User(foodDB, userId, quantity);
		}
		else{
			return Recombee.getRec4User(movieDB, userId, quantity);
		}
		
	}
	
	
	@Override
	public List<ItemObject> getItemsByType(RecombeeDBType db, String ItemType) {
		if(db.equals(RecombeeDBType.foodDB))
			return Recombee.getItemsByType(foodDB, ItemType);
		else
			return Recombee.getItemsByType(movieDB, ItemType);
	}

	@Override
	public List<ItemObject> getAllItem(RecombeeDBType db) {
		if(db.equals(RecombeeDBType.foodDB))
			return Recombee.getAllItems(foodDB);
		else
			return Recombee.getAllItems(movieDB);
	}

	@Override
	public List<Evaluation> getItemRatings(RecombeeDBType db, String itemId) {
		List<Evaluation> list = new ArrayList<Evaluation>();
		
		if(db.equals(RecombeeDBType.foodDB)) {
			for(Rating r:Recombee.getItemRatings(foodDB, itemId)) 
				list.add(new Evaluation(r.getUserId(),r.getItemId(),r.getRating(),r.getTimestamp()));
			return list;
		}
		else {
			for(Rating r:Recombee.getItemRatings(movieDB, itemId)) 
				list.add(new Evaluation(r.getUserId(),r.getItemId(),r.getRating(),r.getTimestamp()));
			return list;
		}
	}
	
	@Override
	public boolean initDB(RecombeeDBType db) {
		if(db.equals(RecombeeDBType.foodDB)) {
			return Init.initFoodDB();
		}
		else {
			return Init.initMovieDB();
		}
	}

	

	public static boolean addNewBatch(RecombeeDBType db,ArrayList<Request> interactions) {
		if(db.equals(RecombeeDBType.foodDB)){
		
		try {
			for(BatchResponse r: foodDB.send(new Batch(interactions))){
				System.out.println(r.getStatusCode());
				if(r.getStatusCode()!=200)
					return false;
			}
			return true;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		}
		else {
			try {
				movieDB.send(new Batch(interactions));
				for(BatchResponse r: movieDB.send(new Batch(interactions))){
					if(r.getStatusCode()!=200) {
						return false;
					}
				}
				return true;
			} catch (ApiException e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}	

	
	
}
