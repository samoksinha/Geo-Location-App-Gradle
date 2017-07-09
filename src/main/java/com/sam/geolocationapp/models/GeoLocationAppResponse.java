package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Samok Sinha
 * 
 * This class is the Model Response class which provides the necessary information back to the
 * End client. This class can contain status and Object reference of any Object including GeoLocationAppRequest.
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status",
					"response"
				  })
public class GeoLocationAppResponse {

	private String status;
	private Object responseObject;
	
	public GeoLocationAppResponse() {
	}
	
	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Object getResponseObject() {
		return responseObject;
	}
	@JsonProperty("response")
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}
	
	@Override
	public String toString() {
		return "GeoLocationAppResponse [status=" + status + ", responseObject=" + responseObject + "]";
	}
	
}
