//####################################################################################
//Intention.java v1.6: translation + code cleaned
//####################################################################################

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import jade.util.leap.ArrayList;
import jade.util.leap.Collection;


public class Intention {
	
 
	public ArrayList intentions = new ArrayList();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	// make it possible to force the outcome of the utility Function
	// contains the predefined values of the debug.java
	public ArrayList forcedUtilities = new ArrayList(); 
	public Integer forcedDecision = 0; // uses random values for the utility function if 0, uses forcedUtilities if 1
	public Integer negotiation = 1; // 1: will enable negotiation, 0: will disable negotiation
	
	
	//####################################################################################
	// Constructors
	//####################################################################################

	
	
	//####################################################################################	
	// Setter
	//####################################################################################
	
		
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public ArrayList getIntentions() { //returns a list of the JobIDs of all Job that are Desired

		return intentions;
	}
	
	public Integer getIntentionsJobID(Integer index) { //returns the JobID of the FilteredTask at index inside of intentions
		return ((FilteredTask) intentions.get(index)).getJobID();
	}
	
	public String getIntentionsJobType(Integer index) { //returns the JobType of the FilteredTask at index inside of intentions

		return ((FilteredTask) intentions.get(index)).getJobType();
	}
	
	//####################################################################################	
	// Methods
	//####################################################################################	
	
	public void newIntention(Integer jobID, String jobType, Double utilityScore) {
		FilteredTask intentionjob = new FilteredTask(jobID, jobType, utilityScore);
		intentions.add(intentionjob);
	}
		
	
	public void removeIntention(Integer JobID) { //removes the intention (named FilteredTask) with the given JobID
		for (int i = 0; i < intentions.size(); i++) {
			if(((FilteredTask) intentions.get(i)).getJobID().equals(JobID)) {
				intentions.remove(i);
			} 
		}
	}
	
	/** Removes Intentions that are not desires
	 * 
	 */
	public void removeNotdesiredIntentions(Desire desire) {
		
		for (int i = 0; i < intentions.size(); i++) { // for every element of intentions
			Integer found = 0;
			Integer tocheck = ((FilteredTask) intentions.get(i)).getJobID(); // check if its value (jobID)
		    for (int j = 0; j < desire.getDesires().size(); j++) { // is inside the list of the Desires
		    	if (tocheck.equals(desire.getDesires().get(j))) { // if yes remove the finished job
		    		found = 1;	
		    	}
		    		
		    	}
		    if (found.equals(0)) {
	    		intentions.remove(i);
	    		i -=1; // by removing a intention the next on will get its old index. without decreasing you could oversee a desire
	    			
		    }
		}
	}
	
	/** 
	 * 
	 */
	
	public Integer addIntentions(Belief belief, Desire desire, Logger logger) {
		Integer intentionadded = 0; // return 1 if new intentions were added while comparing 
		for (int i = 0; i < desire.getDesires().size(); i++) { // for every JobID of the Desires
		    Integer jobIDtocheck = (Integer) desire.getDesires().get(i); // save the JobID to perform a check
		    Integer found = 0;								
		    for (int j = 0; j < intentions.size(); j++) { // check if the JobID is inside of the intentions 
		    	if (jobIDtocheck.equals(((FilteredTask) intentions.get(j)).getJobID())) { 
		    		found = 1;							// set "found = 1" if it is
		    		
		    	}
		    	
		    }
		    if (found.equals(0)) {							// if it was not inside the Intentions, add it
		    	intentions.add(weightedUtilityFunction(jobIDtocheck, belief, desire, intentions, logger)); //the utility function will create the intention
		    	intentionadded = 1;
		    }
		}

		return intentionadded;
	}
	
	
	//remove first add newafterwards
	public Integer updateIntentions(Belief belief, Desire desire, Logger logger) { //updates the intentions given a set of beliefs and desires
		removeNotdesiredIntentions(desire);
		deleteOpenIntention(belief);
		removeChangedIntention(belief);
		
		return addIntentions(belief, desire, logger);
		
	}
	
	
	/** 
	 * removesIntentions with jobType = open and jobProgress = elected so that a new intention can be generated
	 */
	public void deleteOpenIntention(Belief belief) {
		for (int i = 0; i < intentions.size(); i++) {
			String jobType = ((FilteredTask) intentions.get(i)).getJobType();
			if (jobType.contentEquals("open")){
				Integer tocheck = ((FilteredTask) intentions.get(i)).getJobID();
				if (belief.getJobProgressOfJobID(tocheck).equals("accepted")) { 
					intentions.remove(i);
					i-=1;
				}
			}
			
		}	
	}
	
