package com.sam.geolocationapp.rest.client;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.models.GeoLocationAppRequest;

/**
 * @author Samok Sinha
 * 
 * This Class is responsible for integrating with GeoCoding API using GeoCoding Context.
 * This class sends Address and Pin Code to the GeoCoding API and retrieves the latitude
 * and longitude information.
 * 
 */

@Component
public class GeoCodeClient {
	
	public static final Logger LOGGER = LogManager.getLogger(GeoCodeClient.class);

	@Value("${com.sam.geolocation.geoCode.key}")
	protected String geoCodeApiKey;
	
	@Value("${com.sam.geolocation.geoCode.queryLimit}")
	protected String geoCodeQueryLimit;
	
	@Value("${com.sam.geolocation.geoCode.connectionTimeOut}")
	protected String geoCodeConnectionTimeOut;
	
	@Value("${com.sam.geolocation.geoCode.readTimeout}")
	protected String geoCodeReadTimeout;
	
	@Value("${com.sam.geolocation.geoCode.writeTimeout}")
	protected String geoCodeWriteTimeout;
	
	private GeoApiContext geoApiContext;
	
	/**
	 * @throws GeoLocationAppException
	 * 
	 * It is called when this Bean is initialized by the Spring Container to set the necessary
	 * Geocoding Context and Configuration.
	 * 
	 */
	@PostConstruct
	public void init() 
			throws GeoLocationAppException {
		LOGGER.info("In GeoCodeClient:init : Initializing GeoCodeClient Bean with GeoCode Context :");
		
		try {
			geoApiContext = new GeoApiContext();
			geoApiContext.setApiKey(geoCodeApiKey);
			geoApiContext.setQueryRateLimit(Integer.parseInt(geoCodeQueryLimit));
			geoApiContext.setConnectTimeout(Integer.parseInt(geoCodeConnectionTimeOut), TimeUnit.SECONDS);
			geoApiContext.setReadTimeout(Integer.parseInt(geoCodeReadTimeout), TimeUnit.SECONDS);
			geoApiContext.setWriteTimeout(Integer.parseInt(geoCodeWriteTimeout), TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
		LOGGER.info("In GeoCodeClient:init : Successfully initialized GeoCodeClient Bean with GeoCode Context :");
	}
	
	/**
	 * @param geoLocationAppRequest
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It calls the GeoCoding API with Shop Address and Shop Pin Code to retrieve the shop Latitude and Longitude
	 * information for further process and returned it.
	 */
	public GeocodingResult[] callGeoCodeApi(GeoLocationAppRequest geoLocationAppRequest) 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationRestClient:callGeoCodeApi : Starts Executing : with : geoLocationAppRequest : " +geoLocationAppRequest);
		
		long startTime = 0L;
		long endTime = 0L;
		
		GeocodingResult[] geocodingResultArray = null;
		try {
			geocodingResultArray = GeocodingApi.newRequest(geoApiContext)
													.address(geoLocationAppRequest.getShopAddress())
													.components(ComponentFilter
																.postalCode(String.valueOf(geoLocationAppRequest.getShopPincode())))
													.await();
			endTime = System.currentTimeMillis();
		} catch (InvalidRequestException ire) {
			throw new GeoLocationAppException(ExceptionType.INTERNAL, ErrorCodes.ERROR_CODE_117);
		} catch (SocketTimeoutException ste) {
			throw new GeoLocationAppException(ExceptionType.INTERNAL, ErrorCodes.ERROR_CODE_120);
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.INTERNAL, ErrorCodes.ERROR_CODE_118);
		} finally {
			LOGGER.info("In GeoLocationRestClient:callGeoCodeApi : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
	
		return geocodingResultArray;
	}
	
}
