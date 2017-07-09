package com.sam.geolocationapp.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.sam.geolocationapp.entities.ShopDetails;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;

/**
 * @author Samok Sinha
 *
 * This class is the single entry point for communicating with the underlying in memory H2 Database.
 * It provides functionality to persist/retrieve/update Entity objects to the underlying Database.
 */

@Service
@Transactional(TxType.REQUIRED)
public class GeoLocationAppDataAccessService {

	private static final Logger logger = LogManager.getLogger(GeoLocationAppDataAccessService.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public GeoLocationAppDataAccessService() {
	}
	
	/**
	 * @param entity
	 * @throws GeoLocationAppException
	 * 
	 * It provides functionality to persist an Entity Object to the Database.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	public void create(Object entity) 
			throws GeoLocationAppException {
		logger.info("In GeoLocationAppDataAccessService:create : entity : " +entity);
		
		long startTime = 0L;
		long endTime = 0L;
		try {
			startTime = System.currentTimeMillis();
			logger.info("In GeoLocationAppDataAccessService:create : Before calling Database Operation : startTime : " +startTime);
			
			this.entityManager.persist(entity);
			this.entityManager.flush();
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			logger.info("In GeoLocationAppDataAccessService:create : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
			logger.info("In GeoLocationAppDataAccessService:create : Created Enitty into database : " +entity);
		}
	}

	/**
	 * @param type
	 * @param id
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It provides functionality to retrieve an Entity Object from the Database based on Entity Object Id.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	public <T> T find(Class<T> type, String id) 
			throws GeoLocationAppException {
		logger.info("In GeoLocationAppDataAccessService:find : id : " +id);
		
		long startTime = 0L;
		long endTime = 0L;
		
		T t = null;
		try {
			startTime = System.currentTimeMillis();
			logger.info("In GeoLocationAppDataAccessService:find : Before calling Database Operation : startTime : " +startTime);
			
			t = this.entityManager.find(type, id);
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			logger.info("In GeoLocationAppDataAccessService:find : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
			logger.info("In GeoLocationAppDataAccessService:find : Finded Enitty from database : " +t);
		}
		
		return t;
	}

	/**
	 * @param entity
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It provides functionality to update an Entity Object to the Database.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	public <T> T update(T entity) 
			throws GeoLocationAppException {
		logger.info("In GeoLocationAppDataAccessService:update : entity : " +entity);
		
		long startTime = 0L;
		long endTime = 0L;
		
		T t = null;
		try {
			startTime = System.currentTimeMillis();
			logger.info("In GeoLocationAppDataAccessService:update : Before calling Database Operation : startTime : " +startTime);
			
			t = this.entityManager.merge(entity);
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			logger.info("In GeoLocationAppDataAccessService:update : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
			logger.info("In GeoLocationAppDataAccessService:update : Updated Enitty from database : " +t);
		}
		
		return t;
	}
	
	/**
	 * @param shopPin
	 * @param findShopByPinQuery
	 * @param searchKey
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It provides functionality to retrieve an Shop Entity Object from the Database based on Shop Pin Code.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@SuppressWarnings("unchecked")
	public List<ShopDetails> findShopByPin(long shopPin, String findShopByPinQuery, String searchKey) 
			throws GeoLocationAppException {
		logger.trace("In GeoLocationAppDataAccessService:findShopByPin : shopAddress : " +shopPin+ " : findShopByPinQuery : " +findShopByPinQuery+ " :searchKey : " +searchKey);
		
		long startTime = 0L;
		long endTime = 0L;
		
		List<ShopDetails> ShopDetailsList = new ArrayList<>();
		Session session = null;
		try {
			session = this.entityManager.unwrap(Session.class);
			
			startTime = System.currentTimeMillis();
			logger.info("In GeoLocationAppDataAccessService:findShopByPin : Before calling Database Operation : startTime : " +startTime);
			ShopDetailsList = session.createSQLQuery(findShopByPinQuery)
						   			 .addEntity(ShopDetails.class)
						   			 .setParameter(searchKey, shopPin)
						   			 .list();
			endTime = System.currentTimeMillis();
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			logger.info("In GeoLocationAppDataAccessService:findShopByPin : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
		}
		
		return ShopDetailsList;
	}
	
	/**
	 * @param findAllShopsNameQuery
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It provides functionality to retrieve all Shop Entity Objects from the Database.
	 * If any exception occurs it converts that Exception to Custom GeoLocationAppException and throw it back to the calling code.
	 */
	@SuppressWarnings("unchecked")
	public List<ShopDetails> findAllShops(String findAllShopsNameQuery) 
			throws GeoLocationAppException {
		logger.trace("In GeoLocationAppDataAccessService:findAllShops : findAllShopsNameQuery : " +findAllShopsNameQuery);
		
		long startTime = 0L;
		long endTime = 0L;
		
		List<ShopDetails> ShopDetailsList = new ArrayList<>();
		Session session = null;
		try {
			session = this.entityManager.unwrap(Session.class);
			
			startTime = System.currentTimeMillis();
			logger.info("In GeoLocationAppDataAccessService:findAllShops : Before calling Database Operation : startTime : " +startTime);
			ShopDetailsList = session.createSQLQuery(findAllShopsNameQuery)
						   			 .addEntity(ShopDetails.class)
						   			 .list();
			endTime = System.currentTimeMillis();
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			logger.info("In GeoLocationAppDataAccessService:findAllShops : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
		}
		
		return ShopDetailsList;
	}

}
