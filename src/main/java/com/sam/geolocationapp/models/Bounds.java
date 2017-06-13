package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"northeast",
					"southwest"
				  })
public class Bounds {
	
	protected LatLng northeast;
	protected LatLng southwest;
	
	public Bounds() {
	}

	public LatLng getNortheast() {
		return northeast;
	}
	@JsonProperty("northeast")
	public void setNortheast(LatLng northeast) {
		this.northeast = northeast;
	}

	public LatLng getSouthwest() {
		return southwest;
	}
	@JsonProperty("southwest")
	public void setSouthwest(LatLng southwest) {
		this.southwest = southwest;
	}

	@Override
	public String toString() {
		return "Bounds [northeast=" + northeast + ", southwest=" + southwest + "]";
	}
	
}
