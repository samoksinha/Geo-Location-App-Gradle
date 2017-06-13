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

@Service
@Transactional(TxType.REQUIRED)
public class GeoLocationAppDataAccessService {

	private static final Logger logger = LogManager.getLogger(GeoLocationAppDataAccessService.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public GeoLocationAppDataAccessService() {
	}
	
	public void create(Object entity) {
		long startTime = System.currentTimeMillis();
		logger.info("In GeoLocationAppDataAccessService:create : Before calling Database Operation : startTime : " +startTime);
		logger.info("In GeoLocationAppDataAccessService:create : entity : " +entity);
		
		this.entityManager.persist(entity);
		this.entityManager.flush();
		long endTime = System.currentTimeMillis();
		
		logger.info("In GeoLocationAppDataAccessService:create : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
		logger.info("In GeoLocationAppDataAccessService:create : Created Enitty into database : " +entity);
	}

	public <T> T find(Class<T> type, String id) {
		long startTime = System.currentTimeMillis();
		logger.info("In GeoLocationAppDataAccessService:find : Before calling Database Operation : startTime : " +startTime);
		logger.info("In GeoLocationAppDataAccessService:find : id : " +id);
		
		T t = this.entityManager.find(type, id);
		long endTime = System.currentTimeMillis();
		
		logger.info("In GeoLocationAppDataAccessService:find : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
		return t;
	}

	public <T> T updateTransanctionLog(T entity) {
		long startTime = System.currentTimeMillis();
		logger.info("In GeoLocationAppDataAccessService:update : Before calling Database Operation : startTime : " +startTime);
		logger.info("In GeoLocationAppDataAccessService:update : entity : " +entity);
		
		T t = this.entityManager.merge(entity);
		long endTime = System.currentTimeMillis();
		
		logger.info("In GeoLocationAppDataAccessService:find : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public List<ShopDetails> findShopByName(String shopName, String findShopByNameQuery, String searchKey) 
			throws GeoLocationAppException {
		logger.trace("In GeoLocationAppDataAccessService:findShopByName : shopName : " +shopName+ " : findShopByNameQuery : " +findShopByNameQuery+ " :searchKey : " +searchKey);
		
		long startTime = 0L;
		long endTime = 0L;
		
		List<ShopDetails> ShopDetailsList = new ArrayList<>();
		Session session = null;
		try {
			session = this.entityManager.unwrap(Session.class);
			
			startTime = System.currentTimeMillis();
			logger.info("In GeoLocationAppDataAccessService:findShopByName : Before calling Database Operation : startTime : " +startTime);
			ShopDetailsList = session.createSQLQuery(findShopByNameQuery)
						   			 .addEntity(ShopDetails.class)
						   			 .setParameter(searchKey, shopName)
						   			 .list();
			endTime = System.currentTimeMillis();
			
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		} finally {
			logger.info("In GeoLocationAppDataAccessService:findShopByName : After calling Database Operation : endTime : " +endTime+ " : Total time taken : " +(endTime - startTime));
		}
		
		return ShopDetailsList;
	}
	
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
