//####################################################################################
//AgentStatus.java v1.0:
//####################################################################################

public class AgentStatus {
	public Integer agentID;
	public String agentStatus;
	public Double agentLat;
	public Double agentLng;
	

	//####################################################################################
	// Constructors
	//####################################################################################
	
	public AgentStatus(Integer agentID, String agentStatus, Double agentLat, Double agentLng) {
		this.agentID = agentID;
		this.agentStatus = agentStatus;
		this.agentLat = agentLat;
		this.agentLng = agentLng;
	}
	
	//####################################################################################	
	// Setter
	//####################################################################################
	
	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}
	
	public void setAgentLat(Double agentLat) {
		this.agentLat = agentLat;
	}
	
	public void setAgentLng(Double agentLng) {
		this.agentLng = agentLng;
	}
	
	//####################################################################################	
	// Getter
	//####################################################################################
	
	public Integer getAgentID() {
		return agentID;
	}
	
	public String getAgentStatus() {
		return agentStatus;
	}
	
	public Double getAgentLat() {
		return agentLat;
	}
	
	public Double getAgentLng() {
		return agentLng;
	}
	
	//####################################################################################	
	// Debug output
	//####################################################################################
		
		public String toString() {
			String output = "\n"
							+ "|  " + agentID + "  |" + agentStatus + "|" + agentLat + "|" +  agentLng + "|"; 
							
			   return output ; 
			}
	
	
	
	
}