	public void removeChangedIntention(Belief belief) {
		for (int i = 0; i < intentions.size(); i++) {
			String jobType = ((FilteredTask) intentions.get(i)).getJobType();
			if (jobType.contentEquals("other")){
				Integer tocheck = ((FilteredTask) intentions.get(i)).getJobID();
				if (belief.getJobProgressOfJobID(tocheck).equals("noNeighbours")) { 
					intentions.remove(i);
					i-=1;					
				}
			}
		}	
	}

	public Integer getNegotiation(){
		return negotiation;
	}
	
	
	//####################################################################################
	// getUtilityScoreJobs
	//####################################################################################	
	public Double getUtilityScoreJobs(Integer jobIDtocheck, Belief belief, ArrayList intentions, Logger logger) {

		
		double jobPenalty = 1.0;
		LocalDateTime startTimeofCurrentJob = belief.getStartTimeOfJobID(jobIDtocheck);
		Integer lastJobID = jobIDtocheck; // if later is still no chance no other jobs were done
		LocalDateTime lastJobStartTime = LocalDateTime.parse("2016-03-01T00:00:00", formatter); // just a init value, will be overwritten 
		
		LocalDateTime currentTime = LocalDateTime.parse("2000-01-01T00:00:00", formatter);
		if (logger.getSizeOfeEventTrace()!=0){
			currentTime = logger.getDtimeoflastEvent();
		}
		
		Double currentPosLat = belief.getAgentLat();
		Double currentPosLng = belief.getAgentLng();
		Double nextJobStartLat = belief.getStartLatOfJobID(jobIDtocheck);
		Double nextJobStartLng = belief.getStartLngOfJobID(jobIDtocheck);		
		Double speed = belief.getVelocityInKilometersPerHour();
		long travelTime = logger.travelTime(currentPosLat, currentPosLng, nextJobStartLat, nextJobStartLng, speed);
				
		LocalDateTime eta = currentTime.plusSeconds(travelTime);
		
		Double jobScore = 1.0;
		Integer theta = belief.getTheta();
		Integer epsilon = belief.getEpsilon();

		if ((eta.plusMinutes(theta)).isAfter(startTimeofCurrentJob)) {
			jobPenalty = 0.0; //Double.NEGATIVE_INFINITY;
		}
		else if (eta.minusMinutes(theta).isBefore(startTimeofCurrentJob) && startTimeofCurrentJob.isBefore(eta)) {
			jobPenalty = 0.2;
		}
		else if (eta.isBefore(startTimeofCurrentJob) && startTimeofCurrentJob.isBefore(eta.plusMinutes(epsilon))) {
			jobPenalty = 0.6;
		}
		else { //else if ((eta.plusMinutes(epsilon)).isBefore(startTimeofCurrentJob))
			jobPenalty = 1.0;
		}

		return (jobPenalty*jobScore);
	}
	
