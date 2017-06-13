package com.sam.geolocationapp.exceptions;

import com.sam.geolocationapp.utility.GeoLocationAppConstants;
import com.sam.geolocationapp.utility.GeoLocationAppUtility;

public class GeoLocationAppException extends RuntimeException {

	private static final long serialVersionUID = 12345L;
	
	private int errorCode;
	private ExceptionType exceptionType;
	private GeoLocationAppFaultInfo geoLocationAppFaultInfo;
	
	public GeoLocationAppException() {
	}
	
	public GeoLocationAppException(ExceptionType exceptionType) {
		super(String.valueOf(GeoLocationAppConstants.INTERNAL_ERROR_CODE));
		this.exceptionType = exceptionType;
		this.geoLocationAppFaultInfo = GeoLocationAppUtility.resolveErrorMessage(exceptionType);
	}
	
	public GeoLocationAppException(ExceptionType exceptionType, Throwable e) {
		super(e);
		this.exceptionType = exceptionType;
		this.geoLocationAppFaultInfo = GeoLocationAppUtility.resolveErrorMessage(exceptionType);
	}
	
	public GeoLocationAppException(ExceptionType exceptionType, int errorCode) {
		super(String.valueOf(errorCode));
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
		this.geoLocationAppFaultInfo = GeoLocationAppUtility.resolveErrorMessage(exceptionType, errorCode);
	}
	
	public GeoLocationAppException(int errorCode, ExceptionType exceptionType, Throwable e) {
		super(String.valueOf(errorCode));
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
		this.geoLocationAppFaultInfo = GeoLocationAppUtility.resolveErrorMessage(exceptionType, errorCode);
	}

	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public GeoLocationAppFaultInfo getGeoLocationAppFaultInfo() {
		return geoLocationAppFaultInfo;
	}
	public void setGeoLocationAppFaultInfo(GeoLocationAppFaultInfo geoLocationAppFaultInfo) {
		this.geoLocationAppFaultInfo = geoLocationAppFaultInfo;
	}
	
}
