package introsde.project.adopter.recombee.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="evaluation")
public class Evaluation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userId;
	private String itemId;
	private double rating;
	private Date time;
	
	public Evaluation() {}
	
	public Evaluation(String userId2, String itemId2, double rating2, Date timestamp) {
		this.userId=userId2;
		this.itemId=itemId2;
		this.rating=rating2;
		this.time=timestamp;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}

}