	//####################################################################################
	// getUtilityScoreBattery
	//####################################################################################	
	public Double getUtilityScoreBattery(Integer jobIDtocheck, Belief belief) {
		/* 
		 * To ensure that the agent does not stop at the customer's location, it should have at least
		 * twice the load of the required distance to the customer, but at least half of the maximum distance.
		 * The output moves in the interval [0.0,1.0].
		 * 
		 * */
		System.out.println("#Status#Agent#" + belief.getAgentID() + ": " + "Calculating Utility");
		Double maximumEnergyConsumption = getMaxBatteryConsumptionJob(belief);
		System.out.println("maximumEnergyConsumption: " + maximumEnergyConsumption.toString());
		Double energyConsumption = getBatteryConsumptionJob(jobIDtocheck, belief);
		System.out.println("energyConsumption: " + energyConsumption.toString());
		Double currentBatteryLoad = belief.getBattery();
		
		//new version 
		double batteryMultiplier1 = 0.1;
		double batteryMultiplier2 = 0.75;
		double batteryMultiplier3 = 1.0;
		Double upperLimit = 80.0;
		Double lowerLimit = 30.0;
		
		if (energyConsumption >= currentBatteryLoad) {
			return Double.NEGATIVE_INFINITY;
		}
		else if (currentBatteryLoad>upperLimit) {
			return (batteryMultiplier1*(maximumEnergyConsumption - energyConsumption) / maximumEnergyConsumption);
		}
		else if (currentBatteryLoad>=lowerLimit) {
			return (batteryMultiplier2*(maximumEnergyConsumption - energyConsumption) / maximumEnergyConsumption);
		}
		else { // (currentBatteryLoad<lowerLimit)
			return (batteryMultiplier3*(maximumEnergyConsumption - energyConsumption) / maximumEnergyConsumption);
		}
		

	}
	
	//####################################################################################
	// getMaxDistanceToCustomer
	//####################################################################################	
	public Double getMaxDistanceToCustomer(Belief belief) {
		/* Calculates the maximum distance a customer could be away: 
		 * the respective longer distance to the area boundary in x- and y-direction
		 */
		Double agentLat  = belief.getAgentLat();
		Double agentLng  = belief.getAgentLng();
		Double left = belief.mapArea.getLeftBorder();
		Double right = belief.mapArea.getRightBorder();
		Double top = belief.mapArea.getTopBorder();
		Double bottom = belief.mapArea.getBottomBorder();		
		Double distanceToLeft = Math.abs(agentLng - left);
		Double distanceToRight = Math.abs(agentLng - right);
		Double distanceToTop = Math.abs(agentLat - top);
		Double distanceToBottom = Math.abs(agentLat - bottom);
		Double cornerLng = right;
		if (distanceToLeft > distanceToRight) {
			cornerLng = left;
		}
		Double cornerLat = top;
		if (distanceToBottom > distanceToTop) {
			cornerLat = bottom;
		}
		
		Double distance = getDistanceInMeters(agentLng, agentLat, cornerLng, cornerLat);
			
		
		return distance;			
	}
	
	//####################################################################################
	// getMaxBatteryConsumptionJob
	//####################################################################################	
	public Double getMaxBatteryConsumptionJob(Belief belief) {
		/* Calculates the maximum distance a customer could be away:
		 * a triangle through the rectangle (diagonal + along the edge)
		 */
		Double left = belief.mapArea.getLeftBorder();
		Double right = belief.mapArea.getRightBorder();
		Double top = belief.mapArea.getTopBorder();
		Double bottom = belief.mapArea.getBottomBorder();
			
		Double distanceTopLeftToBottomRight = getDistanceInMeters(top, left, bottom, right);
		Double distanceBottomLeftToTopRight = getDistanceInMeters(bottom, left, top, right);
		Double maxDistance = 0.0;
		//Diagonal selection
		if (distanceTopLeftToBottomRight > distanceBottomLeftToTopRight) {
			maxDistance = distanceTopLeftToBottomRight;
			//Lower or upper triangle selection
			
			Double distanceTopLeftToTopRight = getDistanceInMeters(top, left, top, right);
			Double distanceBottomRightToBottomLeft = getDistanceInMeters(bottom, right, bottom, left);
			Double distanceBottomLeftToTopLeft = getDistanceInMeters(bottom, left, top, left);
			Double distanceBottomRightToTopRight = getDistanceInMeters(bottom, right, top, right);
			
			maxDistance += Math.max(distanceTopLeftToTopRight + distanceBottomRightToTopRight, distanceBottomLeftToTopLeft + distanceBottomRightToBottomLeft);
		}
		else {
			maxDistance = distanceBottomLeftToTopRight;
			//Lower or upper triangle selection
			
			Double distanceTopLeftToTopRight = getDistanceInMeters(top, left, top, right);
			Double distanceBottomRightToBottomLeft = getDistanceInMeters(bottom, right, bottom, left);
			Double distanceBottomLeftToTopLeft = getDistanceInMeters(bottom, left, top, left);
			Double distanceBottomRightToTopRight = getDistanceInMeters(bottom, right, top, right);			
			maxDistance += Math.max(distanceBottomRightToBottomLeft + distanceBottomRightToTopRight, distanceBottomLeftToTopLeft + distanceTopLeftToTopRight);
		
		}
		Double maxDistanceInKilometers = maxDistance / 1000.0; //Annahme pro Laengen- bzw. Breitengrad Abstand sind es 111 km (https://www.thoughtco.com/degree-of-latitude-and-longitude-distance-4070616)
		
		Double timeNeededInHours = maxDistanceInKilometers / belief.velocityInKilometersPerHour;
		Double batteryConsumptionMaxDistance = belief.energyConsumptionPerHour * timeNeededInHours;
		Double currentBatteryLoad = belief.getBattery();
		/* Maximum consumption is the distance we can travel with our charge level 
		 * or the consumption we would have if we had to drive to the furthest point and back.
		 */
		Double maxBatteryConsumptionJob = Math.min(currentBatteryLoad, batteryConsumptionMaxDistance);

		return maxBatteryConsumptionJob;			
	}
	
