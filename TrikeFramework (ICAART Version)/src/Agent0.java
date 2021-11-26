//####################################################################################
//Area Agent v1.6: translation + code cleaned 
//####################################################################################
/**
* This Class contains the area Agent
* 
*/

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import jade.core.AID;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.ArrayList;

public class Agent0 extends Agent 
{
	Integer agentID = 0;
	Environment environment = new Environment();
	Integer ready = 1;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	AtomicInteger updateAgentRunning = new AtomicInteger();

    protected void setup() 
    {
    	System.out.println("#Status#Agent#0 START");
    	Debug debug = new Debug();
    	debug.loadEnvironment(environment);
    	addBehaviour( new EnvironmentActivity() );
		}
      
    //####################################################################################    
    // MessageReceiver    
    //####################################################################################    
    class EnvironmentActivity extends CyclicBehaviour 
    {   //if else comparison of words like distribute is easier to understand then a switch case of numbers which would be faster
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msg = receive();
				if (msg!=null)
					try {
						msg.getContentObject();
						String temp = (String)msg.getContentObject();
						System.out.println("TEMP: " + temp);
						Message message = new Message(temp);
						System.out.println("#Status#Agent#" + agentID +": MESSAGE RECEIVED - SENDER WAS AGENT" + message.getSenderID());			
						System.out.println(message);
						if (message.getMessageType().equals("positionUpdate" )) {
							String tempNewStatus = environment.updateAgent(message.getSenderID(), message.getMessageType(), message.getStartLat(), message.getStartLng());
							System.out.println(tempNewStatus);
							if (tempNewStatus.endsWith("updated")) {
								addBehaviour( new Distribution() );	
							}
						}
						else if (message.getMessageType().equals("callForNeighbours")) {//Agent gets an announcement of the contractnet
							ACLMessage sendmsg = new ACLMessage(ACLMessage.INFORM);
							ArrayList neighbours = new ArrayList(); 							
							neighbours = environment.calculateNeighbours(message.getSenderID(), message.getStartLat(), message.getStartLng());
							if (environment.getCalculate()==0) {
								neighbours = environment.getForcedNeighbours();	
							}
														
							Message sendmessage = new Message("sendNeighbours", message.getJobID(), 0, 0.0, 0.0, 0.0, 0.0, 0.0, message.getStartTime(), neighbours);   //added startTime
				    		System.out.println("#Status#Agent#0 " + sendmessage);
				    		
				    		try {
								sendmsg.setContentObject(sendmessage.getCode());
							} catch (IOException e) {
								e.printStackTrace();
							}
				    		   			
				            	sendmsg.addReceiver( new AID( "Agent" + message.getSenderID(), AID.ISLOCALNAME));
				            	
				    		send(sendmsg);
				
						}
		
						else {
							  // block of code to be executed if the condition1 is false and condition2 is false
							}

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			//block(); //puts behavior on hold until next message is received
				
	 				if (environment.agents.size() > 2 && ready == 1) { 
	 					ready = 0;
	 					
	 					addBehaviour( new Distribution() );	
	 				}
			 }       
    }

    //####################################################################################
    // distributes jobs to Agents
    //####################################################################################
    class Distribution extends OneShotBehaviour 
    {
 		public void action()
        {
 			System.out.println("#Status#Agent#0: NEW CUSTOMER TRIP");
 			System.out.println("#Status#Agent#0: CALCULATING CLOSEST AGENTEN FOR TRIP DELEGATION");
 		if (environment.getCustomerJobsSize()>0) {
 			Integer jobID = environment.getNextCustomerJobID();
 			Double startLat = environment.getNextCustomerStartLat();
 			Double startLng = environment.getNextCustomerStartLng();
 			Double endLat = environment.getNextCustomerEndLat();
 			Double endLng = environment.getNextCustomerEndLng();
 			LocalDateTime startTime= environment.getNextCustomerStartTime();
 			Integer targetID = environment.getNextCustomerTargetID();
 			environment.deleteNextCustomerJob();	
 			Integer closestAgent = environment.closestAgent(startLat, startLng);
 			if (environment.getCalculate()==0) {
 				closestAgent = targetID;
 			}
 				
 		ACLMessage msg = new ACLMessage(ACLMessage.INFORM); 
 		ArrayList neighbours = new ArrayList();
		Message message = new Message("newjob", jobID, 0, 0.0, startLat, startLng, endLat, endLng, startTime, neighbours); 
		System.out.println("#Status#Agent#0: SENDING TRIP WITH ID "+ message.getJobID() + " TO AGENT" + closestAgent);
		System.out.println(message);
		try {
			msg.setContentObject(message.getCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	msg.addReceiver( new AID( "Agent" + closestAgent, AID.ISLOCALNAME) );
     	send(msg);
        }
 		
        }	
    }
    //####################################################################################
    }
       
