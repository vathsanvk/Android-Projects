package com.example.photoviewerxml;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText searchPhoto;
	Photo photo;
	ArrayList<Photo> photoUrls;
	ProgressDialog progressDialog;
	int photoIndex = 0;
	Switch switchParser;
	String parserType;
	ImageView imagePrev, imageNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		switchParser = (Switch) findViewById(R.id.switchParser);
		imagePrev = (ImageView) findViewById(R.id.imagePrev);
		imagePrev.setVisibility(ImageView.INVISIBLE);

		imageNext = (ImageView) findViewById(R.id.imageNext);

		imageNext.setVisibility(ImageView.INVISIBLE);

		imagePrev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (photoIndex == 0) {
					photoIndex = photoUrls.size() - 1;
				} else {
					photoIndex--;
				}
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				progressDialog.setMessage("Loading Photo. .");
				progressDialog.setCancelable(false);

				progressDialog.show();
				new DownloadPhoto().execute(photoUrls);
			}
		});

		imageNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (photoIndex == (photoUrls.size() - 1)) {
					photoIndex = 0;
				} else {
					photoIndex++;
				}
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				progressDialog.setMessage("Loading Photo. .");
				progressDialog.setCancelable(false);

				progressDialog.show();
				new DownloadPhoto().execute(photoUrls);
			}
		});

		parserType = "PULL";
		switchParser.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					parserType = "SAX";

				} else {
					parserType = "PULL";

				}

			}
		});

		findViewById(R.id.goButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						searchPhoto = (EditText) findViewById(R.id.searchPhoto);
						photoUrls = new ArrayList<Photo>();
						if (isConnectedOnline()) {
							// TODO Auto-generated method stub
							RequestParams params = new RequestParams("GET",
									"https://api.flickr.com/services/rest/?");
							params.addParams("method", "flickr.photos.search");
							params.addParams("api_key",
									"8516682144ce1c10d700f8d752d5100f");
							params.addParams("text", searchPhoto.getText()
									.toString());
							params.addParams("extras", "url_m");
							params.addParams("per_page", "20");
							params.addParams("format", "rest");

							new GetPhotoAsyncTask().execute(params);
							imageNext.setVisibility(ImageView.VISIBLE);
							imagePrev.setVisibility(ImageView.VISIBLE);
						} else {
							Toast.makeText(MainActivity.this, "No network",
									Toast.LENGTH_LONG).show();
						}
					}
				});

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

	public class DownloadPhoto extends
			AsyncTask<ArrayList<Photo>, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(ArrayList<Photo>... params) {
			// TODO Auto-generated method stub
			URL url;
			try {
				if (!params[0].isEmpty()) {
					url = new URL(params[0].get(photoIndex).getUrl());
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setRequestMethod("GET");
					Bitmap image = BitmapFactory.decodeStream(con
							.getInputStream());

					return image;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				ImageView iv = (ImageView) findViewById(R.id.imagePhoto);
				iv.setImageBitmap(result);
				iv.setScaleType(ScaleType.FIT_XY);
				progressDialog.dismiss();
			} else {
				progressDialog.dismiss();
				Toast.makeText(MainActivity.this, "No results",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	public class GetPhotoAsyncTask extends
			AsyncTask<RequestParams, Void, ArrayList<Photo>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Photo. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected void onPostExecute(ArrayList<Photo> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// progressDialog.dismiss();
			for (Photo p : result) {
				photoUrls.add(p);
			}
			new DownloadPhoto().execute(photoUrls);
		}

		@Override
		protected ArrayList<Photo> doInBackground(RequestParams... params) {
			// TODO Auto-generated method stub
			URL url;
			try {

				HttpURLConnection con = params[0].setupConnection();

				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream in = con.getInputStream();
					Log.d("TAG", in.toString());

					if (parserType.equals("SAX")) {
						return PhotoUtil.PhotoSAXParser.parsePhoto(in);
					} else {
						return PhotoUtil.PhotoPullParser.parsePhoto(in);
					}

				}
			} catch (IOException | SAXException | XmlPullParserException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}
			return null;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
