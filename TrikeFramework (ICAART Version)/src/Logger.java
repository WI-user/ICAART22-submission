//####################################################################################
//Logger.java v1.1: translation + code cleaned
//####################################################################################

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jade.util.leap.ArrayList;
public class Logger {
public ArrayList tripPlan; // w
public ArrayList eEventTrace;
public Integer theta; //todo load from debug or somewhere else
public Double otd; // overall travel distance
public Double odr; // order dropout rate
public Integer translationCounter = 0; // 
public Integer tripsLost = 0;
public Integer tripsServed = 0;



DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

public Logger(Integer initTheta) {
	tripPlan = new ArrayList();
	eEventTrace = new ArrayList();
	otd = 0.0; // overall travel distance
	odr = 0.0; // order dropout rate
	theta = initTheta;
}

public void newTripRequest(Integer tripID, String trType, LocalDateTime vaTime, Double lStartLat, Double lStartLng, Double lEndLat, Double lEndLng) {
	TripRequest loggerTripRequest = new TripRequest(tripID, trType, vaTime, lStartLat, lStartLng, lEndLat, lEndLng);
	tripPlan.add(loggerTripRequest);
}

public void newEvent(String eType, LocalDateTime aTime, Double lLat, Double lLng, LocalDateTime dTime) {
	Event loggerEvent = new Event(eType, aTime, lLat, lLng, dTime);
	eEventTrace.add(loggerEvent);

}


//####################################################################################	
// Methods for tripPlan
//####################################################################################
	public LocalDateTime getDtimeoflastEvent() {
		LocalDateTime lastDeparture = ((Event) eEventTrace.get(eEventTrace.size()-1)).getDTime();
		return lastDeparture;
	}
	
	public Integer getSizeOfeEventTrace() {
		return eEventTrace.size();
	}

	//returns the tripID of an Entry (element) of tripPlan
	public Integer getTripIDOfTripPlan(Integer tripPlanElement) {
		Integer tripID = ((TripRequest) tripPlan.get(tripPlanElement)).getTripID();
		return tripID;
	}
	
	public String getTrTypeOfTripPlan(Integer tripPlanElement) {
		String tripID = ((TripRequest) tripPlan.get(tripPlanElement)).getTrType();
		return tripID;
	}
	
	public LocalDateTime getVATimeOfTripPlan(Integer tripPlanElement) {
		System.out.println("tripPlanElement = " + tripPlanElement);
		LocalDateTime vaTime = ((TripRequest) tripPlan.get(tripPlanElement)).getVATime();
		System.out.println("vaTime = " + vaTime);
		return vaTime;
	}
	
	public Double getLStartLatOfTripPlan(Integer tripPlanElement) {
		Double lStartLat = ((TripRequest) tripPlan.get(tripPlanElement)).getLStartLat();
		return lStartLat;
	}
	
	public Double getLStartLngOfTripPlan(Integer tripPlanElement) {
		Double lStartLng = ((TripRequest) tripPlan.get(tripPlanElement)).getLStartLng();
		return lStartLng;
	}

	public Double getLEndLatOfTripPlan(Integer tripPlanElement) {
		Double lEndLat = ((TripRequest) tripPlan.get(tripPlanElement)).getLEndLat();
		return lEndLat;
	}
	
	public Double getLEndLngOfTripPlan(Integer tripPlanElement) {
		Double lEndLng = ((TripRequest) tripPlan.get(tripPlanElement)).getLEndLng();
		return lEndLng;
	}

//####################################################################################	
// Methods for eEventTrace
//####################################################################################
	
	public String getETypeOfEEventTrace(Integer eEventTraceElement) {
		String eType = ((Event) eEventTrace.get(eEventTraceElement)).getEType();
		return eType;
	}
	public LocalDateTime getATimeOfEEventTrace(Integer eEventTraceElement) {
		LocalDateTime aTime = ((Event) eEventTrace.get(eEventTraceElement)).getATime();
		return aTime;
	}
	
	public Double getLLatOfEEventTrace(Integer eEventTraceElement) {
		Double lEndLat = ((Event) eEventTrace.get(eEventTraceElement)).getLLat();
		return lEndLat;
	}
	
	public Double getLLngOfEEventTrace(Integer eEventTraceElement) {
		Double lEndLng = ((Event) eEventTrace.get(eEventTraceElement)).getLLng();
		return lEndLng;
	}
	
	public LocalDateTime getDTimeOfEEventTrace(Integer eEventTraceElement) {
		LocalDateTime dTime = ((Event) eEventTrace.get(eEventTraceElement)).getDTime();
		return dTime;
	}

	public void setTheta(Integer readTheta) {
		this.theta = readTheta;
		
	}
	
//####################################################################################	
// other methods
//####################################################################################	
	
	public Double euclid(Double Pos1Lat, Double Pos1Lang, Double Pos2Lat, Double Pos2Lang) {
		Double distance = Math.sqrt( ( Math.pow( (Pos1Lat - Pos2Lat), 2) + Math.pow( (Pos1Lang - Pos2Lang), 2) ) );
		return distance;
	}
	
