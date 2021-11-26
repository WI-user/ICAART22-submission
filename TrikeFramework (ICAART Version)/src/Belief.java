//####################################################################################
//Belief.java v1.8: translation + code cleaned
//####################################################################################


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jade.util.leap.ArrayList;

public class Belief {
	
	public Integer agentID;
	public Double agentLat; 
	public Double agentLng;
	public Double battery;
	public Integer driving; 
	public ArrayList assignments;
	public ArrayList neighbours;
	public MapArea mapArea;
	public Double maxBatteryCharge;
	public Double energyConsumptionPerHour;
	public Double energyConsumptionPerMillisecond;
	public Double velocityInKilometersPerHour = 3.6;
	public Integer theta = 4; // have to be changed in debug.java and here!
	public Integer epsilon = 10;
	public Double jobWeight;
	public Double batteryWeight;
	public Double distanceWeight;
	public LocalDateTime vehicleTime;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	//####################################################################################
	// Constructors
	//####################################################################################
	public Belief(Integer agentID, Double agentLat, Double agentLng, Double battery, Integer driving) {
		this.agentID = agentID;
		this.agentLat = agentLat;
		this.agentLng = agentLng;
		this.battery = battery;
		this.driving = driving;
		this.maxBatteryCharge = 0.0;
		this.energyConsumptionPerHour = 100.0;
		this.energyConsumptionPerMillisecond = energyConsumptionPerHour / 3600 / 1000;
		assignments = new ArrayList();
		neighbours = new ArrayList();
		this.mapArea = new MapArea();
		this.jobWeight = 100.0/3;
		this.batteryWeight = 100.0/3;
		this.distanceWeight = 100.0/3;
		String stringVehicleTime = "2000-01-01T00:00:00"; // init with a time before the first job
		this.vehicleTime = LocalDateTime.parse(stringVehicleTime, formatter);
		
	}
	
	
	//####################################################################################	
	// Setter
	//####################################################################################

	public void setMaxBatteryCharge(Double maxBatteryCharge) {		//WAS IST maxBatterxCharge????
		this.maxBatteryCharge = maxBatteryCharge;
	}
	
	public void setEnergyConsumptionPerHour(Double energyConsumptionPerHour) {
		this.energyConsumptionPerHour = energyConsumptionPerHour;
		this.energyConsumptionPerMillisecond = energyConsumptionPerHour / 3600 / 1000;
	}
	
	public void setEnergyConsumptionPerMillisecond(Double energyConsumptionPerMillisecond) {
		this.energyConsumptionPerMillisecond = energyConsumptionPerMillisecond;
		this.energyConsumptionPerHour = energyConsumptionPerHour * 3600 * 1000;
	}
	
	public void setVelocityInKilometersPerHour(Double velocityInKilometersPerHour) {
		this.velocityInKilometersPerHour = velocityInKilometersPerHour;
	}
	
	public void setTheta(Integer theta) {
		this.theta = theta;
	}
	
	public void setEpsilon(Integer epsilon) {
		this.theta = epsilon;
	}
	
	public void setJobWeight(Double jobWeight) {
		this.jobWeight = jobWeight;
	}
	
	public void setBatteryWeight(Double batteryWeight) {
		this.batteryWeight = batteryWeight;
	}
	
	public void setDistanceWeight(Double distanceWeight) {
		this.distanceWeight = distanceWeight;
	}
	
	public void setAgentID(Integer agentID) {
		this.agentID = agentID;
	}
	
	public void setAgentLat(Double agentLat) {
		this.agentLat = agentLat;
	}
	
	public void setAgentLng(Double agentLng) {
		this.agentLng = agentLng;
	}
	
	public void setBattery(Double battery) {
		this.battery = battery;
	}
	
	public void setDriving(Integer driving) {
		this.driving = driving;
	}
	
	
	//####################################################################################	
	// Getter
	//####################################################################################	

	public Double getMaxBatteryCharge() {
		return this.maxBatteryCharge;
	}
	
	public Double getEnergyConsumptionPerHour() {
		return this.energyConsumptionPerHour;
	}
	
	public Double getEnergyConsumptionPerMillisecond() {
		return this.energyConsumptionPerMillisecond;
	}
	
	public Double getVelocityInKilometersPerHour() {
		return this.velocityInKilometersPerHour;
	}
	
	public Integer getTheta() {
		return this.theta;
	}
	
	public Integer getEpsilon() {
		return this.epsilon;
	}
	
	public Double getJobWeight() {
		return this.jobWeight;
	}
	
	public Double getBatteryWeight() {
		return this.batteryWeight;
	}
	
	public Double getDistanceWeight() {
		return this.distanceWeight;
	}
	
	public Integer getAgentID() {
		return agentID;
	}
	
	public Double getAgentLat() {
		return agentLat;
	}
	
