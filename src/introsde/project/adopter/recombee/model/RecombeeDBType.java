package introsde.project.adopter.recombee.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="RecombeeDBType")
public enum RecombeeDBType implements Serializable{
	foodDB("foodDB"),
	movieDB("movieDB");
	
	private String name;
	
	RecombeeDBType(String name) {
		name=this.name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	
	@Override
	 public String toString() {
	    return name;
	 }
	
	public static List<RecombeeDBType> getAll() {
        return Arrays.asList(RecombeeDBType.values());
	}
	
	public static RecombeeDBType fromString(String text) {
	    for (RecombeeDBType b : RecombeeDBType.values()) {
	      if (b.name.equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    return null;
	  }
}
