package com.sam.geolocationapp.models;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"long_name",
					"short_name",
					"types"
				  })
public class AddressComponent {
	
	protected String longName;
	protected String shortName;
	protected String[] types;
  
  	public AddressComponent() {
  	}

	public String getLongName() {
		return longName;
	}
	@JsonProperty("long_name")
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	@JsonProperty("short_name")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String[] getTypes() {
		return types;
	}
	@JsonProperty("types")
	public void setTypes(String[] types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "AddressComponent [longName=" + longName + ", shortName=" + shortName + ", types="
				+ Arrays.toString(types) + "]";
	}
	
}
