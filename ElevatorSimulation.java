// Wajih Yassine 42139560, Kevin Yin 29211757
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElevatorSimulation extends Thread{
	//Class that deals with main functionality of elevator simulation. Handles reading the configuration file, handling elevator threads, starting and stopping the simulation.
	int totalSimulationTime; //Tracks the total time of the simulation
	int simulationRate; //Tracks the amount of time the thread should sleep before ticking the simulation clock
	BuildingManager manager = new BuildingManager(); //initializes the BuildingManager object that will be shared among all elevators
	//Sets up elevator objects
	Elevator elevator0 = new Elevator(0, manager);
	Elevator elevator1 = new Elevator(1, manager);
	Elevator elevator2 = new Elevator(2, manager);
	Elevator elevator3 = new Elevator(3, manager);
	Elevator elevator4 = new Elevator(4, manager);
	ArrayList<ArrayList<PassengerArrival>> passArrivalArr = new ArrayList<ArrayList<PassengerArrival>>(); //Stores all the passenger arrival objects to be iterated through.
	
	public void start(){
		//Start of simulation
		//Get the specification from the ElevatorConfig.txt file
		try{
			File inputFile = new File("ElevatorConfig.txt");
			Scanner inputStream = new Scanner(inputFile);
			getPassengerArrivals(inputStream); 
		} catch(FileNotFoundException e) { System.out.println("ERROR. File Not Found.");}

		
		//Setups up threads for each elevator object
		Thread elevatorThread0 = new Thread(this.elevator0);
		Thread elevatorThread1 = new Thread(this.elevator1);
		Thread elevatorThread2 = new Thread(this.elevator2);
		Thread elevatorThread3 = new Thread(this.elevator3);
		Thread elevatorThread4 = new Thread(this.elevator4);
		
		//Starts each elevator thread
		elevatorThread0.start();
		elevatorThread1.start();
		elevatorThread2.start();
		elevatorThread3.start();
		elevatorThread4.start();
		
		
		while (SimClock.getTime() <= this.totalSimulationTime) { //Main loop that will continue until the total simulation time defined in ElevatorCofig.txt ends	
			//Nested for loop that deals with passenger spawn times and handling to the associated building floor.
			for (int i = 0; i < this.passArrivalArr.size(); i++){ //Loop through each array in passArrivalArr
				ArrayList<PassengerArrival> passengerState = this.passArrivalArr.get(i); //Grab individual arrays
				for (int j =0; j < passengerState.size(); j++){
					PassengerArrival passengerArrival = passengerState.get(j);
					if (passengerArrival.getExpectedTimeOfArrival() == SimClock.getTime()){ //Check if its time to spawn passengers
						System.out.println("|" + SimClock.getTime() + "| |PASSENGER SPAWN| |DESCRIPTION: " + passengerArrival.getNumPassengers() + " passengers arrived on floor " + i + " and are requesting to go to floor " + passengerArrival.getDestinationFloor()+ "|");
						this.manager.setPassengerRequests(i, passengerArrival.getDestinationFloor(), passengerArrival.getNumPassengers());
						this.manager.setTotalDestinationRequests(i, passengerArrival.getDestinationFloor(), passengerArrival.getNumPassengers());
						passengerArrival.setExpectedArrivalTime(passengerArrival.getExpectedTimeOfArrival() + passengerArrival.getTimePeriod());
					}
				}
			}
			
			try{
				Thread.sleep(this.simulationRate);
				
			} catch (InterruptedException e){}
			
			SimClock.tick();
		}
		
		//Print the building state and interrupt the threads to terminate the simulation
		printBuildingState();
		elevatorThread0.interrupt();
		elevatorThread1.interrupt();
		elevatorThread2.interrupt();
		elevatorThread3.interrupt();
		elevatorThread4.interrupt();
	}
	
	public void printBuildingState(){
		//Prints out the total statistics at the end of the simulation
		System.out.println("\n***SIMULATION STATISTICS***");
		
		//Following for loop will print out the totals from each of the floors
		System.out.println("TOTAL DESTINATION & ARRIVAL REQUESTS STATS");
		for (int i = 0; i < this.manager.getFloorSize(); i++){
			System.out.println("\tFLOOR " + i);
			for (int j=0; j < this.manager.getFloorSize(); j++){
				System.out.println("\tDESTINATION: " + this.manager.getTotalDestinationRequests(i, j) + " PASSENGERS REQUESTING FLOOR " + j + " ARRIVAL: " + this.manager.getTotalArrivalPassengers(i, j) + " PASSENGERS FROM ELEVATOR " + j);
			}
		}
		
		//Following code block will print out the totals from each of the elevators
		System.out.println("\nELEVATOR STATS");
		System.out.println("ELEVATOR 0: TOTAL LOADED PASSENGERS " + this.elevator0.totalLoadedPassengers + " TOTAL UNLOADED PASSENGERS " + this.elevator0.totalUnloadedPassengers);
		System.out.println("ELEVATOR 1: TOTAL LOADED PASSENGERS " + this.elevator1.totalLoadedPassengers + " TOTAL UNLOADED PASSENGERS " + this.elevator1.totalUnloadedPassengers);
		System.out.println("ELEVATOR 2: TOTAL LOADED PASSENGERS " + this.elevator2.totalLoadedPassengers + " TOTAL UNLOADED PASSENGERS " + this.elevator2.totalUnloadedPassengers);
		System.out.println("ELEVATOR 3: TOTAL LOADED PASSENGERS " + this.elevator3.totalLoadedPassengers + " TOTAL UNLOADED PASSENGERS " + this.elevator3.totalUnloadedPassengers);
		System.out.println("ELEVATOR 4: TOTAL LOADED PASSENGERS " + this.elevator4.totalLoadedPassengers + " TOTAL UNLOADED PASSENGERS " + this.elevator4.totalUnloadedPassengers);
	}
	
	public void getPassengerArrivals(Scanner inputStream) throws FileNotFoundException{
		this.totalSimulationTime = Integer.parseInt(inputStream.nextLine()); //Grabs the first line which is the total simulation time
		this.simulationRate = Integer.parseInt(inputStream.nextLine());//Grabs the second line which is the simulation rate.
		
		while (inputStream.hasNextLine()){//Iterate through each line which represents a floor
			String[] line = inputStream.nextLine().split(";");
			ArrayList<PassengerArrival> tmpPass = new ArrayList<PassengerArrival>();
			for (int i = 0; i <line.length; i++){
				String[] splitSpace = line[i].split(" ");
				PassengerArrival passArr = new PassengerArrival(new Integer(splitSpace[0]), new Integer(splitSpace[1]), new Integer(splitSpace[2])); //Create each passenger arrival object
				tmpPass.add(passArr);
			}
			this.passArrivalArr.add(tmpPass);
		}
		inputStream.close(); //Close the scanner object when finished
	}	
}
