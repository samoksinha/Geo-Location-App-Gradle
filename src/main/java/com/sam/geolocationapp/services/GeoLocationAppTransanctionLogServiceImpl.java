package com.sam.geolocationapp.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.geolocationapp.entities.ShopDetails;
import com.sam.geolocationapp.entities.TransactionLog;
import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.models.GeoCodingResponse;
import com.sam.geolocationapp.models.GeoCodingResultResponse;
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.rest.client.GeoLocationRestClient;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

@Service
public class GeoLocationAppTransanctionLogServiceImpl implements GeoLocationAppTransanctionLogService {
	
	private static final Logger LOGGER = LogManager.getLogger(GeoLocationAppTransanctionLogServiceImpl.class);

	@Autowired
	private GeoLocationAppDataAccessService geoLocationAppDataAccessService;
	
	@Autowired
	private GeoLocationRestClient geoLocationRestClient;
	
	@Value("${com.sam.geolocation.query.findShopByName}")
	protected String findShopByNameQuery;
	
	@Value("${com.sam.geolocation.query.findShopByName.searchKey}")
	protected String findShopByNameSearchKey;
	
	@Value("${com.sam.geolocation.query.findAllShopsNameQuery}")
	protected String findAllShopsNameQuery;
	
	@Override
	public boolean insertToDb(String emailId,  String uniqueLogId, String accessToken, String tokenType, String requestType,
			  				  String operationName,  String requestTime, String requestBody, String clientIp) 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertToDb : Starts Executing");
		
		boolean insertToDbFlag = false;
		TransactionLog transactionLog = null;
		try {
			transactionLog = new TransactionLog();
			transactionLog.setEmailId(emailId);
			transactionLog.setUniqueLogId(uniqueLogId);
			transactionLog.setApiKey(accessToken);
			transactionLog.setTokenType(tokenType);
			transactionLog.setRequestType(requestType);
			transactionLog.setOperationName(operationName);
			transactionLog.setRequestTime(requestTime);
			transactionLog.setRequestBody(requestBody);
			transactionLog.setClientIp(clientIp);
			
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertToDb : Before Insert :transactionLog : " +transactionLog);
			geoLocationAppDataAccessService.create(transactionLog);
			insertToDbFlag = true;
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertToDb : Completes Execution : " +insertToDbFlag);
		}
		
