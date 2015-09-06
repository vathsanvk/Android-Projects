package com.srivathsanvk.flickrnetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Srivathsan on 03-Sep-15.
 */
public class PhotoUtil {
    static public class PhotosJSONParser {
        static ArrayList<Photo> parsePhotos(String in) throws JSONException {
            ArrayList<Photo> photosList = new ArrayList<Photo>();

            JSONObject root = new JSONObject(in);
            JSONObject photosJSONObject = root.getJSONObject("photos");
            JSONArray photoJSONArray = photosJSONObject.getJSONArray("photo");

            for (int i = 0; i < photoJSONArray.length(); i++) {
                JSONObject photoJSONObject = photoJSONArray.getJSONObject(i);
                Photo photo = new Photo();

                photo.setPhotoUrl(photoJSONObject.getString("url_m"));

                photosList.add(photo);

            }


            return photosList;

        }
    }
}
