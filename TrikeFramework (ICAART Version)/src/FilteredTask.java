//####################################################################################
//FilteredTask.java v1.0:
//####################################################################################
public class FilteredTask {

	public Integer jobID;
	public String jobType;
	public Double utilityScore;
	public Double ownBid;
	
	//####################################################################################
	// Constructors
	//####################################################################################

	public FilteredTask(Integer jobID, String jobType, Double utilityScore) {
		this.jobID = jobID;
		this.jobType = jobType;
		this.utilityScore = utilityScore;
	}	

	public FilteredTask(Integer jobID, String jobType, Double utilityScore, Double ownBid) {
		this.jobID = jobID;
		this.jobType = jobType;
		this.utilityScore = utilityScore;
		this.ownBid = ownBid;
	}
	//####################################################################################	
	// Setter
	//####################################################################################
	
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	public void setUtilityScore(Double utilityScore) {
		this.utilityScore = utilityScore;
	}
	
	public void setOwnBid(Double ownBid) {
		this.ownBid = ownBid;
	}
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public Integer getJobID() {
		return jobID;
	}
	
	public String getJobType() {
		return jobType;
	}
		
	public Double getUtilityScore() {
		return utilityScore;
	}
	
	public Double getOwnBid() {
		return ownBid;
	}

	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String output = "\n"
						+ "|  " + jobID + "  |" + jobType + "|" +  utilityScore + "|" + ownBid + "|";
		   return output ; 
		}   

}
