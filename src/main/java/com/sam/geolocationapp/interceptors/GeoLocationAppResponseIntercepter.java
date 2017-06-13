
package com.sam.geolocationapp.interceptors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.services.GeoLocationAppTransanctionLogServiceImpl;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;


@Aspect
@Component
public class GeoLocationAppResponseIntercepter {
 
	public static final Logger LOGGER = Logger.getLogger(GeoLocationAppResponseIntercepter.class.getName());
    
    @Autowired
	private GeoLocationAppTransanctionLogServiceImpl geoLocationAppTransanctionLogServiceImpl;
    
    @Value("${com.sam.geolocation.dateFormat}")
	protected String dateFormat;
    
	@SuppressWarnings({ "rawtypes" })
	@AfterReturning(pointcut="within(com.sam.geolocationapp.rest.controllers..*)", returning="returnValue")
	public void afterReturningAdvice(JoinPoint joinPoint, Object returnValue) 
			throws Throwable {
		
    	Date responseTime = null;
    	String uniqueLogId = null;
    	String responseBody = null;
    	ObjectMapper objectMapper = null;
    	SimpleDateFormat simpleDateFormat = null;;
    	
    	ResponseEntity responseEntity = null;
    	try {
    		simpleDateFormat = new SimpleDateFormat(dateFormat);
    		objectMapper = new ObjectMapper();
    		
    		if(returnValue instanceof ResponseEntity) {
    			responseEntity = (ResponseEntity) returnValue;
    		}
    		if(responseEntity != null) {
    			responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
    		}
    		
    		responseTime = Calendar.getInstance().getTime();
    		uniqueLogId = (String) MDC.get(GeoLocationAppConstants.MDC_UNIQUE_LOG_ID);
    		
    		geoLocationAppTransanctionLogServiceImpl.updateToDb(uniqueLogId, simpleDateFormat.format(responseTime), responseBody);
    		MDC.clear();
    		
    	} catch(GeoLocationAppException gae){
    		LOGGER.error("In GeoLocationAppResponseIntercepter:afterReturningAdvice : GeoLocationAppException Occured : Stacktrace :" ,gae);
    	} catch (Exception e) {
    		LOGGER.error("Exception Occured In GeoLocationAppResponseIntercepter:afterReturningAdvice : Stacktrace : " ,e);
    	}
	}
    
}
