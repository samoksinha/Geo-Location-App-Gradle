package com.sam.geolocationapp.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shop_details")
public class ShopDetails implements Serializable {

	private static final long serialVersionUID = 7557097983806949305L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="shop_id")
	private Long shopId;

	@Column(name="shop_name", length=500)
	private String shopName;
	
	@Column(name="shop_address", length=500)
	private String shopAddress;
	
	@Column(name="shop_pincode", length=20)
	private long shopPincode;
	
	@Column(name="shop_latitude", length=40)
	private String shopLatitude;
	
	@Column(name="shop_longitude", length=40)
	private String shopLongitude;
	
	public ShopDetails() {
	}

	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public long getShopPincode() {
		return shopPincode;
	}
	public void setShopPincode(long shopPincode) {
		this.shopPincode = shopPincode;
	}

	public String getShopLatitude() {
		return shopLatitude;
	}
	public void setShopLatitude(String shopLatitude) {
		this.shopLatitude = shopLatitude;
	}

	public String getShopLongitude() {
		return shopLongitude;
	}
	public void setShopLongitude(String shopLongitude) {
		this.shopLongitude = shopLongitude;
	}

	@Override
	public String toString() {
		return "ShopDetails [shopId=" + shopId + ", shopName=" + shopName + ", shopAddress=" + shopAddress
				+ ", shopPincode=" + shopPincode + ", shopLatitude=" + shopLatitude + ", shopLongitude=" + shopLongitude
				+ "]";
	}
	
}
