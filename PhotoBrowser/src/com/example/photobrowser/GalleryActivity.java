package com.example.photobrowser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class GalleryActivity extends Activity {

	String search_key;
	ListView listView;
	ProgressDialog progressDialog;
	DatabaseDataManager dm;

	public static final String DETAIL_KEY = "detail_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		dm = new DatabaseDataManager(this);

		listView = (ListView) findViewById(R.id.listView);

		search_key = getIntent().getExtras()
				.getString(MainActivity.GALLERY_KEY);
		if (isConnectedOnline()) {
			// TODO Auto-generated method stub
			RequestParams params = new RequestParams("GET",
					"https://api.500px.com/v1/photos/search?");

			params.addParams("consumer_key",
					"W6CSsB7TGZKMOdlqVlftQO9LcfiArvoxxmrR6qS6");
			params.addParams("term", search_key);
			params.addParams("image_size", "4");
			params.addParams("rpp", "50");

			new GetPhotoAsyncTask().execute(params);

		} else {
			Toast.makeText(GalleryActivity.this, "No network",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onDestroy() {

		dm.close();
		super.onDestroy();
	}

	public class GetPhotoAsyncTask extends
			AsyncTask<RequestParams, Void, ArrayList<Photo>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(GalleryActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Results. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected ArrayList<Photo> doInBackground(RequestParams... params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(params[0].getEncodedUrl());
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = reader.readLine();
					while (line != null) {
						sb.append(line);
						line = reader.readLine();
					}

					return PhotoUtil.PhotosJSONParser
							.parsePhotos(sb.toString());
				}

			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Photo> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			final ArrayList<Photo> photos = result;

			/*
			 * ArrayAdapter<Photo> adapter = new ArrayAdapter<>(
			 * GalleryActivity.this, android.R.layout.simple_list_item_1,
			 * photos);
			 */

			PhotoAdapter adapter = new PhotoAdapter(GalleryActivity.this,
					R.layout.row_item, photos);
			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);
			progressDialog.dismiss();

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(GalleryActivity.this,
							DetailsActivity.class);
					intent.putExtra(DETAIL_KEY, photos.get(position));
					startActivity(intent);
				}

			});

			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					if (dm.getPhoto(photos.get(position).get_id()) == null) {
						dm.savePhoto(photos.get(position));
						Log.d("TAG", dm.getAllPhotos().toString());
						ImageView iv = (ImageView) view
								.findViewById(R.id.itemFav);

						iv.setImageResource(R.drawable.favorites_yellow);

					} else {
						dm.deletePhoto(photos.get(position));
						Log.d("TAG", dm.getAllPhotos().toString());
						ImageView iv = (ImageView) view
								.findViewById(R.id.itemFav);
						iv.setImageResource(R.drawable.favorites_grey);
					}
					return true;
				}

			});

		}
	}

	private boolean isConnectedOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
