//Job.java v1.6: translation + code cleaned
import java.sql.Date;
import java.time.LocalDateTime;

import FIPA.DateTime;
import jade.util.leap.ArrayList;

public class Job {

	public Integer jobID;
	public String jobStatus;
	public String jobProgress = "new";
	public Double startLat;
	public Double startLng;
	public Double endLat;
	public Double endLng;
	public ArrayList askedAgents = new ArrayList();// AgentID which got a 
	public ArrayList expectedAnswers = new ArrayList(); // saves the AgentIDs of the Agents which have to give an answer
	public Integer sourceID = 0; //AgentID of the Agent that send the Job for normal job its always 0 (Agent0) 
	public Integer bestID; // the AgentID of the best score got yet
	public Double bestScore = 0.0; // the best Score until now
	public LocalDateTime startTime;
	
	//####################################################################################
	// Constructors
	//####################################################################################
	public Job(Integer jobID, String jobStatus, Double startLat, Double startLng, Double endLat, Double endLng, LocalDateTime startTime) {
		this.jobID = jobID;
		this.jobStatus = jobStatus;
		this.startLat = startLat;
		this.startLng = startLng;
		this.endLat = endLat;
		this.endLng = endLng;	
		this.startTime = startTime;
	}
	public Job(Integer jobID, String jobStatus, Double startLat, Double startLng, Double endLat, Double endLng, Integer sourceID, LocalDateTime startTime) {
		this.jobID = jobID;
		this.jobStatus = jobStatus;
		this.startLat = startLat;
		this.startLng = startLng;
		this.endLat = endLat;
		this.endLng = endLng;
		this.sourceID = sourceID;
		this.startTime = startTime;
		
	}

	//####################################################################################	
	// Methods
	//####################################################################################
	
	/** overwrites the askedAgents for an job
	 *  
	 */
	public void setAskedAgents(ArrayList askedAgents) {
		this.askedAgents = askedAgents;
	}	
	
	/** overwrites the expectedAnsweres for an job
	 *  
	 */
	public void setExpectedAnswers(ArrayList expectedAnswers) {
		this.expectedAnswers = expectedAnswers;
	}
	
	/** removes a given AgentID from the list of expectedAnswers
	 *  returns the remaining exspectedAnsweres
	 */
	public Integer removeExpectedAnswers(Integer AgentID) {
		for (int i=0; i<expectedAnswers.size(); i++) {
			if (expectedAnswers.get(i)==AgentID) {
				expectedAnswers.remove(i);

			}
		}

		return expectedAnswers.size();
	}

	//####################################################################################	
	// Setter
	//####################################################################################
	
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	public void setJobProgress(String jobProgress) {
		this.jobProgress = jobProgress;
	}
	
	public void setBestID(Integer bestID) {
		this.bestID = bestID;
	}
	
	public void setBestScore(Double bestScore) {
		this.bestScore = bestScore;
	}
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public Integer getJobID() {
		return jobID;
	}
	
	public String getJobStatus() {
		return jobStatus;
	}
	
	public String getJobProgress() {
		return jobProgress;
	}
		
	public Double getStartLat() {
		return startLat;
	}
	
	public Double getStartLng() {
		return startLng;
	}
	
	public Double getEndLat() {
		return endLat;
	}
	
	public Double getEndLng() {
		return endLng;
	}
	
	public LocalDateTime getStartTime() {
		return startTime; 
	}
	
	public ArrayList getAskedAgents() {
		return askedAgents;
	}
	
	public ArrayList getExpectedAnswers() {
		return expectedAnswers;
	}
	
	public Integer getSourceID() {
		return sourceID;
	}
	
	public Integer getBestID() {
		return bestID;
	}
	
	public Double getBestScore() {
		return bestScore;
	}
	
	
	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String output = "\n"
						+ "|  " + jobID + "  |" + jobStatus + "|" + jobProgress + "|" +  startLat + "|" + startLng + "|" + endLat 
						+ "|" + startTime + "|" +  endLng  + "|" + askedAgents + "|" + expectedAnswers + "|" + sourceID +  "|" + bestID + "|" + bestScore + "|";
		   return output ; 
		}   

}
