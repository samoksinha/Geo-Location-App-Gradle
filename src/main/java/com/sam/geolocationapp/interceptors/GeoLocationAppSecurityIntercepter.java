
package com.sam.geolocationapp.interceptors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.exceptions.GeoLocationAppFaultInfo;
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.models.GeoLocationAppResponse;
import com.sam.geolocationapp.services.GeoLocationAppTransanctionLogServiceImpl;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppTokenUtil;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

@Aspect
@Component
public class GeoLocationAppSecurityIntercepter {
 
	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppSecurityIntercepter.class);
	
	@Autowired
	private GeoLocationAppTokenUtil geoLocationAppTokenUtil;
	
	@Autowired
	private GeoLocationAppTransanctionLogServiceImpl geoLocationAppTransanctionLogServiceImpl;
	
	@Value("${com.sam.geolocation.dateFormat}")
	protected String dateFormat;
	
	@Value("${com.sam.geolocation.intercepter.exclude.path}")
	protected String intercepterExcludePath;
	
    
	@SuppressWarnings("unchecked")
	@Around("within(com.sam.geolocationapp.rest.controllers..*)")
    public ResponseEntity<GeoLocationAppResponse> aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
    	
		String clientIp = null;
		String operationName = null;
		String requestBody = null;
		Date requestTime = null;
		String uniqueLogId = null;
		String method = null;
		SimpleDateFormat simpleDateFormat = null;;
		String urlPath = null;
		
		String authorizationHeader = null;
		String authToken = null;
		String emailId = null;
		String tokenType = null;
		
		Object[] methodArgumentArray = null;
		HttpServletRequest httpRequest = null;
		GeoLocationAppRequest geoLocationAppRequest = null;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		GeoLocationAppResponse geoLocationAppResponse = new GeoLocationAppResponse();
		Map<String, String> httpHeadersMap = null;
    	try {
    		simpleDateFormat = new SimpleDateFormat(dateFormat);
    		
    		uniqueLogId = UUID.randomUUID().toString();
    		MDC.put(GeoLocationAppConstants.MDC_UNIQUE_LOG_ID, uniqueLogId);
    		
    		methodArgumentArray = proceedingJoinPoint.getArgs();
    		for(int i = 0; i < methodArgumentArray.length; i++) {
    			if(methodArgumentArray[i] instanceof HttpServletRequest) {
    				httpRequest = (HttpServletRequest) methodArgumentArray[i];
    			} else if (methodArgumentArray[i] instanceof GeoLocationAppRequest) {
    				geoLocationAppRequest = (GeoLocationAppRequest) methodArgumentArray[i];
    			}  else if (methodArgumentArray[i] instanceof Map) {
    				httpHeadersMap = (Map<String, String>) methodArgumentArray[i];
    			}
    		}
    		
	    	method = httpRequest.getMethod().toLowerCase();
	    	operationName = proceedingJoinPoint.getSignature().getName();
	    	urlPath = httpRequest.getRequestURI();
	    	if(!GeoLocationAppUtility.validateString(urlPath)) {
	    		throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_111);
	    	}
	    	
	        requestTime = Calendar.getInstance().getTime();
	        requestBody = GeoLocationAppUtility.getEntityBody(geoLocationAppRequest);
			clientIp = GeoLocationAppUtility.getIpAddress(httpRequest);
	        
			if(urlPath.equals(intercepterExcludePath)) {
				emailId = httpHeadersMap.get(GeoLocationAppConstants.EMAIL_ID_KEY);
				geoLocationAppTransanctionLogServiceImpl.insertToDb(emailId, uniqueLogId, GeoLocationAppConstants.EMPTY_DELIMITER_VALUE, GeoLocationAppConstants.EMPTY_DELIMITER_VALUE, 
																	method, operationName, simpleDateFormat.format(requestTime), GeoLocationAppConstants.EMPTY_DELIMITER_VALUE, clientIp);
			} else {
				authorizationHeader = httpRequest.getHeader(GeoLocationAppConstants.TOKEN_HEADER_PARAM);
		        if (!GeoLocationAppUtility.validateString(authorizationHeader)) {
		        	throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_106);
		        }
			        
		        authToken = geoLocationAppTokenUtil.extractJwtTokenFromAuthorizationHeader(authorizationHeader);
		        if(geoLocationAppTokenUtil.isValid(authToken)) {
		        	emailId = geoLocationAppTokenUtil.getEamilId(authToken);
		        	tokenType = geoLocationAppTokenUtil.getTokenType(authToken);
		        
		        	geoLocationAppTransanctionLogServiceImpl.insertToDb(emailId, uniqueLogId, authToken, tokenType, method, 
		        														operationName, simpleDateFormat.format(requestTime), requestBody, clientIp);
		        }
			}
			
			responseEntity = (ResponseEntity<GeoLocationAppResponse>) proceedingJoinPoint.proceed();
	        
    	} catch(GeoLocationAppException gae){
    		LOGGER.error("In GeoLocationAppSecurityIntercepter:preHandle : GeoLocationAppException Occured : Stacktrace :" ,gae);
    		
    		geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(gae.getGeoLocationAppFaultInfo());
    		responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
    	} catch (Exception e) {
    	    LOGGER.error("In GeoLocationAppSecurityIntercepter:preHandle : Generic Exception Occured : Stacktrace :" ,e);
    	    
    	    geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(new GeoLocationAppFaultInfo(GeoLocationAppConstants.GENERIC_ERROR_CODE, 
																				 GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE))));
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
    	} catch (Throwable t) {
    		LOGGER.error("In GeoLocationAppSecurityIntercepter:preHandle : Throwable Occured : Stacktrace :" ,t);
    		
    		geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(new GeoLocationAppFaultInfo(GeoLocationAppConstants.GENERIC_ERROR_CODE, 
																				 GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE))));
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
    	}
    
    	return responseEntity;
    }
    
}
