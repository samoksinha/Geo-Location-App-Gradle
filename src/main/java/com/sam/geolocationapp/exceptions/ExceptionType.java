package com.sam.geolocationapp.exceptions;

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