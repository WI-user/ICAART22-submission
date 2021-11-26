//Event.java v1.0:

import java.time.LocalDateTime;



public class Event {

	public String eType; // e_type
	public LocalDateTime aTime; // ta
	public Double lLat; // l
	public Double lLng; // l
	public LocalDateTime dTime; // td
	
	//####################################################################################
	// Constructors
	//####################################################################################
	
	public Event(String eType, LocalDateTime aTime, Double lLat, Double lLng, LocalDateTime dTime) {
		this.eType = eType;
		this.aTime = aTime; 
		this.lLat = lLat;
		this.lLng = lLng;
		this.dTime = dTime;
		
		
	}
	
	
	//####################################################################################	
	// Methods
	//####################################################################################	
	
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public String getEType() {
		return eType;
	}
	
	public LocalDateTime getATime() {
		return aTime; 
	}
	
	public Double getLLat() {
		return lLat;
	}
	
	public Double getLLng() {
		return lLng;
	}
	
	public LocalDateTime getDTime() {
		return dTime;
	}
	
	
}
