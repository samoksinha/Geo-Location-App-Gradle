package com.sam.geolocationapp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.sam.geolocationapp.entities.ShopDetails;
import com.sam.geolocationapp.entities.TransactionLog;
import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.rest.client.GeoCodeClient;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

/**
 * @author Samok Sinha
 * 
 * This is the Business interface implementation class which provides the implementation of the Geo Location Application
 * functionality in a central place.
 */

@Service
public class GeoLocationAppTransanctionLogServiceImpl implements GeoLocationAppTransanctionLogService {
	
	private static final Logger LOGGER = LogManager.getLogger(GeoLocationAppTransanctionLogServiceImpl.class);

	@Autowired
	private GeoLocationAppDataAccessService geoLocationAppDataAccessService;
	
	@Autowired
	private GeoCodeClient geoCodeClient;
	
	@Value("${com.sam.geolocation.query.findShopByPin}")
	protected String findShopByPinQuery;
	
	@Value("${com.sam.geolocation.query.findShopByPin.searchKey}")
	protected String findShopByPinSearchKey;
	
	@Value("${com.sam.geolocation.query.findAllShopsNameQuery}")
	protected String findAllShopsNameQuery;
	
	@Value("${com.sam.geolocation.semaphore.concurrentUser}")
	protected String concurrentUser;
	
	private Semaphore singleThreadAccessSemaphore;
	
	/**
	 * @throws GeoLocationAppException
	 * 
	 * It is called when this Bean is initialized by the Spring Container to set the necessary
	 * Semaphore and Configuration.
	 * 
	 */
	@PostConstruct
	public void init() 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:init : Initializing GeoLocationAppTransanctionLogServiceImpl Bean : ");
		
