package com.sam.geolocationapp.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"results",
					"status"
				  })
public class GeoCodingResponse {

	protected List<GeoCodingResultResponse> geoCodingResultResponseList;
	protected String status;
	
	public GeoCodingResponse() {
	}

	public List<GeoCodingResultResponse> getGeoCodingResultResponseList() {
		return geoCodingResultResponseList;
	}
	@JsonProperty("results")
	public void setGeoCodingResultResponseList(List<GeoCodingResultResponse> geoCodingResultResponseList) {
		this.geoCodingResultResponseList = geoCodingResultResponseList;
	}

	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GeoCodingResponse [geoCodingResultResponseList=" + geoCodingResultResponseList + ", status=" + status
				+ "]";
	}
	
}
