// Wajih Yassine 42139560, Kevin Yin 29211757
public class SimClock {
	private static int simTime;
	
	public SimClock(){ //Initializes the simulated time to 0
		this.simTime = 0; 
	}
	
	public static void tick(){ //Increments simulated time to 0
		simTime++;
	}
	
	public synchronized static int getTime(){ //Returns the value of the simulated time
		return simTime;
	}

}