	//####################################################################################
	// getBatteryConsumptionJob
	//####################################################################################	
	public Double getBatteryConsumptionJob(Integer jobIDtocheck, Belief belief) {
		/* Calculates the maximum distance a customer could be away: 
		 * the respective longer distance to the area boundary in x- and y-direction
		 */
		// from the corner to the diagonally opposite corner and back again.
		Double distanceJob = getDistanceJob(jobIDtocheck, belief);
		Double distanceReturn = getDistancePositionToGoal(jobIDtocheck, belief);
		Double distance = distanceJob + distanceReturn;
		Double distanceInKilometers = distance / 1000.0; //Assuming a distance of 111 km per degree of latitude or longitude. 
		Double timeNeededInHours = distanceInKilometers / belief.velocityInKilometersPerHour;
		Double batteryConsumptionJob = belief.energyConsumptionPerHour * timeNeededInHours;  // speed * time
		System.out.println("energyConsumptionPerHour=" + belief.energyConsumptionPerHour);
		return batteryConsumptionJob;			
	}
	
	//####################################################################################
	// getBatteryConsumptionDrive
	//####################################################################################	
	public Double getBatteryConsumptionDrive(Integer jobIDtocheck, Belief belief) {
		/* 
		 * 
		 */
		// from the corner to the diagonally opposite corner and back again.
		Double distanceJob = getDistanceJob(jobIDtocheck, belief);

		Double distance = distanceJob; //+ distanceReturn;
		Double distanceInKilometers = distance / 1000.0; //Assuming a distance of 111 km per degree of latitude or longitude. 
		Double timeNeededInHours = distanceInKilometers / belief.velocityInKilometersPerHour;
		Double batteryConsumptionJob = belief.energyConsumptionPerHour * timeNeededInHours;

		return batteryConsumptionJob;			
	}
	//####################################################################################
	// getMaxDistanceJob
	//####################################################################################	
	public Double getMaxDistanceJob(Belief belief) {
		/* Calculates the maximum distance that would have to be covered for an order:
		 * the maximum possible distance to the customer + the diagonal of the envelope rectangle.
		 */
		Double distance = getMaxDistanceToCustomer(belief);
		//left right is lon
		//top down is lat
		Double left = belief.mapArea.getLeftBorder();
		Double right = belief.mapArea.getRightBorder();
		Double top = belief.mapArea.getTopBorder();
		Double bottom = belief.mapArea.getBottomBorder();
		Double distanceTopLeftToBottomRight  = getDistanceInMeters(top, left, bottom, right);
		Double distanceBottoLeftToTopRight  = getDistanceInMeters(bottom, left, top, right);
		Double maxDiagonale = Math.max(distanceTopLeftToBottomRight, distanceBottoLeftToTopRight);
		Double maxDistance = distance + maxDiagonale;
		return maxDistance;	
	}
	
