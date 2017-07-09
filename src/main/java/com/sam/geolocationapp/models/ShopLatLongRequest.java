package com.sam.geolocationapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Samok Sinha
 * 
 * This class is the Model request class to receive latitude and longitude information from the End Client
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"shopLatitude",
					"shopLongitude",
				  })
public class ShopLatLongRequest {

	@ApiModelProperty(required=true, position=1, dataType="java.lang.String", example="22.5074")
	private String shopLatitude;
	@ApiModelProperty(required=true, position=2, dataType="java.lang.String", example="88.3873")
	private String shopLongitude;
	
	public ShopLatLongRequest() {
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

	@Override
	public String toString() {
		return "ShopLatLongRequest [shopLatitude=" + shopLatitude + ", shopLongitude=" + shopLongitude + "]";
	}
	
}
