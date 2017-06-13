package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"bounds",
					"location",
					"location_type",
					"viewport"
				  })
public class Geometry {
	
	private Bounds bounds;
	private LatLng location;
	private String locationType;
	private Bounds viewport;
	
	public Geometry() {
	}
  
	public Bounds getBounds() {
		return bounds;
	}
	@JsonProperty("bounds")
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
	
	public LatLng getLocation() {
		return location;
	}
	@JsonProperty("location")
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	public String getLocationType() {
		return locationType;
	}
	@JsonProperty("location_type")
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	public Bounds getViewport() {
		return viewport;
	}
	@JsonProperty("viewport")
	public void setViewport(Bounds viewport) {
		this.viewport = viewport;
	}

	@Override
	public String toString() {
		return "Geometry [bounds=" + bounds + ", location=" + location + ", locationType=" + locationType
				+ ", viewport=" + viewport + "]";
	}
  
}
