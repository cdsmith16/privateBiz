package org.scribe.builder.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * private key password notasecret
 * apikey AIzaSyDMW3uke9ymgqC6r4JQb3cFMK28-zs-E2g
 * 
 * @author Mom
 *
 */

public class GoogleZip {
	String formAddress;
	
	public GoogleZip() {
		// TODO Auto-generated constructor stub
	}
	private final String USER_AGENT = "Mozilla/5.0";
	
	private OAuthService createService() {
		OAuthService service = new ServiceBuilder()
		.provider(GoogleApi.class)
		.apiKey("AIzaSyD3GGMyxXRpEk2VZKPwaWa65kecYVa25fQ")
		.build();
		return service;
	}
	
	/**
	 * Get formatted address.  Method will only return useful value after
	 * 'GetZipCode' is run. (I know, I know... :/ )
	 */
	public String GetFormAddress(){
		if(formAddress!=null) {
			return this.formAddress;
		} else {
			System.out.println("Did you Ping Google with 'GetZipCode' yet?");
			return null;
		}
	}
	
	public int GetZipCode(double latitude, double longitude) throws Exception {
		//do math based on longitude and latitude and calculate location score
		int zip = 0;
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://maps.googleapis.com/maps/api/geocode/json?");
		//OAuthService service = this.createService();
	    
		//Create latlng string in Google format
		String latlng = (""+latitude+","+longitude);
		request.addQuerystringParameter("latlng", latlng);
		request.addQuerystringParameter("result_type", "postal_code");
		request.addQuerystringParameter("sensor", "false");
	    request.addQuerystringParameter("key", "AIzaSyD3GGMyxXRpEk2VZKPwaWa65kecYVa25fQ");
		
	    Response response = request.send();
		/**
		 * Ridiculous amount of manual parsing here, but alas it's finally solved.
		 * Definitely come back and clean up/parse more efficiently at some point.
		 */
		//System.out.println(response.getBody());
		JSONObject json = new JSONObject(response.getBody());
		JSONArray results = (JSONArray) json.get("results");
		JSONObject addComps = results.getJSONObject(0);
		JSONArray zipArray = (JSONArray) addComps.get("address_components");
		JSONObject shortZip = zipArray.getJSONObject(0);//get zips from "address components"
		String formAddress = (String) (addComps.get("formatted_address"));//get formatted address
		//System.out.println(formAddress);
		this.formAddress = formAddress;
		zip = shortZip.getInt("short_name");//get zip code value
		return zip;
	}

}
