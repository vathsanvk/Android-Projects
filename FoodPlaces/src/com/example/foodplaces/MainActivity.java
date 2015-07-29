package com.example.foodplaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {

	GoogleMap gMap;
	String cityState;
	EditText dialogText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			Parse.initialize(this, "Jlf1i4UsKvB79R6on5wkrMsxclLh3PPADiDpFvwy",
					"vBSf0fGI02uyvbE7LbUkEW98IG6MsSHyCKW15KKg");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		/*
		 * ParseUser.enableAutomaticUser();
		 * ParseUser.getCurrentUser().increment("RunCount");
		 * ParseUser.getCurrentUser().saveInBackground();
		 */

		/*
		 * gMap = ((MapFragment)
		 * getFragmentManager().findFragmentById(R.id.map)) .getMap();
		 * gMap.setMyLocationEnabled(true);
		 * gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
		 * 35.2270869, -80.8431267), 13));
		 * 
		 * gMap.addMarker(new MarkerOptions().position( new LatLng(35.2270869,
		 * -80.8431267)).title("Hello from CLT"));
		 */

		if (!Geocoder.isPresent()) {

			Toast.makeText(this, "No GeoCoding", Toast.LENGTH_LONG).show();
		}

	}

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub ParseUser.logOut(); ParseUser currentUser =
	 * ParseUser.getCurrentUser(); super.onBackPressed(); }
	 */

	public void getCityState() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View layout = View.inflate(this, R.layout.custom_dialog, null);

		dialogText = ((EditText) layout.findViewById(R.id.dialogText));
		builder.setView(layout);

		builder.setTitle("Enter a location")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (dialogText != null
								&& dialogText.getText().toString().length() != 0) {

							cityState = dialogText.getText().toString();
							if (cityState != null && cityState.length() != 0) {
								Log.d("TAG", cityState);
								new GeoTask(MainActivity.this)
										.execute(cityState);
							}

						} else {
							Toast.makeText(MainActivity.this,
									"Please complete the text",
									Toast.LENGTH_LONG).show();
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});

		AlertDialog alert = builder.create();

		alert.show();

	}

	class GeoTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected void onPostExecute(List<Address> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result.size() != 0) {
				Address addr = result.get(0);
				ParseGeoPoint userPoint = new ParseGeoPoint(addr.getLatitude(),
						addr.getLongitude());
				ParseObject obj = new ParseObject("Places");
				obj.put("location", userPoint);
				generateParseQuery(obj);
			}

		}

		void generateParseQuery(ParseObject obj) {
			ParseGeoPoint userLocation = (ParseGeoPoint) obj.get("location");

			ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
			query.whereWithinMiles("location", userLocation, 50);

			query.setLimit(10);
			query.findInBackground(new FindCallback<ParseObject>() {

				public void done(List<ParseObject> results, ParseException e) {
					if (e != null) {
						// There was an error
						e.printStackTrace();
					} else {
						// results have all the Posts the current user liked.
						Log.d("TAG", results.toString());
						if (results.size() != 0) {
							markOnMap(results);
						} else {
							Toast.makeText(MainActivity.this, "No results",
									Toast.LENGTH_SHORT).show();
						}
					}
				}

			});
		}

		void markOnMap(List<ParseObject> results) {
			ArrayList<Marker> markers = new ArrayList<>();

			gMap.clear();

			for (int i = 0; i < results.size(); i++) {
				ParseGeoPoint point = (ParseGeoPoint) results.get(i).get(
						"location");

				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.position(new LatLng(point.getLatitude(), point
						.getLongitude()));
				markerOptions.title(results.get(i).get("name").toString());

				Marker marker = gMap.addMarker(markerOptions);
				
				markers.add(marker);
				
				marker.showInfoWindow();
			}

			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			for (Marker marker : markers) {
				builder.include(marker.getPosition());

			}
			LatLngBounds bounds = builder.build();
			int padding = 20; // offset from edges of the map in pixels
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
					padding);
			gMap.animateCamera(cu);
			

		}

		Context mContext;

		public GeoTask(Context context) {
			this.mContext = context;
		}

		@Override
		protected List<Address> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<Address> addressList = null;

			Geocoder geocoder = new Geocoder(mContext);

			try {
				addressList = geocoder.getFromLocationName(params[0], 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return addressList;
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
			getCityState();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.exit(0);
		finish();
	}
}
