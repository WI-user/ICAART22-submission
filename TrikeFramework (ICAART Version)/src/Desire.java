//####################################################################################
//Desire.java v1.0:
//####################################################################################

import jade.util.leap.ArrayList;

public class Desire {
	
 
	public ArrayList desires = new ArrayList();
	
	//####################################################################################
	// Constructors
	//####################################################################################

	
	
	//####################################################################################	
	// Setter
	//####################################################################################
	
		
	
	//####################################################################################	
	// Getter
	//####################################################################################	
	
	
	
	//####################################################################################	
	// Methods
	//####################################################################################	
	
	public void newDesire(Integer jobID) {
				
		desires.add(jobID);
	}
		
	public ArrayList getDesires() { //returns a list of the JobIDs of all Job that are Desired

		return desires;
	}
	
	public void removeDesire(Integer JobID) { //removes the desire with the given JobID
		for (int i = 0; i < desires.size(); i++) {
			if(desires.get(i).equals(JobID)) {
				desires.remove(i);
			} 
		}
	}
	
	/** Removes all Desires which are already flagged as finished in the beliefs
	 * 
	 */
	public void removeFinishedJobs(Belief belief) {
		ArrayList finished = belief.getFinished();
		for (int i = 0; i < desires.size(); i++) { // for every element of desire
		    Integer tocheck = (Integer) desires.get(i); // check if its value (jobID)
		    for (int j = 0; j < finished.size(); j++) { // is inside the list of the finished JobIDs
		    	if (tocheck.equals(finished.get(j))) { // if yes remove the finished job
		    		
		    		desires.remove(i);
		    		i -=1; // by removing a desire the next on will get its old index. without decreasing you could oversee a desire
		    	}
		    }
		    
		}
	}
	
	
	
	/** Add all Jobs to desires that are flagged as unfinished
	 *  returns 1 if new desires were added
	 */
	public Integer addUnfinishedJobs(Belief belief) {
		ArrayList unfinished = belief.getUnfinished();
		Integer desireadded = 0; // return 1 iff new desires were added while comparing 
		for (int i = 0; i < unfinished.size(); i++) { // for every JobID of the unfinishedJobs
		    Integer tocheck = (Integer) unfinished.get(i); // save the JobID to perform a check
		    Integer found = 0;								
		    for (int j = 0; j < desires.size(); j++) { // check if the JobID is inside of the desires 
		    	
		    	if (tocheck.equals(desires.get(j))) { 
		    		found = 1;							// set "found = 1" if it is
		    		
		    		
		    	}
		    	
		    }
		    if (found.equals(0)) {							// if it was not inside the desires, add it
		    	desires.add(tocheck);
		    	desireadded = 1;
		    	
		    }
		}
		
		return desireadded;
	}
	
	
	/** Adds and removes  desires in one call by using the upper methods
	 * 
	 */
	
	public Integer updateDesires(Belief belief) { //updates the desires given a set of beliefs
		removeFinishedJobs(belief);
		return addUnfinishedJobs(belief);
		
	}
	
	//####################################################################################	
	// Debug output
	//####################################################################################
	
	public String toString() {
		String desireHeader = "Desire | JobID: ";
		
		String currentDesires = "";
				for (int i = 0; i < desires.size(); i++) {
				
					currentDesires += desires.get(i) + ", ";
				}
				currentDesires += "\n___________________________________________________________";//String.valueOf(self.getAssignments);
		
		String output = desireHeader + currentDesires;  
		
		   return output ;   
		   }
}


