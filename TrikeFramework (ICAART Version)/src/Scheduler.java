//####################################################################################
//Scheduler v1.6: translation + code cleaned
//####################################################################################

import java.time.LocalDateTime;
import java.util.Queue;

import jade.util.leap.ArrayList;

public class Scheduler {

	public ArrayList schedulingArray = new ArrayList();	
	public ArrayList plans = new ArrayList();
	
	//####################################################################################
	// Constructors
	//####################################################################################

	/** 
	 * 
	 */
	public void createPlan(Belief belief, Intention intention) {
		ArrayList intentions = intention.getIntentions();
		for (int i = 0; i < intentions.size(); i++){ // for each intention get the jobID 
			Integer tocheck = intention.getIntentionsJobID(i); // tocheck is the jobID
			
			if (belief.getJobStatusOfJobID(tocheck).equals("unfinished")){ // get the belief with the same JobID
				String jobType = intention.getIntentionsJobType(i);
				String jobProgress = belief.getJobProgressOfJobID(tocheck);
				Double startLat = belief.getStartLatOfJobID(tocheck);
				Double startLng = belief.getStartLngOfJobID(tocheck);
				Double endLat = belief.getEndLatOfJobID(tocheck);
				Double endLng = belief.getEndLngOfJobID(tocheck);
				LocalDateTime startTime = belief.getStartTimeOfJobID(tocheck); //added startTime
				
				// Ability List
				if (jobType.equals("self")) { 
					Plan driveToCustomer = new Plan(tocheck, 2, startLat, startLng, endLat, endLng, startTime);
					schedulingArray.add(driveToCustomer);
				}
				else if(jobType.equals("other") && jobProgress.equals("new")    ) {
					
					ArrayList messageReceiver = new ArrayList();
					messageReceiver.add(0);
					Plan callForNeighbours = new Plan(tocheck, 1, 0, 0, 0.0, 0.0, 0.0, 0.0, startTime, 1, "callForNeighbours", messageReceiver); //#11
					schedulingArray.add(callForNeighbours);
					
				}
				else if(jobType.equals("other") && jobProgress.equals("noNeighbours")    ) {
					Plan driveToCustomer = new Plan(tocheck, 2, startLat, startLng, endLat, endLng, startTime);
					schedulingArray.add(driveToCustomer);
				
				
				}
				//====================================================================================
				// jobProgress == started > send callForProposals to the neighbour agents
				//====================================================================================
				else if (jobType.equals("other") && jobProgress.equals("started")) {
					
					ArrayList messageReceiver = belief.getNeighbours();
					//Plan(jobID, multipart, busyFlag, driveActivity, startLat, startLng, endLat, endLng, messageActivity, messageContent, messageReceiver)
					Plan callForProposals = new Plan(tocheck, 1, 0, 0, startLat, startLng, endLat, endLng, startTime,  1, "callForProposals", messageReceiver);
					belief.resetBestBid(tocheck); 
					schedulingArray.add(callForProposals);
					
				}
				//====================================================================================
				// jobProgress == elected > create messages to reject and accept the proposes
				//====================================================================================
				else if (jobType.equals("other") && jobProgress.equals("elected")) {
					
					ArrayList askedAgents = belief.getAskedAgentsOfJobID(tocheck);
					belief.getBestIDOfJobID(tocheck);
					ArrayList toAccept = new ArrayList();
					ArrayList toReject = new ArrayList();
					ArrayList allAskedAgents = belief.getAskedAgentsOfJobID(tocheck);
					toAccept.add(belief.getBestIDOfJobID(tocheck));
					for (int j = 0; j < allAskedAgents.size(); j++) {
						if (allAskedAgents.get(j)!= belief.getBestIDOfJobID(tocheck)) {
							toReject.add(allAskedAgents.get(j));
						}
					}
					Plan sendReject = new Plan(tocheck, 1, 0, 0, startLat, startLng, endLat, endLng, startTime, 1, "reject", toReject);
					Plan sendAccept = new Plan(tocheck, 0, 0, 0, startLat, startLng, endLat, endLng, startTime, 1, "accept", toAccept);
					schedulingArray.add(sendReject);
					schedulingArray.add(sendAccept);
				}
				//====================================================================================
				// jobType == open > send a propose
				//====================================================================================
				else if (jobType.equals("open")) {
					ArrayList messageReceiver = new ArrayList();
					messageReceiver.add(((Belief) belief).getSourceIDOfJobID(tocheck));
					Double bid = 0.0;
				    for (int j = 0; j < intentions.size(); j++) {
				    	FilteredTask ft = (FilteredTask) intentions.get(j);
						if (tocheck == ft.getJobID()) { 
							bid = ft.getOwnBid();
						}
				    }
								
					if (intention.forcedDecision == 1 && intention.forcedUtilityExists(tocheck)) {
					bid = intention.getForcedUtility(tocheck).getOwnBid();
					}
					//getOwnBid()
					
					Integer test = belief.getAgentID();
					
					Plan propose = new Plan(tocheck, 1, 1, "propose", bid, startTime, messageReceiver);
					//Integer jobID, Integer multipart, Integer messageActivity, String messageContent, Double score, LocalDateTime startTime, ArrayList messageReceiver
					schedulingArray.add(propose);
					
				}
				else {
					System.out.println("Agent" + belief.getAgentID());
					System.out.println("ERROR in createPlan: invalid jobType: " + jobType);
					System.out.println("jobProgress: " + jobProgress);
				}
			}
				
			}
			
		}
				
	
	public Integer ScheduleTasks() {
		
		Integer newplan = 0;
		if (schedulingArray.size()>0) {
			
			newplan = 1;
		}
		while (schedulingArray.size()>0) {
			
			enqueuePlan((Plan) schedulingArray.get(0));
		}
		
		return newplan;
	}
	
	
	public String enqueuePlan(Plan plan) {
		
		plans.add(plan);
		schedulingArray.remove(0);
		return "plan added";
		
	}
	
	public Plan dequeuePlan() {
		
		if (plans.size()>0) {
			Plan extractedPlan = (Plan) plans.get(0);
			plans.remove(0);
			return extractedPlan;
		}
		return null;
	}
	
	public Integer getPlansSize() {
		return plans.size();
	}
	
	//####################################################################################	
	// Abilities
	//####################################################################################

	//####################################################################################	
	// Setter
	//####################################################################################

	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String schedulerVariables = "Plans | \n" 
				+ "|jobID|multipart|busyFlag|driveActivity|startLat | startLng | "
				+ "endLat | endLng |messageActivity|messageContent|score|messageReceiver \n";
		
		String jobsInPlans = "";
		
				for (int i = 0; i < plans.size(); i++) {
					jobsInPlans += plans.get(i);
				}
		
		String output = schedulerVariables + jobsInPlans + "\n"; 
		
		   return output ;
		}

}