		try {
			singleThreadAccessSemaphore = new Semaphore(Integer.parseInt(concurrentUser));
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:init : Successfully initialized GeoLocationAppTransanctionLogServiceImpl Bean :");
	}
	
	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#insertTransanctionLog(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * It provides the functionality to insert details of Request from client to the underlying database for reconciliation purpose using uniqueLogId.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@Override
	public boolean insertTransanctionLog(String emailId,  String uniqueLogId, String accessToken, String tokenType, String requestType,
			  				  			 String operationName,  String requestTime, String requestBody, String clientIp) 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertTransanctionLog : Starts Executing");
		
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
			
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertTransanctionLog : Before Insert :transactionLog : " +transactionLog);
			geoLocationAppDataAccessService.create(transactionLog);
			insertToDbFlag = true;
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:insertTransanctionLog : Completes Execution : " +insertToDbFlag);
		}
		
		return insertToDbFlag;
	}

	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#updateTransanctionLog(java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * It provides the functionality to update details of Response to client to the underlying database for reconciliation purpose using uniqueLogId.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@Override
	public boolean updateTransanctionLog(String uniqueLogId,  String responseTime, String responseBody) 
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateTransanctionLog : Starts Executing");
		
		boolean updateToDbFlag = false;
		TransactionLog transactionLog = null;
		try {
			transactionLog = geoLocationAppDataAccessService.find(TransactionLog.class, uniqueLogId);
			transactionLog.setResponseTime(responseTime);
			transactionLog.setResponseBody(responseBody);
			
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateTransanctionLog : Before Update :transactionLog : " +transactionLog);
			transactionLog = geoLocationAppDataAccessService.update(transactionLog);
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateTransanctionLog : After Update :transactionLog : " +transactionLog);
			
			updateToDbFlag = true;
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateTransanctionLog : Completes Execution : updateToDbFlag : " +updateToDbFlag);
		}
		
		return updateToDbFlag;
	}

	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#addShop(com.sam.geolocationapp.models.GeoLocationAppRequest)
	 * 
	 * It provides functionality to Add Shop Details to the underlying database and returned Shop Details based on business validation.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@Override
	public Map<String, GeoLocationAppRequest> addShop(GeoLocationAppRequest currentVersionRequest) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:addShop : Starts Executing");
		
		ShopDetails shopDetails = null;
		GeoLocationAppRequest previousVersionRequest = null;
		Map<String, GeoLocationAppRequest> addShopResponseMap = null;
		boolean updateShopFlag = false;
		boolean createShopFlag = false;
		try {
			addShopResponseMap = new LinkedHashMap<>();
			
			singleThreadAccessSemaphore.acquire();
			shopDetails = this.findShop(currentVersionRequest);
			if(GeoLocationAppUtility.validateObject(shopDetails)) {
				previousVersionRequest = new GeoLocationAppRequest();
				previousVersionRequest.setShopId(shopDetails.getShopId());
				previousVersionRequest.setShopName(shopDetails.getShopName());
				previousVersionRequest.setShopAddress(shopDetails.getShopAddress());
				previousVersionRequest.setShopPincode(shopDetails.getShopPincode());
				
				if(GeoLocationAppUtility.validateString(shopDetails.getShopLatitude())) {
					previousVersionRequest.setShopLatitude(shopDetails.getShopLatitude());
				} else {
					previousVersionRequest.setShopLatitude(GeoLocationAppConstants.EMPTY_DELIMITER_VALUE);
				}
				if(GeoLocationAppUtility.validateString(shopDetails.getShopLongitude())) {
					previousVersionRequest.setShopLongitude(shopDetails.getShopLongitude());
				} else {
					previousVersionRequest.setShopLongitude(GeoLocationAppConstants.EMPTY_DELIMITER_VALUE);
				}
				
				currentVersionRequest.setShopId(shopDetails.getShopId());
				addShopResponseMap.put(GeoLocationAppConstants.ADD_SHOP_PREVIOUS_VERSION_KEY, previousVersionRequest);
			} else {
				shopDetails = new ShopDetails();
				shopDetails.setShopName(currentVersionRequest.getShopName());
				shopDetails.setShopAddress(currentVersionRequest.getShopAddress());
				shopDetails.setShopPincode(currentVersionRequest.getShopPincode());
				geoLocationAppDataAccessService.create(shopDetails);
				
				createShopFlag = true;
				currentVersionRequest.setShopId(shopDetails.getShopId());
			}
			
			if(createShopFlag 
					|| !(GeoLocationAppUtility.validateString(shopDetails.getShopLatitude())
							&& GeoLocationAppUtility.validateString(shopDetails.getShopLongitude()))) {
				currentVersionRequest = this.populateShopCoordinates(currentVersionRequest);
			} else {
				currentVersionRequest.setShopLatitude(shopDetails.getShopLatitude());
				currentVersionRequest.setShopLongitude(shopDetails.getShopLongitude());
			}
			
			updateShopFlag = this.updateShop(currentVersionRequest);
			if(!updateShopFlag) {
				throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_115);
			}
			
			addShopResponseMap.put(GeoLocationAppConstants.ADD_SHOP_CURRENT_VERSION_KEY, currentVersionRequest);
			
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			singleThreadAccessSemaphore.release();
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:addShop : Completes Execution : addShopResponseMap : " +addShopResponseMap);
		}
		
		return addShopResponseMap;
	}

	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#updateShop(com.sam.geolocationapp.models.GeoLocationAppRequest)
	 * It provides functionality to update Shop Details to the underlying Database.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
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
			
			geoLocationAppDataAccessService.update(shopDetails);
			updateShopFlag = true;
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:updateShop : Completes Execution : updateShopFlag : " +updateShopFlag);
		}
		
		return updateShopFlag;
	}

	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#findShop(com.sam.geolocationapp.models.GeoLocationAppRequest)
	 * 
	 * It provides functionality to retrieve Shop Details from underlying Database based on Shop Pin Code and does Business Validation and returns the Shop Details.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@Override
	public ShopDetails findShop(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:findShop : Starts Executing");
		
		List<ShopDetails> shopDetailsList = new ArrayList<>();
		ShopDetails shopDetails = null;
		Iterator<ShopDetails> shopDetailsIterator = null;
		Map<String, ShopDetails> shopDetailsMap = new HashMap<>();
		try {
			shopDetailsList = geoLocationAppDataAccessService.findShopByPin(geoLocationAppRequest.getShopPincode(), 
																			findShopByPinQuery, findShopByPinSearchKey);
			shopDetailsIterator = shopDetailsList.iterator();
			while(shopDetailsIterator.hasNext()) {
				ShopDetails shop = shopDetailsIterator.next();
				
				shopDetailsMap.put(shop.getShopAddress(), shop);
				shopDetailsIterator.remove();
			}
			
			if(shopDetailsMap.containsKey(geoLocationAppRequest.getShopAddress())){
				shopDetails = shopDetailsMap.get(geoLocationAppRequest.getShopAddress());
			}
			
			if(GeoLocationAppUtility.validateObject(shopDetails)) {
				if(shopDetails.getShopName().equalsIgnoreCase(geoLocationAppRequest.getShopName())
						&& (GeoLocationAppUtility.validateString(shopDetails.getShopLatitude())
								&& GeoLocationAppUtility.validateString(shopDetails.getShopLongitude()))) {
					throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_121);
				}
			}
			
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:findShop : Completes Execution : shopDetails : " +shopDetails);
		}
		
		return shopDetails;
	}
	
	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#findAllShops(com.sam.geolocationapp.models.GeoLocationAppRequest)
	 * 
	 * It provides functionality to retrieve all Shop Details from underlying Database and returned List of Shop Details.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@Override
	public List<GeoLocationAppRequest> findAllShops(GeoLocationAppRequest geoLocationAppRequest) throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : Starts Executing : with : latitude : " +geoLocationAppRequest.getShopLatitude()+ " : longitude : " +geoLocationAppRequest.getShopLongitude());
		
		List<ShopDetails> shopDetailsList = new ArrayList<>();
		List<GeoLocationAppRequest> geoLocationAppRequestList = new ArrayList<>();
		try {
			shopDetailsList = geoLocationAppDataAccessService.findAllShops(findAllShopsNameQuery);
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("In GeoLocationAppTransanctionLogServiceImpl:findAllShops : shopDetailsList : " +shopDetailsList);
			}
			if(GeoLocationAppUtility.validateObject(shopDetailsList)) {
				geoLocationAppRequestList = GeoLocationAppUtility.calculateDistance(shopDetailsList, 
																					geoLocationAppRequest.getShopLatitude(), 
																					geoLocationAppRequest.getShopLongitude());
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

	/* (non-Javadoc)
	 * @see com.sam.geolocationapp.services.GeoLocationAppTransanctionLogService#populateShopCoordinates(com.sam.geolocationapp.models.GeoLocationAppRequest)
	 * 
	 * It provides the functionality to populate Shop Latitude and Longitude by using Shop Address and Shop Pincode by calling GeoCoding API
	 * and returns the updated Shop Details with Latitude and Longitude.
	 * If any exception occurs it updates the Shop Details with the necessary information.
	 */
	@Override
	public GeoLocationAppRequest populateShopCoordinates(GeoLocationAppRequest geoLocationAppRequest)
			throws GeoLocationAppException {
		LOGGER.info("In GeoLocationAppTransanctionLogServiceImpl:populateShopCoordinates : Starts Executing : with : geoLocationAppRequest : " +geoLocationAppRequest);

		GeocodingResult[] geocodingResultArray = null;
		GeocodingResult geocodingResult = null;
		LatLng latLong = null;
		try {
			geocodingResultArray = geoCodeClient.callGeoCodeApi(geoLocationAppRequest);
			if(!(geocodingResultArray != null && geocodingResultArray.length > 0)) {
				throw new GeoLocationAppException(ExceptionType.INTERNAL, ErrorCodes.ERROR_CODE_119);
			}
			
			geocodingResult = geocodingResultArray[0];
			if(GeoLocationAppUtility.validateObject(geocodingResult.geometry)
					&& GeoLocationAppUtility.validateObject(geocodingResult.geometry.location)) {
				
				latLong = geocodingResult.geometry.location;
				geoLocationAppRequest.setShopLatitude(String.valueOf(latLong.lat));
				geoLocationAppRequest.setShopLongitude(String.valueOf(latLong.lng));
			} else {
				throw new GeoLocationAppException(ExceptionType.INTERNAL, ErrorCodes.ERROR_CODE_119);
			}
			
		} catch (GeoLocationAppException gae) {
			if(gae.getExceptionType().getExceptionType().equalsIgnoreCase(ExceptionType.INTERNAL.getExceptionType())) {
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
 