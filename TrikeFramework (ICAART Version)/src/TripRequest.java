//TripRequest.java v1.1: translation + code cleaned
	
import java.time.LocalDateTime;

public class TripRequest {

	public Integer tripID; //id
	public String trType; // tr_type
	public LocalDateTime vaTime; // VATime
	public Double lStartLat; // l_start
	public Double lStartLng; // 
	public Double lEndLat; // l_end
	public Double lEndLng; // l
	
	//####################################################################################
	// Constructors
	//####################################################################################
	
	// for direct input
	public TripRequest(Integer tripID, String trType, LocalDateTime vaTime, Double lStartLat, Double lStartLng, Double lEndLat, Double lEndLng) {
		this.tripID = tripID;
		this.trType = trType;
		this.vaTime = vaTime;
		this.lStartLat = lStartLat;
		this.lStartLng = lStartLng;
		this.lEndLat = lEndLat;
		this.lEndLng = lEndLng;
		
	}
		
	
	//####################################################################################	
	// Methods
	//####################################################################################	
	
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public Integer getTripID() {
		return tripID;
	}
	
	public String getTrType() {
		return trType;
	}
	
	public LocalDateTime getVATime() {
		return vaTime; 
	}
	
	public Double getLStartLat() {
		return lStartLat;
	}
	
	public Double getLStartLng() {
		return lStartLng;
	}
	
	public Double getLEndLat() {
		return lEndLat;
	}
	
	public Double getLEndLng() {
		return lEndLng;
	}
		
	
	
	
}
