
package com.sam.geolocationapp.interceptors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.models.GeoLocationAppResponse;
import com.sam.geolocationapp.services.GeoLocationAppTransanctionLogServiceImpl;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppTokenUtil;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

/**
 * @author Samok Sinha
 * 
 * This Class is responsible for intercepting all the Request
 * coming into the application. It validates every request
 * and do Transaction Logging and based on configuration it 
 * allow the request to proceed further or aborted the request.
 *
 */

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
	
    
	/**
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Exception
	 * 
	 * This method intercepts all the incoming request and based on URL configuration, it perform security check of the
	 * incoming request. If security check succeed then it allows the request to proceed further or it aborted the request
	 * and provided suitable information back to the client.
	 * 
	 * It also logs each and every request with an uniqueLogId parameter to identify each request identically when it comes 
	 * into it into a separate transanction_log Table for reconciliation purpose.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Around("within(com.sam.geolocationapp.rest.controllers..*)")
    public ResponseEntity<GeoLocationAppResponse> aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
    	
		String clientIp = "";
		String operationName = "";
		String requestBody = null;
		Date requestTime = null;
		String requestTimeString = "";
		String uniqueLogId = null;
		String method = null;
		SimpleDateFormat simpleDateFormat = null;
		String urlPath = null;
		
		String authorizationHeader = null;
		String authToken = "";
		String emailId = "";
		String tokenType = "";
		
		Object[] methodArgumentArray = null;
		HttpServletRequest httpRequest = null;
		GeoLocationAppRequest geoLocationAppRequest = null;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		Map<String, String> httpHeadersMap = null;
		Set<String> intercepterExcludePathSet = null;
		String[] intercepterExcludePathArray = null;
    	try {
    		simpleDateFormat = new SimpleDateFormat(dateFormat);
    		intercepterExcludePathSet = new HashSet<>();
    		intercepterExcludePathArray = intercepterExcludePath.split(GeoLocationAppConstants.PIPE_DELIMITER_VALUE);
    		for(int i = 0; i < intercepterExcludePathArray.length; i++) {
    			intercepterExcludePathSet.add(intercepterExcludePathArray[i]);
    		}
    		
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
	        requestTimeString = simpleDateFormat.format(requestTime);
	        requestBody = GeoLocationAppUtility.getEntityBody(geoLocationAppRequest);
			clientIp = GeoLocationAppUtility.getIpAddress(httpRequest);
			
			if(intercepterExcludePathSet.contains(urlPath)) {
				emailId = httpHeadersMap.get(GeoLocationAppConstants.EMAIL_ID_KEY);
			} else {
				authorizationHeader = httpRequest.getHeader(GeoLocationAppConstants.TOKEN_HEADER_PARAM);
		        if (!GeoLocationAppUtility.validateString(authorizationHeader)) {
		        	throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_106);
		        }
			        
		        authToken = geoLocationAppTokenUtil.extractJwtTokenFromAuthorizationHeader(authorizationHeader);
		        if(geoLocationAppTokenUtil.isValid(authToken)) {
		        	emailId = geoLocationAppTokenUtil.getEamilId(authToken);
		        	tokenType = geoLocationAppTokenUtil.getTokenType(authToken);
		        }
			}
			
			this.updateInvalidTokenToTransanctionLog(emailId, uniqueLogId, authToken, tokenType, method, 
													 operationName, requestTimeString, requestBody, clientIp);
			responseEntity = (ResponseEntity<GeoLocationAppResponse>) proceedingJoinPoint.proceed();
	        
    	} catch(GeoLocationAppException gae){
    		LOGGER.error("In GeoLocationAppSecurityIntercepter:preHandle : GeoLocationAppException Occured : Stacktrace :" ,gae);
    		this.updateInvalidTokenToTransanctionLog(emailId, uniqueLogId, authToken, tokenType, method, 
    												 operationName, requestTimeString, requestBody, clientIp);
    		responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(gae);
    	} catch (Exception e) {
    	    LOGGER.error("In GeoLocationAppSecurityIntercepter:preHandle : Generic Exception Occured : Stacktrace :" ,e);
    	    this.updateInvalidTokenToTransanctionLog(emailId, uniqueLogId, authToken, tokenType, method, 
					 								 operationName, requestTimeString, requestBody, clientIp);
    	    responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(e);
    	} catch (Throwable t) {
    		LOGGER.error("In GeoLocationAppSecurityIntercepter:preHandle : Throwable Occured : Stacktrace :" ,t);
    		responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(t);
    	}
    
    	return responseEntity;
    }
	
	/**
	 * @param emailId
	 * @param uniqueLogId
	 * @param authToken
	 * @param tokenType
	 * @param method
	 * @param operationName
	 * @param requestTimeString
	 * @param requestBody
	 * @param clientIp
	 * 
	 * This method actually call the back end service class to do the Request logging into transanction_log table.
	 * 
	 */
	private void updateInvalidTokenToTransanctionLog(String emailId, String uniqueLogId, 
													 String authToken, String tokenType, String method, 
													 String operationName, String requestTimeString,
													 String requestBody, String clientIp) {
		geoLocationAppTransanctionLogServiceImpl.insertTransanctionLog(emailId, uniqueLogId, 
					   												   authToken, tokenType, 
					   												   method, operationName, 
					   												   requestTimeString, 
					   												   requestBody, clientIp);
	}
    
}
