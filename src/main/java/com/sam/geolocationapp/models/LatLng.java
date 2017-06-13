package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"lat",
					"lng"
				  })
public class LatLng {

	protected String lat;
	protected String lng;

	public LatLng() {
	}

	public String getLat() {
		return lat;
	}
	@JsonProperty("lat")
	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}
	@JsonProperty("lng")
	public void setLng(String lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "LatLng [lat=" + lat + ", lng=" + lng + "]";
	}
}
