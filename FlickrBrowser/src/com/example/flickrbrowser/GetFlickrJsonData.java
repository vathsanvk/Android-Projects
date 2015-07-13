package com.example.flickrbrowser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.provider.Contacts.Photos;
import android.util.Log;

public class GetFlickrJsonData extends GetRawData {

	private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
	private List<Photo> photos;
	private Uri destinationUri;

	public GetFlickrJsonData(String searchCriteria, boolean matchAll) {
		super(null);
		createAndUpdateUri(searchCriteria, matchAll);
		photos = new ArrayList<Photo>();
	}

	public void execute() {
		super.setmRawUrl(destinationUri.toString());
		DownloadJsonData downloadJsonData = new DownloadJsonData();
		Log.v(LOG_TAG, "Built Uri = " + destinationUri.toString());
		downloadJsonData.execute(destinationUri.toString());
	}

	public boolean createAndUpdateUri(String searchCriteria, boolean matchAll) {
		final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
		final String TAGS_PARAM = "tags";
		final String TAGMODE_PARAM = "tagmode";
		final String FORMAT_PARAM = "format";
		final String NO_JSON_CALLBACK_PARAM = "nojsoncallback";

		destinationUri = Uri.parse(FLICKR_API_BASE_URL).buildUpon()
				.appendQueryParameter(TAGS_PARAM, searchCriteria)
				.appendQueryParameter(TAGMODE_PARAM, matchAll ? "ALL" : "ANY")
				.appendQueryParameter(FORMAT_PARAM, "json")
				.appendQueryParameter(NO_JSON_CALLBACK_PARAM, "1").build();

		return destinationUri != null;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void processResult() {
		if (getmDownloadStatus() != DownloadStatus.OK) {
			Log.e(LOG_TAG, "Error downloading raw file");
			return;
		}

		final String FLICKR_ITEMS = "items";
		final String FLICKR_TITLE = "title";
		final String FLICKR_MEDIA = "media";
		final String FLICKR_PHOTO_URL = "m";
		final String FLICKR_AUTHOR = "author";
		final String FLICKR_AUTHOR_ID = "author_id";
		final String FLICKR_LINK = "link";
		final String FLICKR_TAGS = "tags";

		try {
			JSONObject jsonData = new JSONObject(getmData());
			JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS);
			for (int i = 0; i < itemsArray.length(); i++) {
				JSONObject jsonPhoto = itemsArray.getJSONObject(i);
				String title = jsonPhoto.getString(FLICKR_TITLE);
				String author = jsonPhoto.getString(FLICKR_AUTHOR);
				String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);
				String link = jsonPhoto.getString(FLICKR_LINK);
				String tags = jsonPhoto.getString(FLICKR_TAGS);

				JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
				String photoUrl = jsonMedia.getString(FLICKR_PHOTO_URL);

				Photo photoObject = new Photo(title, author, authorId, link,
						photoUrl, tags);
				this.photos.add(photoObject);
			}

			for (Photo singlePhoto : photos) {
				Log.v(LOG_TAG, singlePhoto.toString());
			}
		} catch (JSONException jsone) {
			jsone.printStackTrace();
			Log.e(LOG_TAG, "Error processing JSON data");
		}

	}

	public class DownloadJsonData extends DownloadRawData {
		protected void onPostExecute(String webData) {
			super.onPostExecute(webData);
			processResult();
		}

		protected String doInBackground(String... params) {
			String[] par = {destinationUri.toString()};
			return super.doInBackground(par);
		}
	}
}
