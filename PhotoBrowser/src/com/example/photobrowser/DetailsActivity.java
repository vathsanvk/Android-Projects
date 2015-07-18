package com.example.photobrowser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailsActivity extends Activity {

	Photo photoObject;
	TextView photoName, photoOwner;
	ProgressDialog progressDialog;
	ProgressBar progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		photoObject = (Photo) getIntent().getExtras().getSerializable(GalleryActivity.DETAIL_KEY);
		
		photoName = (TextView) findViewById(R.id.photoName);
		photoOwner = (TextView) findViewById(R.id.photoOwner);
		progress = (ProgressBar) findViewById(R.id.progress);
		
		photoName.setText(photoObject.getTitle());
		photoOwner.setText(photoObject.getName());
		
		new GetImage().execute(photoObject.getUrl());
		
		
		
				
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	
	public class GetImage extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setVisibility(ProgressBar.VISIBLE);

		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				URL url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				Bitmap image = BitmapFactory.decodeStream(con.getInputStream());
				return image;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			ImageView photoImage = (ImageView) findViewById(R.id.photoImage);
			String uri = "@drawable/photo_not_found";

			int imageResource = getResources().getIdentifier(uri, null,
					getPackageName());

			Drawable res = getResources().getDrawable(imageResource);
			if(result != null){
				photoImage.setImageBitmap(result);
			} else{
				photoImage.setImageDrawable(res);
			}
			progress.setVisibility(ProgressBar.INVISIBLE);
			photoImage.setVisibility(ImageView.VISIBLE);
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
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
