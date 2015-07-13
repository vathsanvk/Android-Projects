package com.example.photoviewerxml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.util.Log;

public class RequestParams {
	String method, baseUrl;
	HashMap<String, String> params = new HashMap<String, String>();

	public RequestParams(String method, String baseUrl) {
		super();
		this.method = method;
		this.baseUrl = baseUrl;
	}

	public void addParams(String key, String value) {
		params.put(key, value);
	}

	public String getEncodedParams() {
		// loop over the key value params, append to a stringbuilder key=value
		// and add & after each pair
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet()) {
			try {
				String value = URLEncoder.encode(params.get(key), "UTF-8");
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(key + "=" + value);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public String getEncodedUrl() {
	
		return this.baseUrl  + getEncodedParams();
	}
	
	public HttpURLConnection setupConnection() throws IOException{
		if(method.equals("GET")){
			URL url = new URL(getEncodedUrl());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			return con;
		}
		return null;
	}
}
