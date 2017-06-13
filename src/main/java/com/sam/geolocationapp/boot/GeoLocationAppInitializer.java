package com.sam.geolocationapp.boot;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages="com.sam.geolocationapp")
public class GeoLocationAppInitializer extends SpringBootServletInitializer {
	
	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppInitializer.class);

	public GeoLocationAppInitializer() {
	}
	
	public static void main(String[] args) {
		LOGGER.info("In GeoLocationAppInitializer:main : Starts Executing Geo Location App : ");
		try {	
			SpringApplication.run(GeoLocationAppInitializer.class, args);
		} catch (Exception e) {
			LOGGER.error("Generic Exception Occured In GeoLocationAppInitializer:main : Stacktrace : " ,e);
		}
	}
	
}
