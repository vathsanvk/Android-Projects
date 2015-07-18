package com.example.photobrowser;

import java.io.Serializable;

public class Photo implements Serializable{
	
	String title, url, name;
	int _id;
	public int get_id() {
		return _id;
	}



	public void set_id(int _id) {
		this._id = _id;
	}



	@Override
	public String toString() {
		return title;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
