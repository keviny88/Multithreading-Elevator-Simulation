// Wajih Yassine 42139560, Kevin Yin 29211757
public class BuildingManager {
	//Class that allows elevators to manipulate the state of the floors. It is shared among all elevators
	//Will implement lock/synchronized mechanisms to prevent race conditions
	BuildingFloor[] floors = new BuildingFloor[5]; //An array of BuildingFloors representing the state of all floors in the building
	
	public BuildingManager(){
		//Constructor that initializes new floor objects (0-4) for each index of the floors array.
		this.floors[0] = new BuildingFloor();
		this.floors[1] = new BuildingFloor();
		this.floors[2] = new BuildingFloor();
		this.floors[3] = new BuildingFloor();
		this.floors[4] = new BuildingFloor();
	}
	
	public int getFloorSize(){
		//Grabs the building floor size
		return this.floors.length;
	}
	
	public int getPassengerRequests(int floorNumber, int destinationFloor){
		//Calls on the BuildingFloor inner method to grab the passenger requests
		return this.floors[floorNumber]._getPassengerRequests(destinationFloor);
	}
	
	public int getTotalDestinationRequests(int floorNumber, int destinationFloor){
		//Calls on the BuildingFloor inner method to grab the total number of destination requests on a given floor
		return this.floors[floorNumber]._getTotalDestinationRequests(destinationFloor);
	}
	
	public int getTotalArrivalPassengers(int floorNumber, int elevatorID){
		//Calls on the BuildingFloor inner method to grab the total number of arrivals from a given elevator.
		return this.floors[floorNumber]._getTotalArrivalPassengers(elevatorID);
	}
	
	//Setter methods that interact with BuildingFloor objects
	public void setTotalDestinationRequests(int floorNumber, int destinationFloor, int numPassengers){
		//Calls on the BuildingFloor inner method to set total number of destination requests and passengers.
		this.floors[floorNumber]._setTotalDestinationRequests(destinationFloor, numPassengers);
	}
	
	public synchronized void setTotalArrivalPassengers(int floorNumber, int numPassengers, int elevatorID){
		//Calls on the BuildingFloor inner method to set the total number of arrival requests from an elevator
		this.floors[floorNumber]._setTotalArrivalPassengers(elevatorID, numPassengers);
	}
	
	public synchronized void setApproachingElevator(int floorNumber, int elevatorID){
		//Calls on the BuildingFloors inner method to set the approaching elevator.
		this.floors[floorNumber]._setApproachingElevator(elevatorID);
	}
	
	public synchronized void setPassengerRequests(int floorNumber, int destinationFloor, int numPassengers){
		//Calls on the BuildingFloor inner method to set the number of passengers spawned on a specific floor as well as their destination.
		this.floors[floorNumber]._setPassengerRequests(destinationFloor, numPassengers);
	}
	
	public synchronized int checkFloorStatus(){
		//Synchronized method to allow each elevator to check if passengers need to be picked up
		for (int i = 0; i < this.floors.length; i++){ //Iterate through all the floors
			if (this.floors[i]._getApproachingElevator() == -1){
				for (int j = 0; j < this.floors.length; j++){ //Iterate through all the passenger destination requests for each floor
					if (this.floors[i]._getPassengerRequests(j) != 0){
						this.floors[i]._setApproachingElevator(i);
						return i; //returns the floor that the elevator will pick up passengers from
					}
				}
			}
		}
		return -1; //-1 will be used that there are no passengers that need to be picked up on any floor
	}

}
