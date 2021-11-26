//####################################################################################
//Plan v1.6: translation + code cleaned
//####################################################################################
import java.time.LocalDateTime;

import jade.util.leap.ArrayList;

public class Plan {

	public Integer jobID;
	public Integer multipart = 0;
	public Integer busyFlag = 0;
	public Integer driveActivity = 0;
	public Double startLat = 0.0;
	public Double startLng = 0.0;
	public Double endLat = 0.0;
	public Double endLng = 0.0;	
	public LocalDateTime startTime;
	public Integer messageActivity = 0;
	public String messageContent;
	public Double score = 0.0;
	public ArrayList messageReceiver;

	
	//####################################################################################
	// Constructors
	//####################################################################################

	public Plan(Integer jobID, Integer multipart, Integer messageActivity, String messageContent, Double score, LocalDateTime startTime, ArrayList messageReceiver) {
		this.jobID = jobID;
		this.multipart = multipart;
		this.messageActivity = messageActivity;
		this.messageContent = messageContent;
		this.score = score;
		this.startTime = startTime;
		this.messageReceiver = messageReceiver;
	}
	
	public Plan(Integer jobID, Integer driveActivity, Double startLat, Double startLng, Double endLat, Double endLng, LocalDateTime startTime) {
		this.jobID = jobID;
		this.driveActivity = driveActivity;
		this.startLat = startLat;
		this.startLng = startLng;
		this.endLat = endLat;
		this.endLng = endLng;
		this.startTime = startTime;
	}
	
	public Plan(Integer jobID, Integer multipart, Integer busyFlag, Integer driveActivity, Double startLat, Double startLng, Double endLat, Double endLng, LocalDateTime startTime, Integer messageActivity, String messageContent, ArrayList messageReceiver) {
		this.jobID = jobID;
		this.multipart = multipart;
		this.busyFlag = busyFlag;
		this.driveActivity = driveActivity;
		this.startLat = startLat;
		this.startLng = startLng;
		this.endLat = endLat;
		this.endLng = endLng;	
		this.startTime = startTime;
		this.messageActivity = messageActivity;
		this.messageContent = messageContent;
		this.messageReceiver = messageReceiver;	
	}	

	//####################################################################################	
	// Setter
	//####################################################################################
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public Integer getJobID() {
		return jobID;
	}
	
	public Integer getMultipart() {
		return multipart;
	}
	
	public Integer getBusyFlag() {
		return busyFlag;
	}
	
	public Integer getDriveActivity() {
		return driveActivity;
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
	
	public Integer getMessageActivity() {
		return messageActivity;
	}
	
	public String getMessageContent() {
		return messageContent;
	}
	
	public Double getScore() {
		return score;
	}
	
	public ArrayList getMessageReceiver() {
		return messageReceiver;
	}
	
	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		   return "|" + jobID + "|" + multipart + "|" + busyFlag + "|" + driveActivity + "|" + startLat + "|" 
				   + startLng + "|" + endLat + "|" + endLng + "|" + messageActivity + "|" + messageContent + "|" 
				   + score + "|" + messageReceiver + "|";
		}   

}
