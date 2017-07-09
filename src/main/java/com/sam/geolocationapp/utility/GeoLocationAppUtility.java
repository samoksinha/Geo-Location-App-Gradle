package com.sam.geolocationapp.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.geolocationapp.entities.ShopDetails;
import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.exceptions.GeoLocationAppFaultInfo;
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.models.GeoLocationAppResponse;

/**
 * @author Samok Prasad Sinha
 *
 * This class provides various utility functionality used through out the application.
 */

public class GeoLocationAppUtility {
	
	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppUtility.class);
	
	public GeoLocationAppUtility() {
	}
	
	/**
	 * This method provides common String validation like whether the data is null or empty.
	 * @param inputData (User input data that needs validation)
	 * @return validateFlag (Returns validation status of the User input data)
	 */
	public static boolean validateString(String inputData) {
		boolean validateFlag = false;
		try {
			if(null != inputData && !inputData.isEmpty()) {
				validateFlag = true;
			}
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
		return validateFlag;
	}
	
	/**
	 * This method provides common Object(Map, List, Set) validation like whether the data is null or empty.
	 * @param inputData (User input data that needs validation)
	 * @return validateFlag (Returns validation status of the User input data)
	 */
	public static boolean validateObject(Object inputObject) {
		boolean validateFlag = false;
		try {
			if(inputObject instanceof Map) { 
				Map<?,?> inputMap = (Map<?,?>) inputObject;
				if(null != inputMap && !inputMap.isEmpty()) {
					validateFlag = true;
				}
			} else if(inputObject instanceof List) {
				List<?> inputList = (List<?>) inputObject;
				if(null != inputList && !inputList.isEmpty()) {
					validateFlag = true;
				}
			} else if(inputObject instanceof Object) { 
				if(null != inputObject) {
					validateFlag = true;
				}
			} else if(inputObject instanceof Properties) { 
				Properties properties = (Properties) inputObject;
				if(null != properties && !properties.isEmpty()) {
					validateFlag = true;
				}
			}
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
		return validateFlag;
	}
	
	/**
	 * @param exceptionType
	 * @param errorCode
	 * @return
	 * 
	 * It resolves error messages bases on Exception Type and error code from error map and returns 
	 * GeoLocationAppFaultInfo model object.
	 */
	public static GeoLocationAppFaultInfo resolveErrorMessage(ExceptionType exceptionType, int errorCode) {
		
		GeoLocationAppFaultInfo geoLocationAppFaultInfo = new GeoLocationAppFaultInfo();
		if(exceptionType.getExceptionType().equalsIgnoreCase(ExceptionType.BUSINESS.getExceptionType())
				|| exceptionType.getExceptionType().equalsIgnoreCase(ExceptionType.INTERNAL.getExceptionType())) {
			geoLocationAppFaultInfo.setErrorCode(errorCode);
			geoLocationAppFaultInfo.setErrorMessage(GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(errorCode)));
		}
		return geoLocationAppFaultInfo;
	}
	
	/**
	 * @param exceptionType
	 * @return
	 * 
	 * It resolves error messages bases on Exception Type from error map and returns 
	 * GeoLocationAppFaultInfo model object.
	 */
	public static GeoLocationAppFaultInfo resolveErrorMessage(ExceptionType exceptionType) {
		
		GeoLocationAppFaultInfo geoLocationAppFaultInfo = new GeoLocationAppFaultInfo();
		if(exceptionType.getExceptionType().equalsIgnoreCase(ExceptionType.INTERNAL.getExceptionType())) {
			geoLocationAppFaultInfo.setErrorCode(GeoLocationAppConstants.INTERNAL_ERROR_CODE);
			geoLocationAppFaultInfo.setErrorMessage(GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.INTERNAL_ERROR_CODE)));
		} else if(exceptionType.getExceptionType().equalsIgnoreCase(ExceptionType.TECHNNICAL.getExceptionType())) {
			geoLocationAppFaultInfo.setErrorCode(GeoLocationAppConstants.GENERIC_ERROR_CODE);
			geoLocationAppFaultInfo.setErrorMessage(GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE)));
		}
		
		return geoLocationAppFaultInfo;
	}
	
	/**
	 * @param throwable
	 * @return
	 * 
	 * It resolves error messages bases on Throwable object from error map and returns 
	 * GeoLocationAppFaultInfo model object.
	 */
	public static ResponseEntity<GeoLocationAppResponse> mapErrorMessageToResponse(Throwable throwable) {
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		GeoLocationAppResponse geoLocationAppResponse = new GeoLocationAppResponse();
		if(throwable instanceof GeoLocationAppException) {
			GeoLocationAppException geoLocationAppException = (GeoLocationAppException) throwable;
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(geoLocationAppException.getGeoLocationAppFaultInfo());
		} else {
			GeoLocationAppFaultInfo geoLocationAppFaultInfo = new GeoLocationAppFaultInfo();
			geoLocationAppFaultInfo.setErrorCode(GeoLocationAppConstants.GENERIC_ERROR_CODE);
			geoLocationAppFaultInfo.setErrorMessage(GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE)));
					
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(geoLocationAppFaultInfo);
		}
		
		responseEntity = new ResponseEntity<>(geoLocationAppResponse, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * @param responseObject
	 * @return
	 * 
	 * It creates ResponseEntity<GeoLocationAppResponse> based on Response Object and returns it.
	 */
	public static ResponseEntity<GeoLocationAppResponse> mapSuccessMessageToResponse(Object responseObject) {
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		GeoLocationAppResponse geoLocationAppResponse = new GeoLocationAppResponse();
		if(responseObject instanceof Map) {
			Map<?,?> responseObjectMap = (Map<?,?>) responseObject;
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(responseObjectMap);
		} else if(responseObject instanceof List){
			List<?> responseObjectList = (List<?>) responseObject;
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(responseObjectList);
		} else if (responseObject instanceof Set) {
			Set<?> responseObjectSet = (Set<?>) responseObject;
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(responseObjectSet);
		} else {
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(responseObject);
		}
		
		responseEntity = new ResponseEntity<>(geoLocationAppResponse, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * @param httpHeadersMap
	 * @return
	 * 
	 * It copies parameters from a Map and return HttpHeaders Object.
	 */
	public static HttpHeaders copyHttpRequestHeaders(Map<String, String> httpHeadersMap) {
		
		HttpHeaders headers = null;
		try {
			headers = new HttpHeaders();
			if(validateObject(httpHeadersMap)) {
				for(String headerKey : httpHeadersMap.keySet()) {
					headers.add(headerKey, httpHeadersMap.get(headerKey));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occured In GeoLocationAppUtility:copyHttpRequestHeaders : Stacktrace : " ,e);
		}
		
		return headers;
	}
	
	/**
	 * @param ips
	 * @param devicedata
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * 
	 * It copies IP address from HttpServletRequest Object and returns it.
	 */
	public static String getIpAddress(HttpServletRequest httpRequest)
			throws GeoLocationAppException {
		String clientIp;
		String[] ips = null;
		try {
			clientIp = httpRequest.getHeader(GeoLocationAppConstants.X_REAL_IP_KEY);
		    if(clientIp == null || clientIp.isEmpty()) { 
		    	String ipForwarded = httpRequest.getHeader(GeoLocationAppConstants.X_FORWARDED_FOR_KEY);
		        if(ipForwarded != null) {
		        	ips = ipForwarded.split(",");
		        }
	
		        if(ips != null && ips.length > 0) {
		        	clientIp = ips[0];
		        }
		        
		        if(clientIp == null || clientIp.isEmpty()) {
		        	
		        	clientIp = httpRequest.getRemoteAddr();
		        }
		    }
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		    
		return clientIp;
	}
    
    
	/**
	 * @param geoLocationAppRequest
	 * @return
	 * 
	 * It converts GeoLocationAppRequest Model Object into  String using ObjectMapper implemnetation.
	 */
	public static String getEntityBody(GeoLocationAppRequest geoLocationAppRequest) {
    	
    	String requestBody = "";
        ObjectMapper objectMapper = null;
    	try {
    		if(validateObject(geoLocationAppRequest)) {
    			objectMapper = new ObjectMapper();
    			requestBody = objectMapper.writeValueAsString(geoLocationAppRequest);
    		} 
    	} catch (IOException ioe) {
    		LOGGER.error("In GeoLocationAppUtility:getEntityBody : IOException Occured : Stacktrace : " ,ioe);
    	}
        
        return requestBody;
    }
	
	/**
	 * @param shopDetailsList
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * It calculate distance based on Latitude and Longitude from List of ShopDetails objects and updates the distance 
	 * parameter of each ShopDetails Object in that List and returns that updated List.
	 */
	public static List<GeoLocationAppRequest> calculateDistance(List<ShopDetails> shopDetailsList, String latitude, String longitude) {
    	
		double theta = 0.0;
		double shopLatitude = 0.0;
		double shopLongitude = 0.0;
		double requestLatitude = 0.0;
		double requestLongitude = 0.0;
		double distance = 0.0;
		List<GeoLocationAppRequest> geoLocationAppRequestList = new ArrayList<>();
		GeoLocationAppRequest geoLocationAppRequest = null;
    	try {
    		requestLatitude = Double.parseDouble(latitude);
    		requestLongitude = Double.parseDouble(longitude);
    		for(ShopDetails shopDetails : shopDetailsList) {
    			distance = 0.0;
    			
    			geoLocationAppRequest = new GeoLocationAppRequest();
    			geoLocationAppRequest.setShopId(shopDetails.getShopId());
    			geoLocationAppRequest.setShopName(shopDetails.getShopName());
    			geoLocationAppRequest.setShopAddress(shopDetails.getShopAddress());
    			geoLocationAppRequest.setShopPincode(shopDetails.getShopPincode());
    			geoLocationAppRequest.setShopLatitude(shopDetails.getShopLatitude());
    			geoLocationAppRequest.setShopLongitude(shopDetails.getShopLongitude());
    			
    			if(validateString(shopDetails.getShopLatitude()) && validateString(shopDetails.getShopLongitude())) {
    				shopLatitude = Double.parseDouble(shopDetails.getShopLatitude());
    				shopLongitude = Double.parseDouble(shopDetails.getShopLongitude());
    				
    				theta = shopLongitude - requestLongitude;
    				distance = Math.sin(deg2rad(shopLatitude)) * Math.sin(deg2rad(requestLatitude)) + Math.cos(deg2rad(shopLatitude)) * Math.cos(deg2rad(requestLatitude)) * Math.cos(deg2rad(theta));
    				
    				distance = Math.acos(distance);
    				distance = rad2deg(distance);
    				distance = distance * GeoLocationAppConstants.CONSTANT2 * GeoLocationAppConstants.CONSTANT3;
    				distance = distance * GeoLocationAppConstants.CONSTANT4;
    				
    				geoLocationAppRequest.setDistance(String.valueOf(distance));
    				geoLocationAppRequestList.add(geoLocationAppRequest);
    			}
    		}
    	} catch (Exception e) {
    		LOGGER.error("In GeoLocationAppUtility:calculateDistance : Exception Occured : Stacktrace : " ,e);
    	}
        
        return geoLocationAppRequestList;
    }
	
	
	/**
	 * @param deg
	 * @return
	 * 
	 * It converts degree to radiant and returns.
	 */
	public static double deg2rad(double deg) {
		return deg * Math.PI / GeoLocationAppConstants.CONSTANT1;
	}
	
	/**
	 * @param rad
	 * @return
	 * 
	 * It converts radiant to degree and returns.
	 */
	public static double rad2deg(double rad) {
		return rad * GeoLocationAppConstants.CONSTANT1 / Math.PI;
	}
	
	/**
	 * @param geoLocationAppRequestList
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It sorts list of GeoLocationAppRequest Model object in ascending order and returns the ascending List.
	 */
	public static List<GeoLocationAppRequest> sortShopList(List<GeoLocationAppRequest> geoLocationAppRequestList) 
			throws GeoLocationAppException {
		
		try {
			if(validateObject(geoLocationAppRequestList)) {
				Collections.sort(geoLocationAppRequestList, new Comparator<GeoLocationAppRequest>() {
					@Override
					public int compare(GeoLocationAppRequest geoLocationAppRequest1, GeoLocationAppRequest geoLocationAppRequest2) {
						if(Double.parseDouble(geoLocationAppRequest1.getDistance()) 
								< Double.parseDouble(geoLocationAppRequest2.getDistance())) {
							return -1;
						} else if(Double.parseDouble(geoLocationAppRequest1.getDistance()) 
								> Double.parseDouble(geoLocationAppRequest2.getDistance())) {
							return 1;
						} else {
							 return 0;
						}
					}
				});
			} else {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_112);
			}
			
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
		return geoLocationAppRequestList;
	}

}