		return insertToDbFlag;
	}

	@Override
	public boolean updateToDb(String uniqueLogId,  String responseTime, String responseBody) 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateToDb : Starts Executing");
		
		boolean updateToDbFlag = false;
		TransactionLog transactionLog = null;
		try {
			transactionLog = geoLocationAppDataAccessService.find(TransactionLog.class, uniqueLogId);
			transactionLog.setResponseTime(responseTime);
			transactionLog.setResponseBody(responseBody);
			
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertToDb : Before Update :transactionLog : " +transactionLog);
			transactionLog = geoLocationAppDataAccessService.updateTransanctionLog(transactionLog);
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertToDb : After Update :transactionLog : " +transactionLog);
			
			updateToDbFlag = true;
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateToDb : Completes Execution : updateToDbFlag : " +updateToDbFlag);
		}
		
		return updateToDbFlag;
	}

	@Override
	public GeoLocationAppRequest addShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:addShop : Starts Executing");
		
		ShopDetails shopDetails = null;
		GeoLocationAppRequest returnedGeoLocationAppRequest = new GeoLocationAppRequest();
		try {
			shopDetails = this.findShop(geoLocationAppRequest.getShopName());
			if(GeoLocationAppUtility.validateObject(shopDetails)) {
				returnedGeoLocationAppRequest.setShopId(shopDetails.getShopId());
				returnedGeoLocationAppRequest.setShopName(shopDetails.getShopName());
				returnedGeoLocationAppRequest.setShopAddress(shopDetails.getShopAddress());
				returnedGeoLocationAppRequest.setShopPincode(shopDetails.getShopPincode());
				returnedGeoLocationAppRequest.setShopLatitude(shopDetails.getShopLatitude());
				returnedGeoLocationAppRequest.setShopLongitude(shopDetails.getShopLongitude());
				
				geoLocationAppRequest.setShopId(shopDetails.getShopId());
				this.updateShop(geoLocationAppRequest);
			} else {
				shopDetails = new ShopDetails();
				shopDetails.setShopName(geoLocationAppRequest.getShopName());
				shopDetails.setShopAddress(geoLocationAppRequest.getShopAddress());
				shopDetails.setShopPincode(geoLocationAppRequest.getShopPincode());
				geoLocationAppDataAccessService.create(shopDetails);
				
				geoLocationAppRequest.setShopId(shopDetails.getShopId());
				returnedGeoLocationAppRequest = geoLocationAppRequest;
			}
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:addShop : Completes Execution : geoLocationAppRequest : " +geoLocationAppRequest);
		}
		
		return returnedGeoLocationAppRequest;
	}

	@Override
	public boolean updateShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateShop : Starts Executing");
		
		ShopDetails shopDetails = new ShopDetails();
		boolean updateShopFlag = false;
		try {
			shopDetails.setShopId(geoLocationAppRequest.getShopId());
			shopDetails.setShopName(geoLocationAppRequest.getShopName());
			shopDetails.setShopAddress(geoLocationAppRequest.getShopAddress());
			shopDetails.setShopPincode(geoLocationAppRequest.getShopPincode());
			shopDetails.setShopLatitude(geoLocationAppRequest.getShopLatitude());
			shopDetails.setShopLongitude(geoLocationAppRequest.getShopLongitude());
			
			geoLocationAppDataAccessService.updateTransanctionLog(shopDetails);
			updateShopFlag = true;
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateShop : Completes Execution : updateShopFlag : " +updateShopFlag);
		}
		
		return updateShopFlag;
	}

	@Override
	public ShopDetails findShop(String shopName) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:findShop : Starts Executing");
		
		List<ShopDetails> shopDetailsList = new ArrayList<>();
		ShopDetails shopDetails = null;
		try {
			shopDetailsList = geoLocationAppDataAccessService.findShopByName(shopName, findShopByNameQuery, findShopByNameSearchKey);
			if(GeoLocationAppUtility.validateObject(shopDetailsList)) {
				shopDetails = shopDetailsList.get(0);
			}
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateShop : Completes Execution : shopDetails : " +shopDetails);
		}
		
		return shopDetails;
	}
	
	@Override
	public List<GeoLocationAppRequest> findAllShops(String latitude, String longitude) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : Starts Executing : with : latitude : " +latitude+ " : longitude : " +longitude);
		
		List<ShopDetails> shopDetailsList = new ArrayList<>();
		List<GeoLocationAppRequest> geoLocationAppRequestList = new ArrayList<>();
		try {
			shopDetailsList = geoLocationAppDataAccessService.findAllShops(findAllShopsNameQuery);
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : shopDetailsList : " +shopDetailsList);
			}
			if(GeoLocationAppUtility.validateObject(shopDetailsList)) {
				geoLocationAppRequestList = GeoLocationAppUtility.calculateDistance(shopDetailsList, latitude, longitude);
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : Before Sorting with Distance : geoLocationAppRequestList : " +geoLocationAppRequestList);
				}
				
				geoLocationAppRequestList = GeoLocationAppUtility.sortShopList(geoLocationAppRequestList);
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : After Sorting with Distance : geoLocationAppRequestList : " +geoLocationAppRequestList);
				}
				
			} else {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_112);
			}
			
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : Completes Execution : geoLocationAppRequestList : " +geoLocationAppRequestList);
		}
		
		return geoLocationAppRequestList;
	}

	@Override
	public GeoLocationAppRequest populateShopCoordinates(GeoLocationAppRequest geoLocationAppRequest)
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:populateShopCoordinates : Starts Executing : with : geoLocationAppRequest : " +geoLocationAppRequest);
		
		String geloLocationApiResponseString = "";
		ObjectMapper objectMapper = null;
		GeoCodingResponse geoCodingResponse = null;
		GeoCodingResultResponse geoCodingResultResponse = null;
		try {
			objectMapper = new ObjectMapper();
			
			geloLocationApiResponseString = geoLocationRestClient.callGeoLocationRestApi(String.valueOf(geoLocationAppRequest.getShopPincode()));
			if(!GeoLocationAppUtility.validateString(geloLocationApiResponseString)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_117);
			}
			
			geoCodingResponse = objectMapper.readValue(geloLocationApiResponseString, GeoCodingResponse.class);
			if(!GeoLocationAppUtility.validateObject(geoCodingResponse)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_117);
			}
			
			if(!(GeoLocationAppUtility.validateString(geoCodingResponse.getStatus()) 
					&& (geoCodingResponse.getStatus().equalsIgnoreCase(GeoLocationAppConstants.GEO_LOCATION_API_SUCCESS_VALUE)))) {
				if(geoCodingResponse.getStatus().equalsIgnoreCase(GeoLocationAppConstants.GEO_LOCATION_API_EMPTY_RESULT_VALUE)) {
					throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_119);
				} else {
					throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_117);
				}
			}
			
			geoCodingResultResponse = geoCodingResponse.getGeoCodingResultResponseList().get(0);
			if(!GeoLocationAppUtility.validateObject(geoCodingResponse)) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_117);
			}
			
			if(GeoLocationAppUtility.validateObject(geoCodingResultResponse.getGeometry()) 
					&& GeoLocationAppUtility.validateObject(geoCodingResultResponse.getGeometry().getLocation())) {
				geoLocationAppRequest.setShopLatitude(geoCodingResultResponse.getGeometry().getLocation().getLat());
				geoLocationAppRequest.setShopLongitude(geoCodingResultResponse.getGeometry().getLocation().getLng());
			} else {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_117);
			}
			
		} catch (GeoLocationAppException gae) {
			if(gae.getExceptionType().getExceptionType().equalsIgnoreCase(ExceptionType.BUSINESS.getExceptionType())) {
				geoLocationAppRequest.setErrorCode(String.valueOf(gae.getGeoLocationAppFaultInfo().getErrorCode()));
				geoLocationAppRequest.setErrorMessage(gae.getGeoLocationAppFaultInfo().getErrorMessage());
			}
		} catch (Exception e) {
			geoLocationAppRequest.setErrorCode(String.valueOf(ErrorCodes.ERROR_CODE_118));
			geoLocationAppRequest.setErrorMessage(GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP.get(String.valueOf(ErrorCodes.ERROR_CODE_118)));
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:populateShopCoordinates : Completes Execution : geoLocationAppRequest : " +geoLocationAppRequest);
		}
		
		return geoLocationAppRequest;
	}

}
 