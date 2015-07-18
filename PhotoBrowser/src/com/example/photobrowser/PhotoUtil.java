package com.example.photobrowser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoUtil {
   static public class PhotosJSONParser{
	   static ArrayList<Photo> parsePhotos(String in) throws JSONException{
		   ArrayList<Photo> photosList = new ArrayList<Photo>();
		   
		   JSONObject root = new JSONObject(in);
		   JSONArray photosJSONArray = root.getJSONArray("photos");
		   
		   for(int i=0;i<photosJSONArray.length();i++){
			   JSONObject photoJSONObject = photosJSONArray.getJSONObject(i);
			   Photo photo = new Photo();
			   photo.setTitle(photoJSONObject.getString("name"));
			   photo.setUrl(photoJSONObject.getString("image_url"));
			   photo.setName(photoJSONObject.getJSONObject("user").getString("username"));
			   photo.set_id(photoJSONObject.getInt("id"));
			   
			   photosList.add(photo);
			   
		   }
		   
		   
		   return photosList;
		   
	   }
   }
}