	public Double getAgentLng() {
		return agentLng;
	}
	
	public Double getBattery() {
		return battery;
	}
	
	public Integer getDriving() {
		return driving;
	}
	
	public ArrayList getAssignments() {
		return assignments;
	}
	
	public ArrayList getNeighbours() {
		return neighbours;
	}
	
	//####################################################################################	
	// Methods
	//####################################################################################	
	
	public void updateNeighbours(ArrayList newNeighbours) {
		while (neighbours.size()>0) {
			neighbours.remove(0);
		}
		neighbours = newNeighbours;
		
	}
	
	
	public void newJob(Integer jobID, String jobStatus, Double startLat, Double startLng, Double endLat, Double endLng, Integer senderID, LocalDateTime startTime) {
		
		Job beliefjob = new Job(jobID, jobStatus, startLat, startLng, endLat, endLng, senderID, startTime);
		
		assignments.add(beliefjob);
	}
	
	public void setJobProgressOfJobID(Integer jobID, String newProgress) {
		
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {	
				(((Job) assignments.get(i))).setJobProgress(newProgress);
			}
		}
	}
	
	public String getJobProgressOfJobID(Integer jobID) {
		
		String jobProgress = "ID not found";
		for (int i = 0; i < assignments.size(); i++) {
			Integer camparisonID = (((Job) assignments.get(i))).getJobID();
			boolean Case = camparisonID.equals(jobID);
			if(camparisonID.equals(jobID)) {
				jobProgress = (((Job) assignments.get(i))).getJobProgress();		
			}
		}
		return jobProgress;
	}
	
	public Integer countSelfDoneJobs() {
		Integer amountJobs = 0;
		
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobProgress().equals("new")) {
				amountJobs += 1;
			}
			if((((Job) assignments.get(i))).getJobProgress().equals("accepted")) {
				amountJobs += 1;
			}	
			
		}
		
		return amountJobs;
	}
	
	
	public String getJobStatusOfJobID(Integer jobID) {
		String jobStatus = "ID not found";
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				jobStatus = (((Job) assignments.get(i))).getJobStatus();
			}
		}
		return jobStatus;
	}
	
	public Double getStartLatOfJobID(Integer jobID) {
		Double startLat = 0.0;
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				startLat = (((Job) assignments.get(i))).getStartLat();
			}
		}
		return startLat;
	}
	
	public Double getStartLngOfJobID(Integer jobID) {
		Double startLng = 0.0;
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				startLng = (((Job) assignments.get(i))).getStartLng();
			}
		}
		return startLng;
	}
	
	public Double getEndLatOfJobID(Integer jobID) {
		Double endLat = 0.0;
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				endLat = (((Job) assignments.get(i))).getEndLat();
			}
		}
		return endLat;
	}
	
	public Double getEndLngOfJobID(Integer jobID) {
		Double endLng = 0.0;
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				endLng = (((Job) assignments.get(i))).getEndLng();
			}
		}
		return endLng;
	}
	
	public LocalDateTime getStartTimeOfJobID(Integer jobID) {
		String stringStartTime = "2000-01-01T00:00:00";
		LocalDateTime startTime = LocalDateTime.parse(stringStartTime, formatter); //find better solution!!!!!!
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				startTime = (((Job) assignments.get(i))).getStartTime();
			}
		}
		return startTime;
	}
		
	public Integer getSourceIDOfJobID(Integer jobID) {
		Integer sourceID = 0;
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				sourceID = (((Job) assignments.get(i))).getSourceID();
			}
		}
		return sourceID;
	}
	

	public ArrayList getExpectedAnswersOfJobID(Integer jobID) {
		ArrayList expectedAnswers = new ArrayList();
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				 expectedAnswers = (((Job) assignments.get(i))).getExpectedAnswers();
			}
		}
		return expectedAnswers;
	}
	
	public ArrayList getAskedAgentsOfJobID(Integer jobID) {
		ArrayList askedAgents = new ArrayList();
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				askedAgents = (((Job) assignments.get(i))).getAskedAgents();
			}
		}
		return askedAgents;
	}
	
	public Integer getBestIDOfJobID(Integer jobID) {
		Integer bestID = 0;
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				bestID = (((Job) assignments.get(i))).getBestID();
			}
		}
		return bestID;
		
	}
	
	public Integer getJobIDOfLastJob(Integer JobID) {
		Integer lastJobID = 0; //init with invalid JobID
		for (int i = 0; i < assignments.size(); i++) {
			Integer jobIDtocheck = (((Job) assignments.get(i))).getJobID();
			if (getJobStatusOfJobID(jobIDtocheck).equals("finished") &&
				(getJobProgressOfJobID(jobIDtocheck).equals("new") ||
				getJobProgressOfJobID(jobIDtocheck).equals("accepted")))				
				{
				lastJobID = (((Job) assignments.get(i))).getJobID();	
				}
		}
		return lastJobID;
	}
	
	
	
