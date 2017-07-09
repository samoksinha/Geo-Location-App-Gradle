package com.sam.geolocationapp.rest.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.models.GeoLocationAppResponse;
import com.sam.geolocationapp.models.ShopLatLongRequest;
import com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppTokenUtil;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Samok Sinha
 *
 * This class exposes various endpoint URL for getting API key, Add Shops and Find Nearest Shop details related functionalities.
 */

@RestController
@RequestMapping(value="/map")
@Api(value="GeoLocationContoller", description="Operations pertaining Get API Key, Add Shops, Find Nearest Shops")
public class GeoLocationAppController {

	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppController.class);
	
	@Autowired
	private GeoLocationAppTokenUtil geoLocationAppTokenUtil;
	
	@Autowired
	private GeoLocationAppTransanctionLogService geoLocationAppTransanctionLogServiceImpl;
	
	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param httpHeadersMap
	 * @return
	 * 
	 * It provides the API key for accessing the other application API's exposed by this application 
	 * and returned it back to the end client. If any exception occurs it provides the necessary information
	 * with wrapper implementation to the end client.
	 */
	@ApiOperation(value="Get API key to acess Geo Location API's", response=GeoLocationAppResponse.class)
    @ApiResponses(value={@ApiResponse(code=200, message = "Operation Successfull.")})
	@RequestMapping(value="/getapikey/v1",
					produces=MediaType.APPLICATION_JSON_VALUE,
					method=RequestMethod.GET)
	public ResponseEntity<GeoLocationAppResponse> getApiKey(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
															@ApiParam(required=true, name="emailId", 
																	  value="Email Id to generate API Key."
																	 )
															@RequestHeader Map<String, String> httpHeadersMap) {
		LOGGER.info("In GeoLocationAppController:loginWithHeader : Starts Executing");
		
		long startTime = 0L;
		long endTime = 0L;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
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
			responseEntity = GeoLocationAppUtility.mapSuccessMessageToResponse(responseMap);
			
		} catch (GeoLocationAppException gae) {
			LOGGER.error("In GeoLocationAppController:loginWithHeader : GeoLocationAppException Occured : Stacktrace : " ,gae);
			responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(gae);
		} catch (Exception e) {
			LOGGER.error("In GeoLocationAppController:loginWithHeader : Generic Exception Occured : Stacktrace : " ,e);
			responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(e);
		} finally {
			endTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:loginWithHeader : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
	
		return responseEntity;
	}
	
	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param httpHeadersMap
	 * @param geoLocationAppRequest
	 * @return
	 * 
	 * It provides functionality to Add Shop Detail provided by the end client, process the information and persists information along 
	 * with other information like Latitude and Longitude and returns back the necessary information back to the end client. 
	 * If any exception occurs it provides the necessary information with wrapper implementation to the end client.
	 */
	@ApiOperation(value="Add Shop Details", response=GeoLocationAppResponse.class)
    @ApiResponses(value={@ApiResponse(code=200, message = "Operation Successfull.")})
	@RequestMapping(value="/addshop/v1", 
					consumes=MediaType.APPLICATION_JSON_VALUE,
					produces=MediaType.APPLICATION_JSON_VALUE,
					method=RequestMethod.POST)
	public ResponseEntity<GeoLocationAppResponse> addShop(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
														  @ApiParam(required=true, name="authorization", 
															  		value="API Key. \"Bearer {API-KEY}\""
																   )
														  @RequestHeader Map<String, String> httpHeadersMap,
														  @RequestBody GeoLocationAppRequest geoLocationAppRequest) {
		LOGGER.info("In GeoLocationAppController:createAddress : Starts Executing");
		
		long startTime = 0L;
		long endTime = 0L;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		Map<String, GeoLocationAppRequest> addShopResponseMap = null;
		try {
			startTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:createAddress : httpHeadersMap" +httpHeadersMap);
			
			if(!GeoLocationAppUtility.validateObject(geoLocationAppRequest)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_109);
			}
			
			if(!GeoLocationAppUtility.validateString(geoLocationAppRequest.getShopName())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_110);
			}
			
			if(!GeoLocationAppUtility.validateString(geoLocationAppRequest.getShopAddress())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_122);
			}
			
			if(geoLocationAppRequest.getShopPincode() == GeoLocationAppConstants.EMPLTY_PIN_CODE_VALUE) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_116);
			}
			addShopResponseMap = geoLocationAppTransanctionLogServiceImpl.addShop(geoLocationAppRequest);
			responseEntity = GeoLocationAppUtility.mapSuccessMessageToResponse(addShopResponseMap);
			
		} catch (GeoLocationAppException gae) {
			LOGGER.error("In GeoLocationAppController:createAddress : GeoLocationAppException Occured : Stacktrace : " ,gae);
			responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(gae);
		} catch (Exception e) {
			LOGGER.error("In GeoLocationAppController:createAddress : Generic Exception Occured : Stacktrace : " ,e);
			responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(e);
		} finally {
			endTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:createAddress : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
		
		return responseEntity;
	}
	
	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param httpHeadersMap
	 * @param shopLatLongRequest
	 * @return
	 * 
	 * It provided the functionality to search the nearest shop details with the Latitude and Longitude provided by the 
	 * end client as Request and returns the Nearest Shop Details back to the end client.
	 * If any exception occurs it provides the necessary information with wrapper implementation to the end client.
	 */
	@ApiOperation(value="Find Nearest Shops", response=GeoLocationAppResponse.class)
    @ApiResponses(value={@ApiResponse(code=200, message = "Operation Successfull.")})
	@RequestMapping(value="/findnearestshops/v1", 
					consumes=MediaType.APPLICATION_JSON_VALUE,
					produces=MediaType.APPLICATION_JSON_VALUE,
					method=RequestMethod.POST)
	public ResponseEntity<GeoLocationAppResponse> findNearestShops(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
																   @ApiParam(required=true, name="authorization", 
																		   	 value="API Key. \"Bearer {API-KEY}\""
																		    )
																   @RequestHeader Map<String, String> httpHeadersMap,
													  			   @RequestBody ShopLatLongRequest shopLatLongRequest) {
		LOGGER.info("In GeoLocationAppController:findNearestShops : Starts Executing");
		
		long startTime = 0L;
		long endTime = 0L;
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		List<GeoLocationAppRequest> geoLocationAppRequestList = null;
		try {
			startTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:findNearestShops : httpHeadersMap" +httpHeadersMap);
			
			if(!GeoLocationAppUtility.validateObject(shopLatLongRequest)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_109);
			}
			
			if(!GeoLocationAppUtility.validateString(shopLatLongRequest.getShopLatitude())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_113);
			}
			
			if(!GeoLocationAppUtility.validateString(shopLatLongRequest.getShopLongitude())) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_114);
			}
			geoLocationAppRequestList = geoLocationAppTransanctionLogServiceImpl.findAllShops(new GeoLocationAppRequest(shopLatLongRequest.getShopLatitude(),
																														shopLatLongRequest.getShopLongitude()));
			responseEntity = GeoLocationAppUtility.mapSuccessMessageToResponse(geoLocationAppRequestList);
			
		} catch (GeoLocationAppException gae) {
			LOGGER.error("In GeoLocationAppController:findNearestShops : GeoLocationAppException Occured : Stacktrace : " ,gae);
			responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(gae);
		} catch (Exception e) {
			LOGGER.error("In GeoLocationAppController:findNearestShops : Generic Exception Occured : Stacktrace : " ,e);
			responseEntity = GeoLocationAppUtility.mapErrorMessageToResponse(e);
		} finally {
			endTime = System.currentTimeMillis();
			LOGGER.info("In GeoLocationAppController:findNearestShops : Completes Execution : Toatl Time Taken : " +(endTime - startTime));
		}
		
		return responseEntity;
	}
	
}
