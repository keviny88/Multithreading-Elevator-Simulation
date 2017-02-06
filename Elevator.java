// Wajih Yassine 42139560, Kevin Yin 29211757
import java.util.ArrayList;

public class Elevator implements Runnable {
	//Multi threaded class representing an elevator and its behavior
	BuildingManager manager; //BuildingManager object that is shared among all elevators. Handles race conditions via locking/synchronized mechanisms.
	int elevatorID; // A unique ID (0-4) for a specific elevator
	int currentFloor; //The current floor the elevator is on
	int numPassengers; //The current number of passengers in the elevator
	int totalLoadedPassengers; //Total number of passengers this elevator loaded.
	int totalUnloadedPassengers; //Total number of passengers this elevator unloaded.
	int[] passengerDestinations; //An array where the i element represents the number of current passengers who's destination is the i floor.
	ArrayList<ElevatorEvent> moveQueue = new ArrayList<ElevatorEvent>(); //Contains ElevatorEvents that define the movement of an elevator and the anticipated time of arriving at the destination.
	
	public Elevator(int elevatorID, BuildingManager manager){
		//Constructor that initializes the elevator object with a elevator id and the shared BuildingManager object.
		this.elevatorID = elevatorID;
		this.manager = manager;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()){
			/*The following block of code represents the elevators IDLE state, to which it is looking for passengers to pickup.
			 * Once passengers on a floor are found. A pickup request will be created and the program will move to the PICKUP stage (next block of code).
			 */
	
			if (this.moveQueue.size() == 0 && this.numPassengers == 0){ //IDLE
				int foundFloor = this.manager.checkFloorStatus();
				if (foundFloor != -1){
					System.out.println("|" + SimClock.getTime() + "| |PICKUP REQUEST| |DESCRIPTION: elevator " + this.elevatorID + " heading to floor " + foundFloor +"|");
					createElevatorEvent(foundFloor, 0);
				}
			}
			/*The following block of code will handle the simulation state at which the elevator has arrived at the pickup floor.
			 * It will load all the passengers, handle whether passengers are going up or down and update the necessary states 
			 * in order to go to the third stage, the elevator event processing
			 */
			if (this.moveQueue.size() == 1 && this.numPassengers == 0){ //PICKUP 
				if (this.moveQueue.get(0).getExpectedArrival() == SimClock.getTime()){
					System.out.println("|" + SimClock.getTime() + "| |PICKUP ARRIVAL| |DESCRIPTION: elevator " + this.elevatorID + " arrived on floor " + this.moveQueue.get(0).getDestination() + "|");
					
					int pickupFloor = this.moveQueue.get(0).getDestination();//Original floor elevator picked up from.
					int extraTime = 20;//Initial loading time plus unloading for drop off floor, any additional drop off floors will only be incremented by 10.
					boolean onlyDown = onlyRequestingDown(pickupFloor); //Check if all passengers are going down
					this.currentFloor = pickupFloor; //Update the state of the currentfloor
					this.passengerDestinations = new int[5]; //Can assume that if it is a pickup request, then no passengerDestinations should be set yet.
					this.moveQueue.remove(0); //Remove the pickup request, since it has been processed.
					
					for (int i = (this.manager.getFloorSize() - 1); i >=0; i--){
						int passengersWaiting = this.manager.getPassengerRequests(pickupFloor, i);
						//Case when onlyDown is true, which indicates all passengers will be going down.
						if (passengersWaiting != 0 && onlyDown){
							this.passengerDestinations[i] = passengersWaiting;
							this.totalLoadedPassengers += passengersWaiting;
							this.numPassengers += passengersWaiting;
							this.manager.setPassengerRequests(pickupFloor, i, 0); //Since passengers have been loaded, update the floor state
							createElevatorEvent(i, extraTime);
							extraTime += 10;
						//Case when onlyDown did not evaluate to true. Also takes into account that one passenger could be requesting down and to skip that passenger.	
						}else if (passengersWaiting != 0 && (i - this.currentFloor) > 0){
							this.passengerDestinations[i] = passengersWaiting;
							this.totalLoadedPassengers += passengersWaiting;
							this.numPassengers += passengersWaiting;
							this.manager.setPassengerRequests(pickupFloor, i, 0); //Since passengers have been loaded, update the floor state
							createElevatorEvent(i, extraTime);
							extraTime += 10;
						}
					}
					System.out.println("|" + SimClock.getTime() + "| |DROPOFF REQUEST| |DESCRIPTION: elevator: " + this.elevatorID + " heading to floor " + this.moveQueue.get(0).getDestination() + " for dropoff|");
					this.manager.setApproachingElevator(pickupFloor, -1); //Once done picking up the passengers, set it to -1 so all other elevators can pick up passengers from the floor
				}
			}
			/*The last while statement deals with processing of the elevator events. Once the SimClock equals the expected arrival time, the event will be processed.
			 * Processing the event will also update states such as the current floor, number of passengers, number of total arrival passengers and total unloaded passengers
			 */
			
			if (this.moveQueue.size() != 0 && this.numPassengers != 0){ //PROCESS
				
				ElevatorEvent event = this.moveQueue.get(0);
				if (SimClock.getTime() == event.getExpectedArrival()){ //Having a while statement here prevents the event from printing out an unnecessary amount of time.
					processElevatorEvent(event);;
				}
			
			}
		}
	}
	
	public void processElevatorEvent(ElevatorEvent event){
		//Handles the processing of the elevator event. It also updates all the states.
		System.out.println("|" + SimClock.getTime() + "| |DROPOFF ARRIVAL| |DESCRIPTION: elevator: " + this.elevatorID + " dropped off " + this.passengerDestinations[event.getDestination()] + " passengers on floor " +event.getDestination() +"|");
		int unloadedPassengers = this.passengerDestinations[event.getDestination()];
		this.currentFloor = event.getDestination();
		this.totalUnloadedPassengers += unloadedPassengers;
		this.numPassengers -= unloadedPassengers;
		this.manager.setTotalArrivalPassengers(event.getDestination(), unloadedPassengers, this.elevatorID);
		if (this.moveQueue.size() >1){ //If there is another destination print the event out
			System.out.println("|" + SimClock.getTime() + "| |DROPOFF REQUEST| |DESCRIPTION: elevator: " + this.elevatorID + " heading to floor " + this.moveQueue.get(1).getDestination() + " for dropoff|");
		}
		this.moveQueue.remove(0);
	}
	
	public void createElevatorEvent(int floorNumber, int extraTime){
		//Method that creates an elevator event
		ElevatorEvent event = new ElevatorEvent();
		int expectedArrival = SimClock.getTime() + Math.abs(5*(this.currentFloor-floorNumber)) + extraTime; //Absolute value to take into account elevators going up or down.
		event.setDestination(floorNumber);
		event.setExpectedArrival(expectedArrival);
		this.moveQueue.add(event);
	}
	
	public boolean onlyRequestingDown(int floorNumber){
		//Checks to see if there are any passengers going up. If not, all passengers are going down.
		for (int i = (this.manager.getFloorSize() - 1); i >=0; i--){
			int passengersWaiting = this.manager.getPassengerRequests(floorNumber, i);
			if (passengersWaiting != 0 && i - floorNumber > 0){
				return false; //Case when there is at least one passenger trying to go up
			}
		}
		return true; //Case when there are no passengers trying to go up
	}

}
