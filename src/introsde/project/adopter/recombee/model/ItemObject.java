package introsde.project.adopter.recombee.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
public class ItemObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private String itemId;
	private String location;
	private String itemType;
	private double avgRating;
	private int numberUserRating;


	

	public ItemObject() {}
	
	public ItemObject(String itemId, String location, String itemType, double avgRating) {
		this.itemId=itemId;
		this.location=location;
		this.itemType=itemType;
		this.avgRating=avgRating;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}
	
	public int getNumberUserRating() {
		return numberUserRating;
	}

	public void setNumberUserRating(int numberUserRating) {
		this.numberUserRating = numberUserRating;
	}

	public static double addToAvg(double rating, double avgRating2, int numberofpeople) {
		double sum= (avgRating2*numberofpeople)+rating;
		double n= numberofpeople+1;
		return round(sum/n,2);
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

}