	//####################################################################################
	// getDistanceJob
	//####################################################################################		
	public Double getDistanceJob(Integer jobIDtocheck, Belief belief) {
		// Calculates the distance to the destination
		Double agentLat  = belief.getAgentLat();
		Double agentLng  = belief.getAgentLng();
		Double goalLatStart = belief.getStartLatOfJobID(jobIDtocheck);
		Double goalLngStart = belief.getStartLngOfJobID(jobIDtocheck);
		Double goalLatEnd = belief.getEndLatOfJobID(jobIDtocheck);
		Double goalLngEnd = belief.getEndLngOfJobID(jobIDtocheck);
		Double distanceFromMyPositionToStart = getDistanceInMeters(goalLngStart, goalLatStart, agentLng, agentLat);
		Double distanceFromStartToGoal = getDistanceInMeters(goalLngStart, goalLatStart, goalLngEnd, goalLatEnd);
		Double distanceFromMyPositionTotal = distanceFromMyPositionToStart + distanceFromStartToGoal;
		return distanceFromMyPositionTotal;			
	}
	
	//####################################################################################
	// getDistanceInKilometers
	//####################################################################################		
	public Double getDistanceInKilometers(Double utmLongitudePointA, Double utmLatitudePointA, Double utmLongitudePointB, Double utmLatitudePointB) {
		return getDistanceInMeters(utmLongitudePointA, utmLatitudePointA, utmLongitudePointB, utmLatitudePointB) / 1000.0;
	}
	
	//####################################################################################
	// getDistanceInMeters
	//####################################################################################		
	public Double getDistanceInMeters(Double utmLongitudePointA, Double utmLatitudePointA, Double utmLongitudePointB, Double utmLatitudePointB) {
		
		return getEuclidianDistance(utmLongitudePointA, utmLatitudePointA, utmLongitudePointB, utmLatitudePointB);
	}

	//####################################################################################
	// getEuclidianDistance
	//####################################################################################	
	public Double getEuclidianDistance(Double longitudePointA, Double latitudePointA, Double longitudePointB, Double latitudePointB) {
		
		String templongA = String.valueOf(longitudePointA);
		String templongB = String.valueOf(longitudePointB);
		String templatA = String.valueOf(latitudePointA);
		String templatB = String.valueOf(latitudePointB);
		
		
		Integer switchA = 0;
		Integer switchB = 0;
		
		if (templongA.startsWith("3.2") == true) {
		
		String temp2longA = "0.0" + templongA.substring(3);
		
		Double corrlongitudePointA = Double.parseDouble(temp2longA);
		
		longitudePointA = corrlongitudePointA;
		
		switchA = 1;
		
		}
		
		if(templongB.startsWith("3.2") == true) {
		
		String temp2longB = "0.0" + templongB.substring(3);
		
		Double corrlongitudePointB = Double.parseDouble(temp2longB);
		
		longitudePointB = corrlongitudePointB;
		
		switchB = 1;
		
		
		}
		
		if(templatA.startsWith("3.2") == true) {

		String temp2latA = "0.0" + templatA.substring(3);
		
		Double corrlatitudePointA = Double.parseDouble(temp2latA);
		
		latitudePointA = corrlatitudePointA;
		

		
		}
		
		if (templatB.startsWith("3.2") == true) {
		
		String temp2latB = "0.0" + templatB.substring(3);
		
		Double corrlatitudePointB = Double.parseDouble(temp2latB);
		
		latitudePointB = corrlatitudePointB;
		
		}
		
		if (switchA == 1) {
			
		Double tlatA = latitudePointA;
		
		latitudePointA = longitudePointA;
		
		longitudePointA = tlatA;
	    
		}
		
		if (switchB == 1) {
			
			Double tlatB = latitudePointB;
			
			latitudePointB = longitudePointB;
			
			longitudePointB = tlatB;
			
		}
		
		Double distance = Math.sqrt( ( Math.pow( (latitudePointA - latitudePointB), 2) + Math.pow( (longitudePointA - longitudePointB), 2) ) );

		
		return distance;
	}

