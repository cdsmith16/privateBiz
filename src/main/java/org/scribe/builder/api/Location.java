package org.scribe.builder.api;


public class Location {
	
	double latitude = -1;
	double longitude = -1;
	private double locationScore;
	private double valueScore;
	Location parentLocation = null;
	private String address;
	private final String USER_AGENT = "Mozilla/5.0";
	private final double SF_LOC_TOTAL = 333;
	private final double AMT_PER_TRANS = 5;
	private final double SF_NUM_TRANS = 1000;
	private final double SF_MAX_DENSITY = 50992.59259;
	private CSVReader reader;;
	
	public Location(double latitude, double longitude, CSVReader reader) {
		this.latitude = latitude;
		this.longitude = longitude;
		try {
			this.locationScore = calcLocScore(latitude, longitude);
			this.valueScore = calcValScore(latitude,longitude);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.reader = reader;
	}
	
	public Location(double latitude, double longitude, double locationScore, double valueScore){
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationScore = locationScore;
		this.valueScore = valueScore;
	}
	
	private double calcLocScore(double latitude, double longitude) throws Exception {
		double total = 0;
		double score = 0;
		
		//Ping Yelp for number of similar businesses nearby
		Yelp api = new Yelp();
		total = api.locScore("coffee", latitude, longitude);
		//union square SF coordinates of 37.7881° N, 122.4075° W returned 333 coffee shops
		score = (1-total/SF_LOC_TOTAL);
		//System.out.println("Whoa! This locationScore is " + score);
		
		return score;
	}
	
	private double amtPerTrans(){
		//for now assume all transactions are $5
		return AMT_PER_TRANS;
	}
	
	private double numTrans(){return 0;}
	
	private double calcValScore(double latitude, double longitude) throws Exception{
		double score = 0;
		
		//Ping Google for Zip Code of location
		GoogleZip gzip = new GoogleZip();
		int zip = gzip.GetZipCode(latitude, longitude);
		//System.out.println("Also, the zip code here is " + zip);
		
		//Consult database for population density of said Zip Code
		double density = 0;
		density = reader.GetDensityMap().get(zip);
		double densityFactor = (density/SF_MAX_DENSITY);
		
		//calculate score based on density/walkscores etc.
		String formAddress = gzip.GetFormAddress();
		WalkScore ws = new WalkScore();
		double walkScore = (double) ws.GetWalkScore(formAddress, latitude, longitude);
		SetAddress(formAddress);
		
		//tap into transit score later, but for now, calc score based on the above
		
		//calc numTrans
		double numTrans = SF_NUM_TRANS*(densityFactor + (Math.pow((1-densityFactor),2)*(walkScore/100)));
		//System.out.println("This location should have about "+numTrans+" transactions per day.");
		
		//calc amtTrans
		double amtTrans = AMT_PER_TRANS;
		
		score = numTrans * amtTrans;
		
		return score;
	}
	
	private void SetAddress(String address){
		this.address = address;
		return;
	}
	
	public String GetAddress(){
		return address;
	}
	
	public double GetLocationScore(){
		if(locationScore<0){
			System.out.println("Location Score not calculated or yet-to-be initialized");
		}
		return locationScore;
	}
	
	public double GetValueScore(){
		if(valueScore<0){
			System.out.println("Value Score not calculated or yet-to-be initialized");
		}
		return valueScore;
	}
	
	public double Favorability(){
		if(locationScore>0 && valueScore>0) {
			double score = locationScore * valueScore;
			//System.out.println(score);
			return score;
		}
		//System.out.println("Scores not calculated yet");
		return -1;
	}

	public Location clone(){
		Location clone = new Location(this.latitude,this.longitude, this.locationScore, this.valueScore);
		return clone;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Sample location center of Oakland
		//Location oakland = new Location(37.8044,-122.2708);
		//Location sf = new Location(37.7881, -122.4075);
	}

}
