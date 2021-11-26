//####################################################################################
//Trike Agent v1.7: translation + code cleaned 
//####################################################################################
/**
* This Class contains a vehicle agent
* 
*/


import jade.core.Agent;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.behaviours.*;

//import jade.domain.AMSService;
//import jade.domain.FIPAAgentManagement.*;

import jade.lang.acl.*;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Agent4 extends Agent 
{
	private static final long serialVersionUID = 1L;
	
	//====================================================================================
	// extract the agentID from the class name, this makes it easier to create new agents (less changes nessesary)
	String className = this.getClass().getName();
	String namesplitter = "Agent";
	String[] nametokens = className.split(namesplitter);
	int agentID = Integer.parseInt(nametokens[1]);
	//====================================================================================
	
	Belief belief = new Belief(agentID, 0.0, 0.0, 100.0, 0);
	Desire desire = new Desire();
	Intention intention = new Intention();
	Scheduler scheduler = new Scheduler();
	Queue<String> EventQueue = new LinkedList<>(); //EventQueue for activity management
	//Integer debug = 1; //debuging Mode on = 1, off = 0, gives additional information
	Integer showmem = 1; // shows the state of belief, desire, intention and other
	Integer showstatus = 1; // prints the status of an agent
	Timestamp timestampRunning = new Timestamp(0);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	Debug debug = new Debug();
	Logger logger = new Logger(belief.getTheta());
	// atomic integers to count the amount of active instances of an agent component
	AtomicInteger messageReceiverRunning = new AtomicInteger(); 
	AtomicInteger evaluateRunning = new AtomicInteger();
	AtomicInteger filterRunning = new AtomicInteger();
	AtomicInteger meansEndsReasoningRunning = new AtomicInteger();
	AtomicInteger actRunning = new AtomicInteger();
	
	String status = "#Status#Agent#" + belief.getAgentID() + " ";
	String header = "###########################################################\n" + status;
	String seperator = "###########################################################";

    //####################################################################################    
    // setup     
    //####################################################################################	
	/**
	* initialize the Agent components 
	*
	*/
	protected void setup() 
    {	
		//====================================================================================
    	//debug command to load jobs, desires, intentions, Scheduler
    	//====================================================================================
    	debug.loader(belief, desire, intention, scheduler); // loads all predifined infos from the debug.java
    	showstatus = debug.getShowStatus(); // sets the showstaus and showmem to the value which is defined in the debug.java
    	showmem = debug.getShowMem();
    	
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    	if (showstatus==1) System.out.println(status + "START"); // status info
    	if (showmem==1) System.out.println( header + belief + "\n" + desire + "\n" + intention + "\n" + scheduler + seperator); // memory output
    	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    	//====================================================================================
		// Informs The environment (Agent0) about its new Position
		//====================================================================================		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		Message message = new Message("positionUpdate", belief.getAgentID(), belief.getAgentLat(), belief.getAgentLng());  
		try {
			msg.setContentObject(message.getCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.addReceiver( new AID( "Agent" + 0, AID.ISLOCALNAME) );
		send(msg);
		
		if (showstatus==1) System.out.println(status + "SEND POSITION UPDATE"); // status info
		//====================================================================================
    	
    	messageReceiverRunning.incrementAndGet(); // increment the counter for active messageReceivers 
    	addBehaviour( new MessageReceiver() );
    	evaluateRunning.incrementAndGet();
    	addBehaviour( new Evaluate() );

    	//====================================================================================

    }
    //####################################################################################
    
    //####################################################################################    
    // Evaluate     
    //####################################################################################
    /**
	* Evaluate, part of the BDI cycle
	*
	*/   
    class Evaluate extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		public void action() {
    		if (EventQueue.size()>=1){
    			String decission = EventQueue.remove();
    			if (decission.equals("start_evaluate")){
    				if (desire.updateDesires(belief) == 1) {
    					//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    					if (showstatus==1) System.out.println(status + "new desires were added"); // status info
    					//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    					EventQueue.add("start_filter");
    		    	}
    
    			}
    			else if(decission.equals("start_filter")) {
    				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    				if (showstatus==1) System.out.println(status + "Start Filter"); // status info
    				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					addBehaviour( new Filter() );
    			}
    			else if(decission.equals("start_means-ends-reasoning")) {
    				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    				if (showstatus==1) System.out.println(status + "Start Means-Ends-Reasoning"); // status info
    				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					addBehaviour( new Means_Ends_Reasoning() );
    			}
    			else if(decission.equals("start_act")) {
    				if(actRunning.get()==0) {
    					actRunning.incrementAndGet();
    					//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    					if (showstatus==1) System.out.println(status + "Start Act"); // status info
    					//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    					addBehaviour( new Act() );
    				}
    			}
    			else {
    				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    				if (showstatus==1) System.out.println(status + "ERROR in Evaluate: invalid entry in EventQueue!"); // status info
    				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    			}
    			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    			if (showmem==1) System.out.println(header + belief + "\n" + desire + "\n" + intention + "\n" + scheduler + seperator); // memory output
    			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    		}
    	}
    }
    
    //####################################################################################    
    // Filter     
    //####################################################################################
    /**
	 * Filter, part of the BDI cycle
	 */
    class Filter extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		public void action() {
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			if (showstatus==1) System.out.println(status + "Filter started"); // status info
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			if (intention.updateIntentions(belief, desire, logger) == 1) {
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				if (showstatus==1) System.out.println(status + "new Intentions were added"); // status info
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				EventQueue.add("start_means-ends-reasoning");
	    	}
		}
	}
    
    //####################################################################################    
    // Means-Ends-Reasoning     
    //####################################################################################    
    class Means_Ends_Reasoning extends OneShotBehaviour{
    	/**
		 * Means Ends Reasoning, part of the BDI cycle
		 */
		private static final long serialVersionUID = 1L;
		public void action() {
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			if (showstatus==1) System.out.println(status + "Means-Ends-Reasoning started"); // status info
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			scheduler.createPlan(belief, intention);

			System.out.println(status + "Nach createPlan"); // status info
			if (scheduler.ScheduleTasks()==1) {
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				if (showstatus==1) System.out.println(status + "new Plans were added"); // status info
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				EventQueue.add("start_act");
			}
		}
	}
           
    //####################################################################################    
    // Act     
    //####################################################################################
    class Act extends OneShotBehaviour{ 
    	/**
		 * Act
		 */
		private static final long serialVersionUID = 1L;
		public void action() {
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			if (showstatus==1) System.out.println(status + "Act started"); // status info
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			while (scheduler.getPlansSize()>=1){ 
				Plan actplan = scheduler.dequeuePlan(); 
				//====================================================================================
				// DriveActivity
				//====================================================================================
				if (actplan.getDriveActivity()>=1) {
					Integer tripWithCustomer = logger.translatePlanToTripPlan(belief, actplan); // translation for the logger
					// executes the drive activity
					Integer jobIDtocheck = actplan.getJobID();
					Double energyConsumption = intention.getBatteryConsumptionDrive(jobIDtocheck, belief);
					belief.setAgentLat(actplan.getStartLat());
					belief.setAgentLng(actplan.getStartLng());
					if (actplan.getDriveActivity()==2 && tripWithCustomer == 1) {
						belief.setAgentLat(actplan.getEndLat());
						belief.setAgentLng(actplan.getEndLng());
					}
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
					belief.batteryUse(belief.getBattery(), energyConsumption);
					System.out.println("#Status#Agent# " + belief.getAgentID() + "NEW BATTERY LEVEL = " + belief.getBattery());
					logger.Evaluation();
					logger.writeResults(belief.getAgentID(), intention.getNegotiation());
					
					//====================================================================================
					// Informs The environment (Agent0) about its new Position
					//====================================================================================		
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		    		Message message = new Message("positionUpdate", belief.getAgentID(), belief.getAgentLat(), belief.getAgentLng());  
		    		try {
						msg.setContentObject(message.getCode());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		msg.addReceiver( new AID( "Agent" + 0, AID.ISLOCALNAME) );
		    		send(msg);
		    		//====================================================================================
		  
				}	
				//====================================================================================
				// MessageActivity
				//====================================================================================
				if (actplan.getMessageActivity()==1) {
					
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					ArrayList neighbours = new ArrayList(); //TODO integrate
					//                                     String messageType,      Integer jobID,    Integer senderID,       Double score, Double startLat, Double startLng, Double endLat, Double endLng
		    		Message message = new Message(actplan.getMessageContent(), actplan.getJobID(), belief.getAgentID(), actplan.getScore(), actplan.getStartLat(), actplan.getStartLng(), actplan.getEndLat(), actplan.getEndLng(), actplan.getStartTime(), neighbours); //added startTime  
		    		try {
						msg.setContentObject(message.getCode());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		for (int i = 0; i<(actplan.getMessageReceiver().size()); i++) {   			
		            	msg.addReceiver( new AID( "Agent" + (actplan.getMessageReceiver().get(i)), AID.ISLOCALNAME));
		    		}
		    		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		    		if (showmem==1) System.out.println(header + message); // memory output
		    		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		    		if (actplan.getMessageContent().equals("callForProposals")){ // add the receivers of the message to the job in the beliefs
		    					    			
		    			//====================================================================================
		    			// This is some kind of workaround as it causes crazy errors if use the same ArrayList for
		    			// setAskedAgentsOfJobID and setExpectedAnswersOfJobID
		    			// this manual step by step was the only way to make it work (why ever...)
		    			//====================================================================================
		    			ArrayList messageReceivers1 = new ArrayList();
		    			ArrayList messageReceivers2 = new ArrayList();
		    			ArrayList receiverCopy = actplan.getMessageReceiver();
		    			for (int i=0; i<receiverCopy.size(); i++  ) {
		    				messageReceivers1.add(receiverCopy.get(i));
		    				messageReceivers2.add(receiverCopy.get(i));
		    			}
		    			//====================================================================================
		   
		    			belief.setAskedAgentsOfJobID(actplan.getJobID(), messageReceivers1);
		    			belief.setExpectedAnswersOfJobID(actplan.getJobID(), messageReceivers2);
		    			//belief.setExpectedAnswersOfJobID(actplan.getJobID(), actplan.getMessageReceiver());
		  
		    		}
		    		send(msg);
				}
				if(actplan.getMultipart()==0) { // when the job was not only a part of many it is finished now
					belief.updateJobStatus(actplan.getJobID(), "finished");
					timestampRunning.setTime(0);
				}
				EventQueue.add("start_act"); // The act behaviour is finished and the next 
				}
				actRunning.decrementAndGet();
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				if (showmem==1) System.out.println(header + belief + "\n" + desire + "\n" + intention + "\n" + scheduler + seperator); // memory output
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
		}
	}
    
    //####################################################################################    
    // MessageReceiver    
    //####################################################################################    
    class MessageReceiver extends CyclicBehaviour 
    {   //if else comparison of words like distribute is easier to understand then a switch case of numbers which would be faster
		/**
		 * Message Receiver
		 */
		private static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msg= receive();
				if (msg!=null)
					try {
						msg.getContentObject();
						String temp = (String)msg.getContentObject();
						Message message = new Message(temp);
						//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
						if (showstatus==1) System.out.println(status + "received message - SENDER AGENT" + message.getSenderID()); // status info						
						if (showmem==1) System.out.println(header + message); // memory output
						//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
						
						//====================================================================================
						// receive new jobs from Agent0
						//====================================================================================
						if (message.getMessageType().equals("newjob" )) {
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							if (showstatus==1) System.out.println(status + "message received - newjob"); // status info
							if (showstatus==1) System.out.println(status + "new job was added to Beliefs"); // status info
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							belief.newJob( message.getJobID(), "unfinished", message.getStartLat(), message.getStartLng(), message.getEndLat(), message.getEndLng(), 0, message.getStartTime()); //added startTime
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							if (showstatus==1) System.out.println(status + "add start_evaluate to EventQueue"); // status info
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							EventQueue.add("start_evaluate");
							Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());
							if (!(timestampRunning.equals(new Timestamp(0)))) {                                                 

								long elapsedTime =  newTimestamp.getTime() - timestampRunning.getTime();
								Double energyConsumption = elapsedTime * belief.energyConsumptionPerMillisecond;
								Double newBattery = belief.getBattery() * belief.maxBatteryCharge - energyConsumption;
								newBattery = newBattery/belief.maxBatteryCharge;
								belief.setBattery(newBattery);							
							}
							timestampRunning = newTimestamp;
						}
						//====================================================================================
						// receive the AgentIDs if the Agents in arm lengh reach
						//====================================================================================
						else if (message.getMessageType().equals("sendNeighbours")) {//Agent gets an announcement of the contractnet
							  // calculates bid and send bid message to sender
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							if (showstatus==1) System.out.println(status + "received answer for neighbour request"); // status info
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							
							belief.updateNeighbours(message.getNeighbours());
																					
							if (message.getNeighbours().size()==0){
								belief.setJobProgressOfJobID(message.getJobID(), "noNeighbours");
								//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
								if (showstatus==1) System.out.println(status + "No neighbours in arm lengh reach available!"); // status info
								//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							}
							else {
								
								belief.setJobProgressOfJobID(message.getJobID(), "started");
				
																
							}
							
							EventQueue.add("start_means-ends-reasoning");
						}
						//====================================================================================
						// receive a callForProposal
						//====================================================================================
						else if (message.getMessageType().equals("callForProposals")) {//Agent gets a bid of the contractnet
							belief.newJob( message.getJobID(), "unfinished", message.getStartLat(), message.getStartLng(), message.getEndLat(), message.getEndLng(), message.getSenderID(), message.getStartTime()); //added startTime
							belief.setJobProgressOfJobID(message.getJobID(), "callForProposals");
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							if (showstatus==1) System.out.println(status + "received a callForProposal"); // status info
							if (showstatus==1) System.out.println(status + "add start_evaluate to EventQueue"); // status info
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							EventQueue.add("start_evaluate");
						}	  // collect all bids and send a award to the best one
						
						//====================================================================================
						// receive propose
						//====================================================================================
						else if (message.getMessageType().equals("propose")) {
							//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
							if (showstatus==1) System.out.println(status + "got a propose"); // status info
							
							if (belief.updateBestBid(message.getSenderID(), message.getJobID(), message.getscore())==0) {
								
								if (belief.getJobProgressOfJobID(message.getJobID()).equals("started")){
									//System.out.println("JETZT");
									belief.setJobProgressOfJobID(message.getJobID(), "elected");
								}
								EventQueue.add("start_means-ends-reasoning");
							}
							
						}
						else if (message.getMessageType().equals("reject")) {
							belief.setJobProgressOfJobID(message.getJobID(), "rejected");
							belief.updateJobStatus(message.getJobID(), "finished");	
							EventQueue.add("start_evaluate");
							
						}
						else if (message.getMessageType().equals("accept")) {
							belief.setJobProgressOfJobID(message.getJobID(), "accepted");
							EventQueue.add("start_filter");
							Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());
							if (!(timestampRunning.equals(new Timestamp(0)))) {			
								
								long elapsedTime =  newTimestamp.getTime() - timestampRunning.getTime();
								Double energyConsumption = elapsedTime * belief.energyConsumptionPerMillisecond;
								Double newBattery = belief.getBattery() * belief.maxBatteryCharge - energyConsumption;
								newBattery = newBattery/belief.maxBatteryCharge;
								belief.setBattery(newBattery);							
							}
							timestampRunning = newTimestamp;
						}
						
						else {
							}
						if (showmem==1) System.out.println(header + belief + "\n" + desire + "\n" + intention + "\n" + scheduler + seperator); // memory output

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			 }       
    }
//####################################################################################
 
    //####################################################################################       
    
}//end class myAgent
