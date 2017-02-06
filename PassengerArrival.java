// Wajih Yassine 42139560, Kevin Yin 29211757
public class PassengerArrival {
	//Class that handles the passenger arrival behavior from the configuration file.
	private int numPassengers; //Represents the number of passengers that will request an elevator.
	private int destinationFloor; //Represents the desired destination floor of the passengers
	private int timePeriod; //Represents the periodic time period passengers will request an elevator.
	private int expectedTimeOfArrival; //Represents simulated time where the next batch of passengers will enter the simulation.
	
	public PassengerArrival(int numPassengers, int destinationFloor, int timePeriod){
		this.numPassengers = numPassengers;
		this.destinationFloor = destinationFloor;
		this.timePeriod = timePeriod;
		this.expectedTimeOfArrival = timePeriod; //Initial expected time of arrival will be whatever the timePeriod is.
	}
	
	//Getter methods
	public int getNumPassengers(){
		return this.numPassengers;
	}
	
	public int getDestinationFloor(){
		return this.destinationFloor;
	}
	
	public int getTimePeriod(){
		return this.timePeriod;
	}
	
	public int getExpectedTimeOfArrival(){
		return this.expectedTimeOfArrival;
	}
	
	//Setter methods
	public void setExpectedArrivalTime(int expectedTimeOfArrival){
		this.expectedTimeOfArrival = expectedTimeOfArrival;
	}
}
