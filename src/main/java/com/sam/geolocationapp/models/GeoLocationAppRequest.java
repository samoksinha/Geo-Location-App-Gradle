package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Samok Sinha
 * 
 * This class is the Model Request class which provides the necessary Shop details information.
 * This class information is moving back an forward to the End Client.
 * It also wraps Entity class informations and converts it to this Model class information.
 * It also provides GeoCoding API related messages and codes to the end client.
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"shopId",
					"shopName",
					"shopAddress",
					"shopPincode",
					"shopLatitude",
					"shopLongitude",
					"distance",
					"geoApiErrorCode",
					"geoApiErrorMessage"
				  })
public class GeoLocationAppRequest {

	@ApiModelProperty(hidden=true)
	private Long shopId;
	@ApiModelProperty(required=true, position=1, dataType="java.lang.String", example="Shop1")
	private String shopName;
	@ApiModelProperty(required=true, position=2, dataType="java.lang.String", example="802, Purbachal Main Road")
	private String shopAddress;
	@ApiModelProperty(required=true, position=3, dataType="java.lang.String", example="700078")
	private long shopPincode;
	
	@ApiModelProperty(hidden=true)
	private String shopLatitude;
	@ApiModelProperty(hidden=true)
	private String shopLongitude;
	@ApiModelProperty(hidden=true)
	private String distance;
	
	@ApiModelProperty(hidden=true)
	private String errorCode;
	@ApiModelProperty(hidden=true)
	private String errorMessage;
	
	public GeoLocationAppRequest() {
	}
	
	public GeoLocationAppRequest(String shopLatitude, String shopLongitude) {
		this.shopLatitude = shopLatitude;
		this.shopLongitude = shopLongitude;
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
	@JsonProperty("geoApiErrorCode")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	@JsonProperty("geoApiErrorMessage")
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
