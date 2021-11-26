# ICAART 2022 submission: Negotiation in ride-hailing between cooperating BDI agents

This program requires a proper Jade (https://jade.tilab.com/) configuration (v4.5.0). Consider the following steps: 

- `src/`: source folder 
  - `Agent0.java`: Area agent distributing trip reguests to vehicle agents;
  - `Agent1.java`: Vehicle Agent 1 (other configured Agents are also Vehicle Agents);
  - `AgentStatus.java`: Status of the Vehicle Agent
  - `Belief.java`: Vehicle Agent Beliefs
  - `Debug.java`: Debug File to change parameters for each single agent carrying out experiments
  - `Desire.java`: Vehicle Agent Desires
  - `Environment.java`: Environment of Vehicle Agents
  - `Event.java`: Events occuring during Agent processing
  - `FilteredTask.java`: Filtered Tasks the Agent wants to process
  - `Intention.java`: Vehicle Agent Intentions
  - `Job.java`: Job to be processed by the Vehicle Agents
  - `Logger.java`: Logger for Simulated Event Trace
  - `MapArea.java`: Map Utilities
  - `Message.java`: Message for Agent communications
  - `Plan.java`: Vehicle Agent Plans
  - `Scheduler.java`: Dummy Schedule for Trip Requests
  - `TripRequest.java`: Customer Trip Request 


# Vehicle Agent cognitive BDI architecture 

![BDI architecture](TrikeAgent_abstract.pdf)


# How to run

1. Add the `jade.jar` into the  Java project libraries

2. Use the following program arguments in the *run configuration* to define which of the configured agentss will start (e.g. Area Agent + Vehicle Agent 1, 2, 3 for running the 3 Agent configuration):

Program arguments:
```bash
-gui Agent0:Agent0;Agent1:Agent1;Agent2:Agent2;Agent3:Agent3;
```
Use the `debug.java` to define specific parameters and load the data sets:

  - `Integer globalNegotiation = 1;`: enables negotiation if `1` and no negotiation with `0`;
  - `Double batteryWeight = (100.0/3.0);`: Utility component weight of `1/3`. This can be modified but the sum of `jobWeight`, `distanceWeight` and `batteryWeight` must be 100 i.e. 100%;
  - `Double distanceWeight = (100.0/3.0);`: Utility component weight of `1/3`;
  - `Double jobWeight = (100.0/3.0);`: Utility component weight of `1/3`;
  - `Double speed = 3.6;`: Vehicle Agent speed can be modified, we used `3.6 km/h`, which is equivalent to the walking speed;
  - `Integer theta = 4;`: waiting time of customers (has to be changed also in `Belief.java`);
  - `Integer epsilon = 3;`: buffer time;

# Dataset information

The data samples were created by modifying the "Call A Bike" data sets from Deutsche Bahn (DB).
https://data.deutschebahn.com/dataset/data-call-a-bike.html efgfiuvzgufbsdkhbkjshbvf
