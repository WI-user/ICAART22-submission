import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import FIPA.DateTime;
import jade.util.leap.ArrayList;
//####################################################################################
//Debug.java v1.6: translation + code cleaned
//####################################################################################
/**
* This is the Debug class
* in this you can define the internal states (beliefs, desires, intentions, ...) of the agents. 
* these will be loaded before the agent starts and can be used for debugging
* 
* The class is separated in different parts (if clauses), one for each agent. 
* If you want to use more than 3 agents just add a if clause for your agents in the same way.
*
* read the templates for examples of internal states or read the master thesis for further infomations 
* To make this file not unessesary wide there will beno proper indent
* 
* Warning! It is possible to use the comands to define invalid internal states Invalid 
* 
*/


//####################################################################################
// Settings for Agent0 (the Environment)
//####################################################################################
public class Debug {

	public Integer showMem;
	public Integer showStatus;
	
	public Integer getShowMem(){
		return showMem;
	}
	
	public Integer getShowStatus(){
		return showStatus;
	}
	Integer globalNegotiation = 1; //enable or disable negotiation
	Double batteryWeight = (100.0/3.0); 
	Double distanceWeight = (100.0/3.0);
	Double jobWeight = (100.0/3.0);
	Double speed = 3.6;
	Integer theta = 4;	// have to be changed also in Belief.java
	Integer epsilon = 3;

	
	public void loadEnvironment(Environment environment) {
		//====================================================================================
		//
		//====================================================================================
		Double latLeft =32475864.599194866;
		Double latRight =32476925.501925234;
		Double lonTop =5553350.366261396;
		Double lonBottom =5552179.49798069;
		Double latLeftToRight = lonTop - lonBottom;
		Double lonBottomToTop = latRight - latLeft;
		Double customerLat = latLeft + 0.5* latLeftToRight;
		Double customerLon = lonTop;
		
		ArrayList forcedNeighbours = new ArrayList();
		
		environment.setCalculate(1); 
		
		environment.setArmLengthReach(99999999999.9);
		
		
	//	environment.setForcedNeighbours(forcedNeighbours);
		
		//addCustomerJobs(Integer jobID, String jobStatus, Double startLat, Double startLng, Double endLat, Double endLng)

//Booking_ID		Job_Status		Start_Longitude		Start_Latitude		End_Longitude		End_Latitude		Area_Agent		Request_StartTime	
		
		
		//example
		environment.addCustomerJobs(	1	,	"new"	,	32476672.69	,	5552495.478	,	32475880.42	,	5553019.51	,	0	,	"2016-07-16T00:01:14"	);
		
		
		//add test sample data here! replace the exampl!
		
	}
	
	
	public void loader(Belief belief, Desire desire, Intention intention, Scheduler scheduler) {
		Double latLeft = belief.mapArea.getLeftBorder();
		Double latRight = belief.mapArea.getRightBorder();
		Double lonTop = belief.mapArea.getTopBorder();
		Double lonBottom = belief.mapArea.getBottomBorder();
		Double latLeftToRight = latRight - latLeft;
		Double lonBottomToTop = lonTop - lonBottom;
		
		Double pointALng = lonBottom + lonBottomToTop *0.80;
		Double pointALat = latLeft + 0.5* latLeftToRight;	
		Double pointBLng = lonBottom;
		Double pointBLat = latLeft;
		Double pointCLng = lonBottom + lonBottomToTop *0.5;
		Double pointCLat = latRight;
		
		Integer globalConsideredJobs = 3; // not used anymore! Jobs in the past that will be considered by calculating the job score
		
		
		
		
		//####################################################################################
		// Settings for Agent1
		//####################################################################################
		if (1==belief.getAgentID()) {
			showMem = 1;
			showStatus = 1;
		
			belief.setAgentLat(32476517.74);
			belief.setAgentLng(5552822.314);
				
			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); //50 km mit einem charge, fährt 6 km/h -> kann 8,33 stunden fahren, d.h. 25/3 stunden
			belief.setBattery(100.0);
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation); // 1: will enable negotiation (default even if entry is missing), 0: will disable negotiation
			
		//belief.newJob(	2	,	"unfinished"	,	32476390.99	,	5552888.197	,	32476206.06	,	5552808.708	,	0	);
		//	belief.newJob(            1,     "unfinished",            2.22,            2.22,          3.33,          3.33,                0);
		//intention.addForcedUtility(			 1, 		"self",				  100.0, 100.0);
			