	public long travelTime(Double Pos1Lat, Double Pos1Lng, Double Pos2Lat, Double Pos2Lng, Double speed) {
		Double distance = euclid(Pos1Lat, Pos1Lng, Pos2Lat, Pos2Lng);
		//Double speed = 3.6; // km/h
		//System.out.println("Inside travelTime distance = " + distance);
		//Double traveltimeD = speed/distance;
		//System.out.println("Inside travelTime traveltimeD = " + traveltimeD);
		long traveltimeL = (long) Math.round(distance/speed) ; //seconds
		//System.out.println("Inside travelTime traveltimeL = " + traveltimeL);
		if (distance == 0.0) {
			traveltimeL = 0;
		}
		
		//System.out.println("calculated traveltimeL = " + traveltimeL);
		return traveltimeL;
	}
	
//####################################################################################	
// PAAMS21 paper "pseudocode"
//####################################################################################


public Integer translatePlanToTripPlan(Belief belief, Plan actPlan) {
	Double speed = belief.getVelocityInKilometersPerHour();
	Integer tripID0 = actPlan.getJobID();
	String trType0 = "CUSTOMER_TRIP"; // todo make it possible to detect refill trips
	
	LocalDateTime vaTime0 = belief.getStartTimeOfJobID(tripID0);
	Double lStartLat0 = actPlan.getStartLat();
	Double lStartLng0 = actPlan.getStartLng();
	Double lEndLat0 = actPlan.getEndLat();
	Double lEndLng0 = actPlan.getEndLng();
	newTripRequest(tripID0, trType0, vaTime0, lStartLat0, lStartLng0, lEndLat0, lEndLng0);
	
	Integer tripWithCustomer = 0;
	
	if (translationCounter == 0) {
		String eTypeInit = "NONE";
		LocalDateTime aTimeInit = LocalDateTime.parse("2016-03-01T00:00:00", formatter); //init with a date before the first trip
		Double lLatInit = belief.getAgentLat();
		Double lLngInit = belief.getAgentLng();
		LocalDateTime dTimeInit = aTimeInit; // is equal for first event
		newEvent(eTypeInit, aTimeInit, lLatInit, lLngInit, dTimeInit);
	}
	//Integer n = 0;
	for (int j = translationCounter ; j < tripPlan.size(); j++) {
		tripsServed = tripsServed + 1;  
		String type = getTrTypeOfTripPlan(j);
		 switch (type) {
		 case "CUSTOMER_TRIP":
			Double lLat = getLStartLatOfTripPlan(j);
			Double lLng = getLStartLngOfTripPlan(j);
			Double lLatEvent = getLLatOfEEventTrace(eEventTrace.size()-1);
			Double lLngEvent = getLLngOfEEventTrace(eEventTrace.size()-1);
			long travelTimeSec = travelTime(lLat, lLng, lLatEvent, lLngEvent, speed);
			LocalDateTime eta = getDTimeOfEEventTrace(eEventTrace.size()-1).plusSeconds(travelTimeSec);

			if ((eta.isBefore(getVATimeOfTripPlan(j).plusMinutes(theta))) || eta.equals(getVATimeOfTripPlan(j).plusMinutes(theta))){
		
				tripWithCustomer = 1;
				LocalDateTime etd = eta;
				if (eta.isBefore(getVATimeOfTripPlan(j))) {
					etd = getVATimeOfTripPlan(j); // arrived too early
				} else {
					// before if clause 
				}
				newEvent("PICKUP", eta, lLat, lLng, etd);
				Double lEndLat = getLStartLatOfTripPlan(j);
				Double lEndLng = getLStartLngOfTripPlan(j);
				long travelTimeSec2 = travelTime(lLat, lLng, lEndLat, lEndLng, speed);
				eta = etd.plusSeconds(travelTimeSec2); 
				newEvent("DROP", eta, lEndLat, lEndLng, eta);
			} else{

				newEvent("PASS_BY", eta, lLat, lLng, eta);

			}
			
			break;
			 
		 case "REFILL_TRIP":
			 //todo
			 break;
			 
		 }
		 
		 translationCounter += 1;

		 
	}
	
	return tripWithCustomer;
}

public void Evaluation() {
	otd = 0.0; // reset former value
	odr = 0.0;
	tripsLost = 0;
	tripsServed = 0;
	

if (eEventTrace.size() > 0){
	
	for (int i = 0; i < eEventTrace.size()-1; i++ ){
	
		Double Pos1Lat = ((Event) eEventTrace.get(i)).getLLat();
		Double Pos1Lng = ((Event) eEventTrace.get(i)).getLLng();
		Double Pos2Lat = ((Event) eEventTrace.get(i+1)).getLLat();
		Double Pos2Lng = ((Event) eEventTrace.get(i+1)).getLLng();
		otd += euclid(Pos1Lat, Pos1Lng, Pos2Lat, Pos2Lng);
	}
	
	for (int i = 0; i < eEventTrace.size(); i++ ){
		if (getETypeOfEEventTrace(i).contentEquals("PASS_BY")){
			tripsLost += 1;
			tripsServed += 1;
		}
		else if (getETypeOfEEventTrace(i).contentEquals("DROP")){
			tripsServed += 1;
		}
		
	}
}

	
	if (tripsServed > 0) {
	odr = ((double) tripsLost/(double) tripsServed);
	}
	
}

//####################################################################################	
// Output Methods
//####################################################################################

public void printTripPlanTrace() {
	System.out.println("\n" + "tripID | trType | vaTime | lStartLat | lStartLng |  lEndLat | lEndLng    ");
	for (int i = 0; i < tripPlan.size(); i++) {
		TripRequest tempTripRequest = (TripRequest) tripPlan.get(i);
		Integer tripID = tempTripRequest.getTripID();
		String trType = tempTripRequest.getTrType();
		LocalDateTime vaTime = tempTripRequest.getVATime();
		Double lStartLat = tempTripRequest.getLStartLat();
		Double lStartLng = tempTripRequest.getLStartLng();
		Double lEndLat = tempTripRequest.getLEndLat();
		Double lEndLng = tempTripRequest.getLEndLng();
		
		System.out.println(tripID + "|" + trType + "|" + vaTime + "|" + lStartLat + "|" + lStartLng + "|" + lEndLat + "|" + lEndLng );
		
	}
	System.out.println("\n");
}

public void printEEventTrace() {
	System.out.println("\n" + "eType |     aTime     | lLat | lLng |     dTime    ");
	for (int i = 0; i < eEventTrace.size(); i++) {
		Event tempEvent = (Event) eEventTrace.get(i);
		String eType = tempEvent.getEType();
		LocalDateTime aTime = tempEvent.getATime();
		Double lLat = tempEvent.getLLat();
		Double lLng = tempEvent.getLLng();
		LocalDateTime dTime = tempEvent.getDTime();
		System.out.println(eType + " | " + aTime + " | " + lLat + " | " + lLng + " | " + dTime);
		
	}
	System.out.println("\n");
}



public void writeResults(Integer AgentID, Integer negotiation) {
	//====================================================================================
	// write tripPlanget
	//==================================================================================== 
	try (PrintWriter writer = new PrintWriter(new File("Agent#"+ AgentID +"#tripPlan" + "#negotiation" + negotiation + "#theta" + theta + ".csv"))) {
	      StringBuilder sb = new StringBuilder();
	      for (int i=0; i < tripPlan.size(); i++) {
	    	  
	    	  sb.append(getTripIDOfTripPlan(i));
	    	  sb.append(';');
	    	  sb.append(getTrTypeOfTripPlan(i));
	    	  sb.append(';');
	    	  sb.append(getVATimeOfTripPlan(i));
		      sb.append(';');
		      sb.append(getLStartLatOfTripPlan(i));
		      sb.append(";");
		      sb.append(getLStartLngOfTripPlan(i));
		      sb.append(';');
		      sb.append(getLEndLatOfTripPlan(i));
		      sb.append(";");
		      sb.append(getLEndLngOfTripPlan(i));
		      sb.append(";");
		      		 
		      sb.append('\n');
	      }

	      writer.write(sb.toString());



		 } catch (FileNotFoundException e) {
		   System.out.println(e.getMessage());
		 }	
	//====================================================================================
	// write eEventTrace
	//==================================================================================== 
	try (PrintWriter writer = new PrintWriter(new File("Agent#"+ AgentID +"#EEventTrace" + "#negotiation" + negotiation + "#theta" + theta + ".csv"))) {

	      StringBuilder sb = new StringBuilder();
	      for (int i=0; i < eEventTrace.size(); i++) {
	    	  sb.append(getETypeOfEEventTrace(i));
	    	  sb.append(';');
	    	  sb.append(getATimeOfEEventTrace(i));
	    	  sb.append(';');
	    	  sb.append(getLLatOfEEventTrace(i));
		      sb.append(';');
		      sb.append(getLLngOfEEventTrace(i));
		      sb.append(";");
		      sb.append(getDTimeOfEEventTrace(i));
		      sb.append(";");
		      sb.append('\n');
	      }


	      writer.write(sb.toString());


	    } catch (FileNotFoundException e) {
	      System.out.println(e.getMessage());
	    }
	//====================================================================================
	// write Evaluation
	//====================================================================================
	try (PrintWriter writer = new PrintWriter(new File("Agent#"+ AgentID +"#Evaluation" + "#negotiation" + negotiation + "#theta" + theta + ".csv"))) {

	      StringBuilder sb = new StringBuilder();
	      sb.append("otd");
	      sb.append(';');
	      sb.append("odr");
	      sb.append(';');
	      sb.append("tripsLost");
	      sb.append(';');
	      sb.append("tripsServed");
	      sb.append('\n');

	      sb.append(otd);
	      sb.append(';');
	      sb.append(odr);
	      sb.append(';');
	      sb.append(tripsLost);
	      sb.append(';');
	      sb.append(tripsServed);
	      sb.append('\n');

	      writer.write(sb.toString());

	    } catch (FileNotFoundException e) {
	      System.out.println(e.getMessage());
	    }	 
	//====================================================================================
	 
	 
	 
	
}

}