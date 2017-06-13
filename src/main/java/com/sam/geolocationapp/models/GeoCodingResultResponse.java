package com.sam.geolocationapp.models;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"address_components",
					"formatted_address",
					"geometry",
					"partial_match",
					"place_id",
					"post_code_localities",
					"types"
				  })
public class GeoCodingResultResponse {

	protected AddressComponent[] addressComponents;
	protected String formattedAddress;
	protected Geometry geometry;
	public boolean partialMatch;
	public String[] postcodeLocalities;
	protected String[] types;
	protected String placeId;
	
	public GeoCodingResultResponse() {
	}
	
	public AddressComponent[] getAddressComponents() {
		return addressComponents;
	}
	@JsonProperty("address_components")
	public void setAddressComponents(AddressComponent[] addressComponents) {
		this.addressComponents = addressComponents;
	}
	
	public String getFormattedAddress() {
		return formattedAddress;
	}
	@JsonProperty("formatted_address")
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
	
	public Geometry getGeometry() {
		return geometry;
	}
	@JsonProperty("geometry")
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public boolean isPartialMatch() {
		return partialMatch;
	}
	@JsonProperty("partial_match")
	public void setPartialMatch(boolean partialMatch) {
		this.partialMatch = partialMatch;
	}

	public String[] getPostcodeLocalities() {
		return postcodeLocalities;
	}
	@JsonProperty("post_code_localities")
	public void setPostcodeLocalities(String[] postcodeLocalities) {
		this.postcodeLocalities = postcodeLocalities;
	}

	public String[] getTypes() {
		return types;
	}
	@JsonProperty("types")
	public void setTypes(String[] types) {
		this.types = types;
	}
	
	public String getPlaceId() {
		return placeId;
	}
	@JsonProperty("place_id")
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	@Override
	public String toString() {
		return "GeoCodingResultResponse [addressComponents=" + Arrays.toString(addressComponents)
				+ ", formattedAddress=" + formattedAddress + ", geometry=" + geometry + ", partialMatch=" + partialMatch
				+ ", postcodeLocalities=" + Arrays.toString(postcodeLocalities) + ", types=" + Arrays.toString(types)
				+ ", placeId=" + placeId + "]";
	}
	
}
