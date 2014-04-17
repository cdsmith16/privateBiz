
package org.scribe.builder.api;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.json.JSONObject;

public class Yelp
{
	/**
	 * Important shit for Yelp API
	 * @author Smooth
	 * @param apiKey QIjfKyScITxLzAAmU3qZ6A
	 * @param apiSecret	equcbAvMoQz6Z6s9ZG7i3ITYB9c
	 * @param token Token	BiMe82nfa5aJe19O5pWJW4H9otFcjtBF
	 * @param tokenSecret Token Secret	8el7lKYUAxqCHytTLWKjfquLVU4
	 *
	 *YWSID for Yelp API 1.0
	 *PB0JvfJwjZ2LzkmL1a0tEg
	 */
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	public OAuthService createService() {
		OAuthService service = getService("QIjfKyScITxLzAAmU3qZ6A","equcbAvMoQz6Z6s9ZG7i3ITYB9c");
		return service;
	}
	
	private OAuthService getService(String apiKey, String apiSecret){
		OAuthService service = new ServiceBuilder()
	    .provider(YelpApi2.class)
	    .apiKey(apiKey)
	    .apiSecret(apiSecret)
	    .build();
		return service;
	}

	public Token requestToken(String token, String tokenSecret){
		Token requestToken = new Token(token, tokenSecret);
		return requestToken;
	}
	
	public String authUrl(OAuthService service, Token requestToken){
		String authUrl = service.getAuthorizationUrl(requestToken);
		return authUrl;
	}
	
	public int locScore(String term, double latitude, double longitude) throws Exception {
		//do math based on longitude and latitude and calculate location score
		int total = 0;
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		OAuthService service = this.createService();
		Token token = this.requestToken("BiMe82nfa5aJe19O5pWJW4H9otFcjtBF","8el7lKYUAxqCHytTLWKjfquLVU4");
		
		
		//for now, search term entered as "coffee" and bounds set to +/ 1 mile
	    
		request.addQuerystringParameter("term", term);
	    request.addQuerystringParameter("bounds", (latitude-.01) + "," + (longitude-.01) + "|" + (latitude+.01) + "," + (longitude + .01));
	    service.signRequest(token, request);
	    Response response = request.send();
		
		//System.out.println(response.getCode());
		JSONObject json = new JSONObject(response.getBody());
		total = (Integer) (json.get("total"));
		return total;
	}
}
