package com.sam.geolocationapp.rest.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.exceptions.GeoLocationAppFaultInfo;
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.models.GeoLocationAppResponse;
import com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppTokenUtil;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

@RestController
@RequestMapping(value="/map")
public class GeoLocationAppController {

	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppController.class);
	
	@Autowired
	private GeoLocationAppTokenUtil geoLocationAppTokenUtil;
	
	@Autowired
	private GeoLocationAppTransanctionLogService geoLocationAppTransanctionLogServiceImpl;
	
	private Semaphore singleThreadAccessSemaphore = new Semaphore(GeoLocationAppConstants.NO_OF_THREAD_ACCESS_CONCURRENTLY);
	
	@RequestMapping(value="/getapikey/v1", 
					consumes=MediaType.APPLICATION_JSON_VALUE,
					produces=MediaType.APPLICATION_JSON_VALUE,
					method=RequestMethod.GET)
	public ResponseEntity<GeoLocationAppResponse> getApiKey(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
															@RequestHeader Map<String, String> httpHeadersMap) {
		LOGGER.info("In GeoLocationAppController:loginWithHeader : Starts Executing");
		
		long startTime = 0L;
		long endTime = 0L;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		GeoLocationAppResponse geoLocationAppResponse = new GeoLocationAppResponse();
		Map<String, Object> responseMap = null;
		
		String emailId = null;
		String accessToken = null;
		try {
			startTime = System.currentTimeMillis();
			responseMap = new LinkedHashMap<>();
			LOGGER.info("In GeoLocationAppController:loginWithHeader : httpHeadersMap" +httpHeadersMap);
			
			if(httpHeadersMap.containsKey(GeoLocationAppConstants.EMAIL_ID_KEY)) {
				emailId = httpHeadersMap.get(GeoLocationAppConstants.EMAIL_ID_KEY);
			} else {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_103);
			}
			
			if(GeoLocationAppUtility.validateString(emailId)) {
				accessToken = geoLocationAppTokenUtil.createAccessToken(emailId);
				responseMap.put(GeoLocationAppConstants.EMAIL_ID_KEY, emailId);
				responseMap.put(GeoLocationAppConstants.ACCESS_TOKEN_KEY, accessToken);
			} else {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_104);
			}
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(responseMap);
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} catch (GeoLocationAppException gae) {
			LOGGER.error("In GeoLocationAppController:loginWithHeader : GeoLocationAppException Occured : Stacktrace : " ,gae);
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(gae.getGeoLocationAppFaultInfo());
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} catch (Exception e) {
			LOGGER.error("In GeoLocationAppController:loginWithHeader : Generic Exception Occured : Stacktrace : " ,e);
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(new GeoLocationAppFaultInfo(GeoLocationAppConstants.GENERIC_ERROR_CODE, 
																				 GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE))));
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} finally {
			endTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:loginWithHeader : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
	
		return responseEntity;
	}
	
	@RequestMapping(value="/addshop/v1", 
					consumes=MediaType.APPLICATION_JSON_VALUE,
					produces=MediaType.APPLICATION_JSON_VALUE,
					method=RequestMethod.POST)
	public ResponseEntity<GeoLocationAppResponse> addShop(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
														  @RequestHeader Map<String, String> httpHeadersMap,
														  @RequestBody GeoLocationAppRequest geoLocationAppRequest) {
		LOGGER.info("In GeoLocationAppController:createAddress : Starts Executing");
		
		long startTime = 0L;
		long endTime = 0L;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		GeoLocationAppResponse geoLocationAppResponse = new GeoLocationAppResponse();
		GeoLocationAppRequest addedGeoLocationAppRequest = null;
		boolean updateShopFlag = false;
		try {
			startTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:createAddress : httpHeadersMap" +httpHeadersMap);
			
			if(!GeoLocationAppUtility.validateObject(geoLocationAppRequest)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_109);
			}
			
			if(!GeoLocationAppUtility.validateString(geoLocationAppRequest.getShopName())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_110);
			}
			
			if(geoLocationAppRequest.getShopPincode() == GeoLocationAppConstants.EMPLTY_PIN_CODE_VALUE) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_116);
			}
			
			singleThreadAccessSemaphore.acquire();
			addedGeoLocationAppRequest = geoLocationAppTransanctionLogServiceImpl.addShop(geoLocationAppRequest);
			
			geoLocationAppRequest = geoLocationAppTransanctionLogServiceImpl.populateShopCoordinates(geoLocationAppRequest);
			updateShopFlag = geoLocationAppTransanctionLogServiceImpl.updateShop(geoLocationAppRequest);
			if(!updateShopFlag) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_115);
			}
			
			addedGeoLocationAppRequest.setErrorCode(geoLocationAppRequest.getErrorCode());
			addedGeoLocationAppRequest.setErrorMessage(geoLocationAppRequest.getErrorMessage());
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(addedGeoLocationAppRequest);
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} catch (GeoLocationAppException gae) {
			LOGGER.error("In GeoLocationAppController:createAddress : GeoLocationAppException Occured : Stacktrace : " ,gae);
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(gae.getGeoLocationAppFaultInfo());
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} catch (Exception e) {
			LOGGER.error("In GeoLocationAppController:createAddress : Generic Exception Occured : Stacktrace : " ,e);
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(new GeoLocationAppFaultInfo(GeoLocationAppConstants.GENERIC_ERROR_CODE, 
																				 GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE))));
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} finally {
			singleThreadAccessSemaphore.release();
			endTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:createAddress : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
		
		return responseEntity;
	}
	
	@RequestMapping(value="/findnearestshops/v1", 
					consumes=MediaType.APPLICATION_JSON_VALUE,
					produces=MediaType.APPLICATION_JSON_VALUE,
					method=RequestMethod.POST)
	public ResponseEntity<GeoLocationAppResponse> findNearestShops(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
													  			   @RequestHeader Map<String, String> httpHeadersMap,
													  			   @RequestBody GeoLocationAppRequest geoLocationAppRequest) {
		LOGGER.info("In GeoLocationAppController:findNearestShops : Starts Executing");
		
		long startTime = 0L;
		long endTime = 0L;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		GeoLocationAppResponse geoLocationAppResponse = new GeoLocationAppResponse();
		List<GeoLocationAppRequest> geoLocationAppRequestList = null;
		try {
			startTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:findNearestShops : httpHeadersMap" +httpHeadersMap);
			
			if(!GeoLocationAppUtility.validateObject(geoLocationAppRequest)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_109);
			}
			
			if(!GeoLocationAppUtility.validateString(geoLocationAppRequest.getShopLatitude())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_113);
			}
			
			if(!GeoLocationAppUtility.validateString(geoLocationAppRequest.getShopLongitude())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_114);
			}
			
			geoLocationAppRequestList = geoLocationAppTransanctionLogServiceImpl.findAllShops(geoLocationAppRequest.getShopLatitude(), geoLocationAppRequest.getShopLongitude());
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_SUCCESS_VALUE);
			geoLocationAppResponse.setResponseObject(geoLocationAppRequestList);
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} catch (GeoLocationAppException gae) {
			LOGGER.error("In GeoLocationAppController:findNearestShops : GeoLocationAppException Occured : Stacktrace : " ,gae);
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(gae.getGeoLocationAppFaultInfo());
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} catch (Exception e) {
			LOGGER.error("In GeoLocationAppController:findNearestShops : Generic Exception Occured : Stacktrace : " ,e);
			
			geoLocationAppResponse.setStatus(GeoLocationAppConstants.STATUS_FALIURE_VALUE);
			geoLocationAppResponse.setResponseObject(new GeoLocationAppFaultInfo(GeoLocationAppConstants.GENERIC_ERROR_CODE, 
																				 GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(GeoLocationAppConstants.GENERIC_ERROR_CODE))));
			responseEntity = new ResponseEntity<GeoLocationAppResponse>(geoLocationAppResponse, HttpStatus.OK);
			
		} finally {
			endTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:findNearestShops : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
		
		return responseEntity;
	}
	
}
