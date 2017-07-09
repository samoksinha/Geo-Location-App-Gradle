package com.sam.geolocationapp.exceptions;

/**
 * @author Samok Sinha
 * 
 * This Enum provides the 3 different Exception Type that the application will propagate.
 *
 */

public enum ExceptionType { 
	
	TECHNNICAL("TECHINICAL"),
	INTERNAL("INTERNAL"),
	BUSINESS("BUSINESS");
	
	private String exType;
	
	private ExceptionType(String exteptionType) {
		this.exType=exteptionType;
	}
	
	public String getExceptionType() {
		return exType;
	}
}