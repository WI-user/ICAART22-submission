//####################################################################################
//Message v1.6: translation + code cleaned
//####################################################################################
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jade.util.leap.ArrayList;

public class Message {
	// Private instance variables
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	public String messageType = "";
	public Integer jobID;
	public Integer senderID = 0;
	public Double score =  0.0;
	public Double startLat = 0.0;
	public Double startLng = 0.0;
	public Double endLat = 0.0;
	public Double endLng = 0.0;
	public LocalDateTime startTime = LocalDateTime.parse("2000-01-01T00:00:00", formatter); 
	public ArrayList neighbours = new ArrayList();
	public String code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng + "#" + startTime + "#" + neighbours;
	
	//####################################################################################
	// Constructors
	//####################################################################################
	public Message(String messageType, Integer jobID, Integer senderID, Double score, Double startLat, Double startLng, Double endLat, Double endLng, LocalDateTime startTime, ArrayList neighbours) {
		this.messageType = messageType;
		this.jobID = jobID;
		this.senderID = senderID;
		this.score = score;
		this.startLat = startLat;
		this.startLng = startLng;
		this.endLat = endLat;
		this.endLng = endLng;
		this.startTime = startTime;
		this.neighbours = neighbours;
		
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng + "#" + formatter.format(startTime) + "#" + neighbours;
	
	}
	
	public Message(String messageType, Integer senderID, Double startLat, Double startLng) { //startTime neccesary???
		this.messageType = messageType;
		this.jobID = 0;
		this.senderID = senderID;
		this.score = 0.0;
		this.startLat = startLat;
		this.startLng = startLng;
		this.endLat = 0.0;
		this.endLng = 0.0;

		
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng + "#" + formatter.format(startTime) + "#" + neighbours; //replace by startTime 
	
	}
	
	
	
	public Message(String code) {
		
		String[] temp = code.split("#");
		messageType = temp[0]; //Message Type
		
		jobID = Integer.parseInt(temp[1]); //ID: Number of The Agent
		senderID = Integer.parseInt(temp[2]);
		score = Double.parseDouble(temp[3]);
		startLat = Double.parseDouble(temp[4]);
		startLng = Double.parseDouble(temp[5]);
		endLat = Double.parseDouble(temp[6]);
		endLng = Double.parseDouble(temp[7]);

		startTime = LocalDateTime.parse(temp[8], formatter);
		
		ArrayList neighbours = new ArrayList();

		String tempNeigbour = temp[9]; // transform the string into an array of integers

		if (!"[]".equals(tempNeigbour)) {
			
			String temp1 = tempNeigbour.replace( "[", "" );
			
			String temp2 = temp1.replace( "]", "" );
			String temp3 = temp2.replace( " ", "" );
			String[] splitted = temp3.split(","); 
			
			for (int i=0; i<splitted.length; i++) {
				String transform = splitted[i];
				this.neighbours.add(Integer.parseInt(transform));
				
			}
		}
		
		
	}
	//####################################################################################	
	// Setter
	//####################################################################################
	// currently not used, if needed have to be updated as they are probably not working at the moment
	// starttime ergänzen
	// nighbous überall noch hinten dran hängen???
	public void setMessageType(String messageType) {
		this.messageType = messageType;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setJobID(Integer jobID) {
		this.jobID = jobID;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setSenderID(Integer senderID) {
		this.senderID = senderID;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setScore(Double score) {
		this.score = score;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setStartLat(Double startLat) {
		this.startLat = startLat;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setStartLng(Double startLng) {
		this.startLng = startLng;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setEndLat(Double endLat) {
		this.endLat = endLat;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	public void setEndLng(Double endLng) {
		this.endLng = endLng;
		code = messageType + "#" + jobID + "#" + senderID + "#" + score + "#" + startLat + "#" + startLng + "#" + endLat + "#" + endLng;
	}
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	public String getMessageType() {
		return messageType;
	}
	
	public Integer getJobID() {
		return jobID;
	}
	
	public Integer getSenderID() {
		return senderID;
	}
	
	public Double getscore() {
		return score;
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
	
	public String getCode() {
		return code;
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public ArrayList getNeighbours() {
		return neighbours;
	}
	
	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String messageVariables = "Message | messageType |  jobID  | senderID | score | startLat |startLng|endLat|endLng|startTime|neighbours|\n"
							+ "        |" + messageType + "|    " + jobID + "     |   " + senderID + "   | " +  score + "|" + startLat + " | "
							+ startLng + "|" + endLat + "|" + endLng + "|" + startTime + "|" + neighbours; 		
		String output = messageVariables + "|" + "\n###########################################################";
		   return output ;
		}  

}
