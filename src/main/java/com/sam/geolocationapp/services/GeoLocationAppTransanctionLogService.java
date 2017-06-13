package com.sam.geolocationapp.services;

import java.util.List;

import com.sam.geolocationapp.entities.ShopDetails;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.models.GeoLocationAppRequest;

public interface GeoLocationAppTransanctionLogService {

	public boolean insertToDb(String emailId,  String uniqueLogId, String accessToken, String tokenType, String requestType,
							  String operationName,  String requestTime, String requestBody, String clientIp) 
			throws GeoLocationAppException;
	public boolean updateToDb(String uniqueLogId,  String responseTime, String responseBody) 
			throws GeoLocationAppException;
	
	public GeoLocationAppRequest addShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
	public boolean updateShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
	public ShopDetails findShop(String shopName) throws GeoLocationAppException;
	public List<GeoLocationAppRequest> findAllShops(String latitude, String longitude) throws GeoLocationAppException;
	public GeoLocationAppRequest populateShopCoordinates(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException;
}
