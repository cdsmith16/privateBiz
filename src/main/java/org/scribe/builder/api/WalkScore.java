package org.scribe.builder.api;

import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class WalkScore {
	
	public WalkScore() {
		
	}
	
	private String addressConverter(String googleAddress){
		StringBuffer buffer = new StringBuffer();
		//hack to get rid of ", USA" from googleAddress
		googleAddress = googleAddress.substring(0, googleAddress.lastIndexOf(','));
		for(int i = 0; i<googleAddress.length(); i++){
			if(googleAddress.charAt(i)==','){
				continue;
			} else if(googleAddress.charAt(i)==' '){
				buffer.append("%20");
			} else {
				buffer.append(googleAddress.charAt(i));
			}
		}
		String converted = buffer.toString();
		System.out.println(converted);
		return converted;
	}
	
	public int GetWalkScore(String formAddress, double latitude, double longitude) throws Exception{
		int walkScore = 0;
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.walkscore.com/score?format=json");
	    
		//Create latlng string in WalkScore format
		String wsapikey = "ffd1c56f9abcf84872116b4cc2dfcf31";
		request.addQuerystringParameter("wsapikey", wsapikey);
		//quick and dirty string conversion
		request.addQuerystringParameter("lat", ""+latitude);
		request.addQuerystringParameter("lon", ""+longitude);
		String walkAddress = addressConverter(formAddress);
		request.addQuerystringParameter("address",walkAddress);
		
		Response response = request.send();
		
		JSONObject json = new JSONObject(response.getBody());
		if(json.getInt("status")>1) {
			System.out.println("WalkScore website no fun v_v");
			walkScore = 100;
		} else {
			walkScore = json.getInt("walkscore");
		}
		System.out.println("This is the walkscore in your neighborhood! " + walkScore);
		return walkScore;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