	//####################################################################################
	// getDistanceToCustomer
	//####################################################################################		
	public Double getDistanceToCustomer(Integer jobIDtocheck, Belief belief) {

		Double agentLat  = belief.getAgentLat();
		Double agentLng  = belief.getAgentLng();
		Double goalLatStart = belief.getStartLatOfJobID(jobIDtocheck);
		Double goalLngStart = belief.getStartLngOfJobID(jobIDtocheck);
		Double distanceFromMyPositionToStart = getDistanceInMeters(goalLngStart, goalLatStart, agentLng, agentLat);
		
		return distanceFromMyPositionToStart;			
	}
	
	//####################################################################################
	// getDistancePositionToGoal
	//####################################################################################		
	public Double getDistancePositionToGoal(Integer jobIDtocheck, Belief belief) {
		// Calculates the distance for the return path
		Double agentLat  = belief.getAgentLat();
		Double agentLng  = belief.getAgentLng();
		Double goalLatEnd = belief.getEndLatOfJobID(jobIDtocheck);
		Double goalLngEnd = belief.getEndLngOfJobID(jobIDtocheck);
		Double distanceFromPositionToGoal = getDistanceInMeters(goalLngEnd, goalLatEnd, agentLng, agentLat);
		return distanceFromPositionToGoal;			
	}
	
	//####################################################################################
	// getUtilityScoreDistanceToCustomer
	//####################################################################################		
	public Double getUtilityScoreDistanceToCustomer(Integer jobIDtocheck, Belief belief) {
		/* The benefit decreases with the distance to be covered until the order is accepted.
		 * The distance is considered in relation to the distance to the farthest
		 * border of the order area. The output is in the interval [0.0,1.0].
		 */
		Double maxDistance  = getMaxDistanceToCustomer(belief);
		Double distance = getDistanceToCustomer(jobIDtocheck , belief);	
		return ((maxDistance - distance) / (maxDistance));
	}

	//####################################################################################
	// weightedUtilityScore
	//####################################################################################	
	public Double weightedUtilityScore(Integer jobIDtocheck, Belief belief, Desire desire, ArrayList intentions, Logger logger) {
		Double distanceWeight = belief.getDistanceWeight();
		Double batteryWeight = belief.getBatteryWeight();
		Double jobWeight = belief.getJobWeight();
		Double batteryScore = 0.0;
		if (batteryWeight != 0.0) {
			batteryScore = batteryWeight * getUtilityScoreBattery(jobIDtocheck, belief);
		}
		Double distanceScore = distanceWeight * getUtilityScoreDistanceToCustomer(jobIDtocheck, belief);
		System.out.println("distanceWeight=" + distanceWeight);
		System.out.println("batteryWeight=" + batteryWeight);
		System.out.println("jobweight=" + jobWeight);
		System.out.println("getUtilityScoreDistanceToCustomer=" + getUtilityScoreDistanceToCustomer(jobIDtocheck, belief));
		System.out.println("getUtilityScoreBattery=" + getUtilityScoreBattery(jobIDtocheck, belief));
		System.out.println("getUtilityScoreJobs=" + getUtilityScoreJobs(jobIDtocheck, belief, intentions, logger));
		
		Double jobScore = jobWeight * getUtilityScoreJobs(jobIDtocheck, belief, intentions, logger);
		String output = "Agent" + belief.getAgentID() +":\n";
		output += "JobScore: " + jobScore.toString() +"\n";
		output += "DistanceScore: " + distanceScore.toString() +"\n";
		output += "BatteryScore: " + batteryScore.toString();
		System.out.println(output);
		Double utilityScore = jobScore + distanceScore + batteryScore;
		return utilityScore;
	}	
	
