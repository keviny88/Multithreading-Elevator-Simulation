// Wajih Yassine 42139560, Kevin Yin 29211757
public class BuildingFloor {
	//Class that represents the state of a specific floor in the building.
	int[] totalDestinationRequests; //Array where the i element is representing the number of passengers who has requests to go to the i floor throughout the simulation.
	int[] arrivedPassengers; //Array where the i element represents the number of passengers who arrived on this floor from an elevator.
	int[] passengerRequests; // Array where the i element represents the number of people who currently want to travel to the i floor of the building
	int approachingElevator; // The elevator ID that is currently heading to the floor for passengers pickup.
	
	public BuildingFloor(){
		this.totalDestinationRequests = new int[5];
		this.arrivedPassengers = new int[5];
		this.passengerRequests = new int[5];
		this.approachingElevator = -1; //-1 is used to represent no elevator currently heading in that direction.
	}
	
	//Setter methods
	public void _setPassengerRequests(int destinationFloor, int numPassengers) {
		//Sets the destination and number of passengers in the arrived passengerRequests array
		this.passengerRequests[destinationFloor] = numPassengers;
	}
	
	public void _setTotalDestinationRequests(int destinationFloor, int numPassengers){
		//Sets the total number of destination requests and numbers of passengers
		this.totalDestinationRequests[destinationFloor] += numPassengers;
	}
	
	public void _setApproachingElevator(int approachingElevator){
		//Sets the approaching elevator
		this.approachingElevator = approachingElevator;
	}
	
	public void _setTotalArrivalPassengers(int elevatorID, int numPassengers){
		//Sets the total passenger arrivals from an elevator.
		this.arrivedPassengers[elevatorID] += numPassengers; 
	}
	
	//Getter methods
	public int _getTotalDestinationRequests(int destinationFloor){
		//Grabs the total destination requests given a destination floor
		return this.totalDestinationRequests[destinationFloor];
	}
	
	public int _getTotalArrivalPassengers(int elevatorID){
		//Grabs the total arrived passengers from a given elevator
		return this.arrivedPassengers[elevatorID];
	}
	
	public int _getApproachingElevator(){
		//Grabs the approaching elevator from the floor
		return this.approachingElevator;
	}
	
	public int _getPassengerRequests(int destinationFloor){
		//Grabs the number of passengers requesting to go to a given destination floor
		return this.passengerRequests[destinationFloor];
	}

}
