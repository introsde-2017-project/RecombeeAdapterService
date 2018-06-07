package introsde.project.adopter.recombee.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
public class ItemObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private String itemId;
	private String location;
	private String itemType;

	public ItemObject() {}
	
	public ItemObject(String itemId, String location, String itemType) {
		this.itemId=itemId;
		this.location=location;
		this.itemType=itemType;
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

}
