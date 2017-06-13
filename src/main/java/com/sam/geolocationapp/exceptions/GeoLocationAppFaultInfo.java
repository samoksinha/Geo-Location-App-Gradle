package com.sam.geolocationapp.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"errorCode", 
					"errorMessage"
			  	  })
public class GeoLocationAppFaultInfo {

	private int errorCode;	
	private String errorMessage;

	public GeoLocationAppFaultInfo() {
	}
	
	public GeoLocationAppFaultInfo(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}
	@JsonProperty("errorCode")
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	@JsonProperty("errorMessage")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "GeoLocationAppFaultInfo [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}
	
}
	
