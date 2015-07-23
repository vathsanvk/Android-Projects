package com.example.topiosapps;

import java.io.Serializable;

public class App implements Serializable {
	String id;
	String price;
	String title, devName, url, smallPhotoUrl, LargePhotoUrl, rlsDate;
	
	


	public String getRlsDate() {
		return rlsDate;
	}

	public void setRlsDate(String rlsDate) {
		this.rlsDate = rlsDate;
	}

	
	@Override
	public String toString() {
		return "App [id=" + id + ", price=" + price + ", title=" + title
				+ ", devName=" + devName + ", url=" + url + ", smallPhotoUrl="
				+ smallPhotoUrl + ", LargePhotoUrl=" + LargePhotoUrl
				+ ", rlsDate=" + rlsDate + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSmallPhotoUrl() {
		return smallPhotoUrl;
	}

	public void setSmallPhotoUrl(String smallPhotoUrl) {
		this.smallPhotoUrl = smallPhotoUrl;
	}

	public String getLargePhotoUrl() {
		return LargePhotoUrl;
	}

	public void setLargePhotoUrl(String largePhotoUrl) {
		LargePhotoUrl = largePhotoUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
