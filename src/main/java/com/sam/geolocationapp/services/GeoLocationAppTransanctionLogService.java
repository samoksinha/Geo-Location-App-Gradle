package com.sam.geolocationapp.services;

import java.util.List;
import java.util.Map;

import com.sam.geolocationapp.entities.ShopDetails;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.models.GeoLocationAppRequest;

/**
 * @author Samok Sinha
 * 
 * This is the Business interface which abstract the functionality of the Geo Location Application
 * in a central place.
 */

public interface GeoLocationAppTransanctionLogService {

	public boolean insertTransanctionLog(String emailId,  String uniqueLogId, String accessToken, String tokenType, String requestType,
							  			 String operationName,  String requestTime, String requestBody, String clientIp) 
							  					 throws GeoLocationAppException;
	public boolean updateTransanctionLog(String uniqueLogId,  String responseTime, String responseBody) 
			throws GeoLocationAppException;
	
	public Map<String, GeoLocationAppRequest> addShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
	public boolean updateShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
	public ShopDetails findShop(GeoLocationAppRequest currentVersionRequest) throws GeoLocationAppException;
	
	public List<GeoLocationAppRequest> findAllShops(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
	public GeoLocationAppRequest populateShopCoordinates(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
}
