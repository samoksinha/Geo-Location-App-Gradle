package com.sam.geolocationapp.utility;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class GeoLocationAppErrorConfigurator implements ApplicationListener<ContextRefreshedEvent> {

	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppErrorConfigurator.class);
	
	protected static boolean contextStratFlag = false;
	
	@Value("${com.sam.geolocation.errorMessage.file.name}")
	protected String errorPropertyFileName;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		LOGGER.info("Entering ErrorsPropertyConfigurerStartUp:onApplicationEvent : with : contextStratFlag : " +contextStratFlag);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Error Config File Name : " +errorPropertyFileName);
		}
		
		if(!contextStratFlag) {
			GeoLocationAppConstants.GEO_LOCATION_ERROR_MAP = loadAndReturnPropertiesToMap();
			contextStratFlag = true;
		}
		LOGGER.info("Leaving ErrorsPropertyConfigurerStartUp:onApplicationEvent : contextStratFlag : " +contextStratFlag);
	}

	private Map<String, String> loadAndReturnPropertiesToMap() {
		LOGGER.info("Entering ErrorsPropertyConfigurerStartUp:loadAndReturnPropertiesToMap");
		
		Map<String, String> errorMap = null;
		Properties  errorProperties = null;
		try (InputStream errorPropertiesFileInputStream = this.getClass().getResourceAsStream(errorPropertyFileName)) {
			errorMap = new HashMap<String,String>();
			errorProperties = new Properties();
			
			errorProperties.load(errorPropertiesFileInputStream);
			for (final String name: errorProperties.stringPropertyNames()) {
				errorMap.put(name.trim(), errorProperties.getProperty(name).trim());
			}
		} catch(Exception e) {
			LOGGER.error("Exception Occured in startUp ::" ,e);
		} catch(Throwable t) {
			LOGGER.error("Fatal Error in startUp ::" ,t);
		} finally {
			LOGGER.info("Leaving ErrorsPropertyConfigurerStartUp:loadAndReturnPropertiesToMap");
		}
		
		return Collections.unmodifiableMap(errorMap);
	}
}