		//	belief.newJob(            2,     "unfinished",            2.22,            2.22,          3.33,          3.33,                0);
		//	intention.addForcedUtility(			 2, 		"self",				  100.0, 100.0);
			
		}
		//####################################################################################
		// Settings for Agent2
		//####################################################################################
		else if (2==belief.getAgentID()) {

			showMem = 1;
			showStatus = 1;
		
			belief.setAgentLat(32476104.02); 	
			belief.setAgentLng(5552339.033);
			
			belief.setMaxBatteryCharge(50.0); 
			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); 
			belief.setBattery(100.0);
						
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation); // 1: will enable negotiation (default even if entry is missing), 0: will disable negotiation
			
		//belief.newJob(	2	,	"unfinished"	,	32476390.99	,	5552888.197	,	32476206.06	,	5552808.708	,	0	);
		//	belief.newJob(            1,     "unfinished",            2.22,            2.22,          3.33,          3.33,                0);
		//intention.addForcedUtility(			 1, 		"self",				  100.0, 100.0);
			
		//	belief.newJob(            2,     "unfinished",            2.22,            2.22,          3.33,          3.33,                0);
		//	intention.addForcedUtility(			 2, 		"self",				  100.0, 100.0);
			
		}
		//####################################################################################
		// Settings for Agent3
		//####################################################################################
		else if (3==belief.getAgentID()) {

			showMem = 1;
			showStatus = 1;

			belief.setAgentLat(32476391.36); 	
			belief.setAgentLng(5552477.073);
			
			belief.setMaxBatteryCharge(50.0); 
			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); 
			belief.setBattery(100.0);
						
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation); // 1: will enable negotiation (default even if entry is missing), 0: will disable negotiation
			
		//belief.newJob(	2	,	"unfinished"	,	32476390.99	,	5552888.197	,	32476206.06	,	5552808.708	,	0	);
		//	belief.newJob(            1,     "unfinished",            2.22,            2.22,          3.33,          3.33,                0);
		//intention.addForcedUtility(			 1, 		"self",				  100.0, 100.0);
			
		//	belief.newJob(            2,     "unfinished",            2.22,            2.22,          3.33,          3.33,                0);
		//	intention.addForcedUtility(			 2, 		"self",				  100.0, 100.0);
			
		}
		//####################################################################################
		// Settings for Agent4
		//####################################################################################
		else if (4==belief.getAgentID()) {

			showMem = 1;
			showStatus = 1;

			belief.setAgentLat(32476664.15); 	
			belief.setAgentLng(5552772.748);

			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); //50 km mit einem charge, fährt 6 km/h -> kann 8,33 stunden fahren, d.h. 25/3 stunden
			belief.setBattery(100.0);
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation);
			

		}
		//####################################################################################
		// Settings for Agent5
		//####################################################################################
		else if (5==belief.getAgentID()) {

			showMem = 1;
			showStatus = 1;

			belief.setAgentLat(32476080.43); 	
			belief.setAgentLng(5552657.361);

			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); //50 km mit einem charge, fährt 6 km/h -> kann 8,33 stunden fahren, d.h. 25/3 stunden
			belief.setBattery(100.0);
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation);
			
		}
		//####################################################################################
		// Settings for Agent6
		//####################################################################################
		else if (6==belief.getAgentID()) {

			showMem = 1;
			showStatus = 1;

			belief.setAgentLat(32476123.23); 	
			belief.setAgentLng(5552973.279);

			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); 
			belief.setBattery(100.0);
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation);

		}
		//####################################################################################
		// Settings for Agent7
		//####################################################################################
		else if (7==belief.getAgentID()) {

			showMem = 1;
			showStatus = 1;

			belief.setAgentLat(32476389.27); 	
			belief.setAgentLng(5553039.031);

			belief.setVelocityInKilometersPerHour(speed);
			belief.setTheta(theta);
			belief.setEpsilon(epsilon);
			belief.setEnergyConsumptionPerHour(speed); 
			belief.setBattery(100.0);
			belief.setBatteryWeight(batteryWeight);
			belief.setDistanceWeight(distanceWeight);
			belief.setJobWeight(jobWeight);
			//belief.setConsideredJobs(3);
			intention.setForcedDecision(0);	
			intention.setNegotiation(globalNegotiation);
		}		
				

					
		//####################################################################################
		// add Settings for other Agents here if needed 
		//####################################################################################
		//else if (4==belief.getAgentID()) {
		//}
	
	
	
	
	//####################################################################################	
	}// end of the "loader" method
	//####################################################################################
}