	//####################################################################################
	// weightedUtilityFunction
	//####################################################################################	
	public FilteredTask weightedUtilityFunction(Integer jobIDtocheck, Belief belief, Desire desire, ArrayList intentions, Logger logger) {
		Integer jobID = jobIDtocheck;
		String jobType = "self"; // self or other
		Double utilityScore = weightedUtilityScore(jobIDtocheck, belief, desire, intentions, logger);
		Double ownBid = 0.0;
		
				
		if ((utilityScore <=50) && negotiation == 1)  { //&& negotiation == 1)   
			jobType = "other"; // self or other
			
		}
		if ((utilityScore == Double.NEGATIVE_INFINITY) && negotiation == 1)  { //&& negotiation == 1)   
			System.out.println("infitest");
			jobType = "other"; // self or other
			
		}
		
		//}
		FilteredTask newFilteredTask = new FilteredTask(jobID, jobType, utilityScore, ownBid);
		//if (forcedDecision == 1) {
		if (forcedDecision == 1 && forcedUtilityExists(jobID)) {
			//System.out.println("forcedDecision");
			newFilteredTask = getForcedUtility(jobID);
		}	
			if ((belief.getJobProgressOfJobID(jobID)).contentEquals("callForProposals")) {
				Double bid = utilityScore;
				if (forcedDecision == 1) {
					//bid = 66.6;			
				}
				System.out.println("JobType: " + "open");
				newFilteredTask = new FilteredTask(jobID, "open" , 0.0 , bid); //calculate a real bid insted pof 66.6		
				//if (forcedDecision == 1) {
					//newFilteredTask = getForcedUtility(jobID);
				//}	
			}
			else if ((belief.getJobProgressOfJobID(jobID)).contentEquals("accepted")) {
				System.out.println("JobType: " + "self");
				newFilteredTask = new FilteredTask(jobID, "self" , utilityScore, 0.0); //calculate a real bid insted pof 66.6
			}
			
			else {}
		String output = "Agent" + belief.getAgentID() +":\n";
		output += "Job: " + jobIDtocheck.toString() + "; JobType: " + jobType + "; Score: "+ utilityScore.toString();
		System.out.println(output);		
		for (int i = 0; i < belief.assignments.size(); i++) {
			if((((Job) belief.assignments.get(i))).getJobID()==jobID) {
				(((Job) belief.assignments.get(i))).setBestScore(utilityScore);
				(((Job) belief.assignments.get(i))).setBestID(belief.agentID);
			}
		}
		return newFilteredTask;
	}			
	
	
	//####################################################################################	
	// forcedUtility methods
	//####################################################################################
	
	public void setForcedDecision(Integer forcedDecision) {
		this.forcedDecision = forcedDecision;
	}
	
	public void setNegotiation(Integer negotiation) {
		this.negotiation = negotiation;
	}
	
	
	public Double getRandomScore() {
		Random rnd = new Random();
		Double randomScore = 1.0 + rnd.nextInt(99);
		return randomScore;
	}
	
	
	public void addForcedUtility(Integer jobID, String jobType, Double utilityScore, Double ownBid) {
		
		
		
		FilteredTask forcedUtility = new FilteredTask(jobID, jobType, utilityScore, ownBid);
		forcedUtilities.add(forcedUtility);
	}
	
	public Boolean forcedUtilityExists(Integer jobID) {
		for (int i = 0; i < forcedUtilities.size(); i++) {
			Integer forcedJobID = ((FilteredTask) forcedUtilities.get(i)).getJobID();
			if (jobID == forcedJobID) {
				return true;	
			}
		}
		return false;
	}
	
	public FilteredTask getForcedUtility(Integer jobID) {
		
		
		//FilteredTask(Integer jobID, String jobType, Double utilityScore)
		FilteredTask forcedFilteredTask = new FilteredTask(0, "not found", 0.0, 0.0);
		for (int i = 0; i < forcedUtilities.size(); i++) {
			Integer forcedJobID = ((FilteredTask) forcedUtilities.get(i)).getJobID();
			if (jobID == forcedJobID) {
				String forcedJobType = ((FilteredTask) forcedUtilities.get(i)).getJobType();
				Double forcedUtilityScore = ((FilteredTask) forcedUtilities.get(i)).getUtilityScore();
				
				forcedFilteredTask = ((FilteredTask) forcedUtilities.get(i));
				
				
				
			}
			
		}
		
		return forcedFilteredTask;
		
	}
	
	
	
	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String beliefVariables = "Intention | \n"; 

				//+ "###########################################################";
			
		String jobsInAssignments = "|    jobID  |  jobType  | utilityScore |  ownBid |";
				for (int i = 0; i < intentions.size(); i++) {
					jobsInAssignments = jobsInAssignments + intentions.get(i);
				}
		jobsInAssignments += "\n___________________________________________________________";
		
		String output = beliefVariables + jobsInAssignments; 
		
		   return output ;
		}     

}