// debug test #########################################################################################
	
	public void batteryUse(Double oldBatteryLevel, Double energyConsumption) {
		
		Double newBatteryLevel = oldBatteryLevel - energyConsumption;
		this.battery = newBatteryLevel;
	}
	

	public void resetBestBid(Integer jobID) {
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				
					(((Job) assignments.get(i))).setBestScore(0.0);
					
				}				 
			}
		}
		
	
	
	/** 
	 *  
	 */
	public Integer updateBestBid(Integer AgentID, Integer jobID, Double bestScore) {
		//
		Integer remaining = 999; 
				
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				
				remaining = (((Job) assignments.get(i))).removeExpectedAnswers(AgentID);
				 
				Double oldScore = (((Job) assignments.get(i))).getBestScore();
				if (bestScore > oldScore) {
					(((Job) assignments.get(i))).setBestScore(bestScore);
					(((Job) assignments.get(i))).setBestID(AgentID);
				}				 
			}
		}
		
		return remaining;
		
	}
	
	
	
	
	public void setAskedAgentsOfJobID(Integer jobID, ArrayList askedAgents) {
		
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				 (((Job) assignments.get(i))).setAskedAgents(askedAgents);		 
			}
		}
	}
	
	public void setExpectedAnswersOfJobID(Integer jobID, ArrayList expectedAnswers) {
		
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				 (((Job) assignments.get(i))).setExpectedAnswers(expectedAnswers);	
			
			}
		}
	}
	
	
	public void removeExpectedAnswersOfJobID(Integer jobID, Integer AgentID) {

		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				 (((Job) assignments.get(i))).removeExpectedAnswers(AgentID);
			}
		}
		
	}
	
	
	/**
	* updates the JobStauts for the given JobId to the given newJobStatus 
	*
	*/
	public void updateJobStatus(Integer jobID, String newJobStatus) {
		for (int i = 0; i < assignments.size(); i++) {
			if((((Job) assignments.get(i))).getJobID().equals(jobID)) {
				(((Job) assignments.get(i))).setJobStatus(newJobStatus);
				break;
			}
		}
	}
	
	public ArrayList getUnfinished() { //returns a list of the JobIDs of all Job that are unfinished
		ArrayList unfinished = new ArrayList();
		
		 for (int i = 0; i < assignments.size(); i++) {
			 if (((Job) assignments.get(i)).getJobStatus().equals("unfinished")) {
				 unfinished.add(((Job) assignments.get(i)).getJobID());
			 }
		      
		    }
		return unfinished;
	}
	
	public ArrayList getFinished() { //returns a list of the JobIDs of all Job that are unfinished
		ArrayList finished = new ArrayList();
		
		 for (int i = 0; i < assignments.size(); i++) {
			 if (((Job) assignments.get(i)).getJobStatus().equals("finished")) {
				 finished.add(((Job) assignments.get(i)).getJobID());
			 }
		    }
		return finished;
	}
	
	public Integer countLastJobs(){
		Integer lastJobs = 0;
		Integer consideredJobs = 3;
		
		
		if (consideredJobs>assignments.size()) {
			consideredJobs = assignments.size();
		}
		
		if (consideredJobs<0) {
			consideredJobs = 0;
		}

		
		for (int i = assignments.size()-1; i > (assignments.size()-consideredJobs); i--) { //
			//System.out.println("iLOOP: " + i);
			if((((Job) assignments.get(i))).getJobProgress().contentEquals("new")) {
				lastJobs +=1;
			}
			if((((Job) assignments.get(i))).getJobProgress().contentEquals("accepted")) {
				lastJobs +=1;
			}
			if((((Job) assignments.get(i))).getJobProgress().contentEquals("elected")) {
				lastJobs -=1;
			}
			
		}
		
		if (lastJobs<0) {
			lastJobs = 0;
		}
				
		return lastJobs;
	}
	

	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String beliefVariables = "\n"+"Belief |agentID|agentLat|agentLng|+++ battery ---|SelfDone|\n"
							+ "       |     " + agentID + " |" + agentLat + "|" + agentLng + "|+++ " + getBattery() + " ---|" + countSelfDoneJobs() +  "|"; 

			
		String jobsInAssignments = "\n|jobID|" + "jobStatus|" + "jobProgress|startLat|startLng|endLat|endLng|askedAgents|expectedAnswers|sourceID|bestID|bestScore|";
				for (int i = 0; i < assignments.size(); i++) {
					jobsInAssignments = jobsInAssignments + assignments.get(i);
				}
		
		String output = beliefVariables + jobsInAssignments + "\n___________________________________________________________"; 
		
		   return output ;
		}   
}