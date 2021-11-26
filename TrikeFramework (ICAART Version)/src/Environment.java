//####################################################################################
//Environment.java v1.6: translation + code cleaned
//####################################################################################

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import FIPA.DateTime;
import jade.util.leap.ArrayList;

public class Environment {
	public ArrayList agents;
	public ArrayList customerJobs;
	public ArrayList forcedNeighbours;
	public Integer calculate;
	public Double armLengthReach;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
	//####################################################################################
	// Constructors
	//####################################################################################
	
	public Environment() {
		agents = new ArrayList();
		customerJobs = new ArrayList();
	}
	
	//####################################################################################	
	// Setter
	//####################################################################################
	
	public void setCalculate(Integer calculate) {
		this.calculate = calculate;
	}
	
	public void setArmLengthReach(Double armLengthReach) {
		this.armLengthReach = armLengthReach;
	}
	
	public void setForcedNeighbours(ArrayList forcedNeighbours) {
		this.forcedNeighbours = forcedNeighbours;
	}
	
	// new version with string > date
		public void addCustomerJobs(Integer jobID, String jobStatus, Double startLat, Double startLng, Double endLat, Double endLng, Integer targetID, String stringStartTime) {
			LocalDateTime startTime = LocalDateTime.parse(stringStartTime, formatter);
			Job predefinedJob = new Job( jobID, jobStatus, startLat, startLng, endLat, endLng, targetID, startTime);
			customerJobs.add(predefinedJob);
		}
	
	public Integer getNextCustomerJobID(){
		Integer jobID = (((Job) customerJobs.get(0))).getJobID();
		return jobID;
	}
	
	public Integer getNextCustomerTargetID(){
		Integer targetID = (((Job) customerJobs.get(0))).getSourceID(); //normaly Jobs does not have a targetID but in this case 
		return targetID;												//i use the SourceID as a TargetID because it is not used for customerJobs
	}
	
	public Double getNextCustomerStartLat(){
		Double startLat = (((Job) customerJobs.get(0))).getStartLat();
		return startLat;
	}
	
	public Double getNextCustomerStartLng(){
		Double startLng = (((Job) customerJobs.get(0))).getStartLng();
		return startLng;
	}
	
	public Double getNextCustomerEndLat(){
		Double endLat = (((Job) customerJobs.get(0))).getEndLat();
		return endLat;
	}
	
	public Double getNextCustomerEndLng(){
		Double endLng = (((Job) customerJobs.get(0))).getEndLng();
		return endLng;
	}
	
	public LocalDateTime getNextCustomerStartTime(){
		LocalDateTime startTime = (((Job) customerJobs.get(0))).getStartTime();
		return startTime;
	}
	
	
	
	public Integer getCustomerJobsSize(){
		Integer customerJobsSize = customerJobs.size();
		return customerJobsSize;
	}
	
	public void deleteNextCustomerJob(){
		customerJobs.remove(0);
	}
	
	public void addNewAgent(Integer agentID, String agentStatus, Double agentLat, Double agentLng) {
		
		AgentStatus agent = new AgentStatus(agentID, agentStatus, agentLat, agentLng);
		
		agents.add(agent);
	}
	
	public String updateAgent(Integer agentID, String agentStatus, Double agentLat, Double agentLng) {
		String newStatus = "";
		if (agents.size()==0) {
			newStatus = "Agent" + agentID + " was added to agents";
			addNewAgent(agentID, agentStatus, agentLat, agentLng);
		}
		
		else {
			Integer notfound = 0;
			for (int i = 0; i < agents.size(); i++) {
				//System.out.println("die agentID array = " + (((AgentStatus) agents.get(i))).getAgentID());
				//System.out.println("agentID übergabe = " + agentID);
				if((((AgentStatus) agents.get(i))).getAgentID()==agentID) {
					notfound = 1;
					newStatus = "Agent" + agentID + " position was updated";
					//System.out.println("ÄNDERUNG PASSIERT!");

					(((AgentStatus) agents.get(i))).setAgentStatus(agentStatus);
					(((AgentStatus) agents.get(i))).setAgentLat(agentLat);
					(((AgentStatus) agents.get(i))).setAgentLng(agentLng);
				}
				
			}
			if (notfound ==0) {
				newStatus = "Agent" + agentID + " was added to agents";
				addNewAgent(agentID, agentStatus, agentLat, agentLng);
			}
		}
		return newStatus;
	}
	
	
	
	//####################################################################################	
	// Getter
	//####################################################################################
	
	public Integer getAgentsSize() {
		
		return agents.size();
	}
	
	public ArrayList getAgents() {
		return agents;
	}
		
	public Integer getCalculate() {
		return calculate;
	}	
		
	public Double getArmLengthReach() {
		return armLengthReach;
	}	
		
	public ArrayList getForcedNeighbours() {
		return forcedNeighbours;
	}	
		
	
	public ArrayList calculateNeighbours(Integer agentID, Double agentLat, Double agentLng) { 
		ArrayList neighbours = new ArrayList();
		
		if (calculate==0) {
			neighbours = forcedNeighbours;
		}

		else{
			for (int i = 0; i < agents.size(); i++) {
				
				Integer toCompareID = (((AgentStatus) agents.get(i))).getAgentID();
				if(toCompareID != agentID) {
					Double neighbourLat = (((AgentStatus) agents.get(i))).getAgentLat();
					Double neighbourLng = (((AgentStatus) agents.get(i))).getAgentLng();
					Double neigbourDistance = Math.sqrt( ( Math.pow( (agentLat - neighbourLat), 2) + Math.pow( (agentLng - neighbourLng), 2) ) );
					if(neigbourDistance <= armLengthReach) {
						neighbours.add(toCompareID);	
					}	
				}	
			}
		}
		
		return neighbours;
	}
	
	public Integer closestAgent(Double startLat, Double startLng) {
		Integer closestAgentID = 0;
		Double closestDistance = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < agents.size(); i++) {
			Double neighbourLat = (((AgentStatus) agents.get(i))).getAgentLat();
			Double neighbourLng = (((AgentStatus) agents.get(i))).getAgentLng();
			Double neigbourDistance = Math.sqrt( ( Math.pow( (startLat - neighbourLat), 2) + Math.pow( (startLng - neighbourLng), 2) ) );
			if (neigbourDistance < closestDistance) {
				closestDistance = neigbourDistance;
				closestAgentID = (((AgentStatus) agents.get(i))).getAgentID();
		}
		}
		
		
		return closestAgentID;
	}
	
	
	
	//####################################################################################	
	// Getter
	//####################################################################################
	
	
	
	//####################################################################################	
	// Debug output
	//####################################################################################
		
	public String toString() {
		String output = "\n"
			+ agents + "\n" + customerJobs;
			   return output ; 
		}   

	
	
	
	
	
}
