package org.scribe.builder.api;
import org.w3c.dom.Node;


public class LocationStack {

	Location worstSpot = null;
	Location bestSpot = null;
	private int numSpots = 0;
	private final static int MAX_SPOTS = 20;
	private static CSVReader reader;
	
	public LocationStack(Location location){
		worstSpot = location;
		numSpots = 1;
	}
	
	public LocationStack(double latitude, double longitude){
		reader = new CSVReader("hi!");
		worstSpot = new Location(latitude,longitude, reader);
		numSpots = 1;
	}
	
	public LocationStack(Location[] locations){
		worstSpot = locations[0];
		numSpots = 1;
		for(int i=1; i<locations.length; i++){
			Push(locations[i]);
			numSpots++;
		}
	}
	
	public Location Pop(){
		if(worstSpot==null){
			System.out.println("Location Stack is empty");
			return null;
		}
		Location tempLoc = worstSpot.clone();
		worstSpot = worstSpot.parentLocation;
		numSpots--;
		return tempLoc;
	}
	/**
	 * Formula comparing input location to current 'worst' spot.
	 * If the location is less favorable than the worst spot, we 
	 * simply set the parent to the worst spot. If the location is
	 * more favorable, however, we insert the location into our linked-list/stack
	 * and continue until finding a spot more favorable than 'location'.
	 * Once all is finished, we add to the number of total spots, and 
	 * if the spots is higher than desired, we pop the last spot off of
	 * the stack.
	 * @param location
	 */
	public void Push(Location location){
		Location curSpot = worstSpot;
		Location oldSpot = curSpot;
		while(location.Favorability()>curSpot.Favorability()){
			//if curSpot is the best spot
			if(curSpot.parentLocation==null){
				curSpot.parentLocation = location;
				numSpots++;
				if(numSpots>MAX_SPOTS) {
					Pop();
				}
				return;
			}
			oldSpot = curSpot;
			curSpot = curSpot.parentLocation;
		}
		oldSpot.parentLocation = location;
		location.parentLocation = curSpot;
		numSpots++;
		if(numSpots>MAX_SPOTS) {
			Pop();
		}
		return;
	}
	
	private Location findBestSpot(){
		Location best = worstSpot;
		while(best.parentLocation!=null){
			best = worstSpot.parentLocation;
		}
		return best;
	}
	
	public Location GetBestSpot(){
		bestSpot = findBestSpot();
		return bestSpot;
	}
	
	private static Location[] GetOaklandLocations(int width, int length, double centerX, double centerY, double scalar){
		reader = new CSVReader("hi!");
		Location[] locations = new Location[width*length];
		double startLat = (centerX-(width/2)*scalar);
		double startLon = (centerY-(length/2)*scalar);
		for(int j=0; j<length; j++){
			for(int i=0; i<width; i++){
				locations[i+j*width]= new  Location(startLat + i*scalar, startLon + j*scalar, reader);
			}
		}
		return locations;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//for hella spots in Oakland
		Location[] locations = GetOaklandLocations(4,4,37.8044,-122.2708, .05);
		LocationStack stack = new LocationStack(locations);
		Location best = stack.GetBestSpot();
		System.out.println("Favorability is: " + best.Favorability() + " at this location! ");
		System.out.println(best.GetAddress());
		//Location oakland = new Location(37.8044,-122.2708);

	}

}
