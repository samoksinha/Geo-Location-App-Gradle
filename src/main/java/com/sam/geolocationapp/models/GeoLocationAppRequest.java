package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"shopId",
					"shopName",
					"shopAddress",
					"shopPincode",
					"shopLatitude",
					"shopLongitude",
					"distance",
					"errorCode",
					"errorMessage"
				  })
public class GeoLocationAppRequest {

	private Long shopId;
	private String shopName;
	private String shopAddress;
	private long shopPincode;
	private String shopLatitude;
	private String shopLongitude;
	private String distance;
	private String errorCode;
	private String errorMessage;
	
	public GeoLocationAppRequest() {
	}
	
	public GeoLocationAppRequest(Long shopId, String shopName, String shopAddress, 
								 long shopPincode, String shopLatitude, String shopLongitude,
								 String distance) {
		this.shopId = shopId;
		this.shopName = shopName;
		this.shopAddress = shopAddress;
		this.shopPincode = shopPincode;
		this.shopLatitude = shopLatitude;
		this.shopLongitude = shopLongitude;
		this.distance = distance;
	}

	public Long getShopId() {
		return shopId;
	}
	@JsonProperty("shopId")
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
	public String getShopName() {
		return shopName;
	}
	@JsonProperty("shopName")
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}
	@JsonProperty("shopAddress")
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public long getShopPincode() {
		return shopPincode;
	}
	@JsonProperty("shopPincode")
	public void setShopPincode(long shopPincode) {
		this.shopPincode = shopPincode;
	}
	
	public String getShopLatitude() {
		return shopLatitude;
	}
	@JsonProperty("shopLatitude")
	public void setShopLatitude(String shopLatitude) {
		this.shopLatitude = shopLatitude;
	}

	public String getShopLongitude() {
		return shopLongitude;
	}
	@JsonProperty("shopLongitude")
	public void setShopLongitude(String shopLongitude) {
		this.shopLongitude = shopLongitude;
	}

	public String getDistance() {
		return distance;
	}
	@JsonProperty("distance")
	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getErrorCode() {
		return errorCode;
	}
	@JsonProperty("errorCode")
	public void setErrorCode(String errorCode) {
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
		return "GeoLocationAppRequest [shopId=" + shopId + ", shopName=" + shopName + ", shopAddress=" + shopAddress
				+ ", shopPincode=" + shopPincode + ", shopLatitude=" + shopLatitude + ", shopLongitude=" + shopLongitude
				+ ", distance=" + distance + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}
	
}
