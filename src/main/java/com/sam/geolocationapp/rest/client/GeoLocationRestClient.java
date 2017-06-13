package com.sam.geolocationapp.rest.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;

@Component
public class GeoLocationRestClient {
	
	public static final Logger LOGGER = LogManager.getLogger(GeoLocationRestClient.class);

	@Value("${com.sam.geolocation.geoLocationApi.url}")
	protected String geoLocationApiUrl;
	
	@Value("${com.sam.geolocation.geoLocationApi.parameter}")
	protected String geoLocationApiParameter;
	
	@Value("${com.sam.geolocation.geoLocationApi.method}")
	protected String geoLocationApiMethod;
	
	public String callGeoLocationRestApi(String pinCode) 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationRestClient:callGeoLocationRestApi : Starts Executing : with : pinCode : " +pinCode);
		
		String geloLocationApiResponseString = "";
		StringBuilder geloLocationApiResponseBuilder = new StringBuilder(256);
		
		HttpURLConnection httpURLConnection = null;
		URL geloLocationApiUrl = null;
		String populatedGeoLocationApiUrl = null;
		
		long startTime = 0L;
		long endTime = 0L;
		try {
			populatedGeoLocationApiUrl = geoLocationApiUrl +GeoLocationAppConstants.QUESTION_MARK_DELIMITER_VALUE 
										 +geoLocationApiParameter +GeoLocationAppConstants.EQUALS_DELIMITER_VALUE +pinCode;
			LOGGER.info("In GeoLocationRestClient:callGeoLocationRestApi : populatedGeoLocationApiUrl : " +populatedGeoLocationApiUrl);
			geloLocationApiUrl = new URL(populatedGeoLocationApiUrl);
			httpURLConnection = (HttpURLConnection) geloLocationApiUrl.openConnection();
			httpURLConnection.setRequestMethod(geoLocationApiMethod);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestProperty(GeoLocationAppConstants.GEO_LOCATION_API_CONTENT_TYPE_KEY, GeoLocationAppConstants.GEO_LOCATION_API_CONTENT_TYPE_VALUE);
			httpURLConnection.setRequestProperty(GeoLocationAppConstants.GEO_LOCATION_API_ACCEPT_KEY, GeoLocationAppConstants.GEO_LOCATION_API_ACCEPT_VALUE);
			
			startTime = System.currentTimeMillis();
			if(httpURLConnection.getResponseCode() == GeoLocationAppConstants.GEO_LOCATION_API_SUCCESS_CODE_VALUE) {
				try (BufferedReader responseBufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))){
					String line = null;
					while((line= responseBufferedReader.readLine()) != null) {
						geloLocationApiResponseBuilder.append(line);
					}
					endTime = System.currentTimeMillis();
					geloLocationApiResponseString = geloLocationApiResponseBuilder.toString();
							
				} catch (Exception e) {
					throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
				}
			} else {
				endTime = System.currentTimeMillis();
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_117);
			}
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (UnknownHostException uhe) {
			throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_120);
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationRestClient:callGeoLocationRestApi : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
	
		return geloLocationApiResponseString;
	}
	
}
