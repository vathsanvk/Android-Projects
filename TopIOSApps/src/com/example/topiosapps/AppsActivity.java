package com.example.topiosapps;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AppsActivity extends Activity {

	ProgressDialog progressDialog;
	ArrayList<App> apps;
	ListView listView;
	AppAdapter adapter;
	ArrayList<App> historyTemp, history;
	SharedPreferences sharedPreferences;
	public final static String APPS_KEY = "apps_key";
	public final static String HISTORY = "history";
	public final static int APPS_ACTIVITY_ID = 101;
	AppAdapter adapterHistory;
	String inputType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apps);
		listView = (ListView) findViewById(R.id.listView);

		history = new ArrayList<App>();
		loadSavedPreferences();

		inputType = getIntent().getExtras().getString(MainActivity.MAIN_KEY);

		if (inputType.equals("appsList")) {
			new GetAppAsyncTask()
					.execute("https://itunes.apple.com/us/rss/topgrossingapplications/limit=20/xml");
		} else if (inputType.equals("history")) {

			loadHistory();

		}

	}

	public class GetAppAsyncTask extends
			AsyncTask<String, Void, ArrayList<App>> {

		@Override
		protected ArrayList<App> doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestMethod("GET");
				con.connect();

				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream in = con.getInputStream();
					Log.d("TAG", in.toString());
					return AppUtil.AppPullParser.parseApp(in);

				}
			} catch (IOException | XmlPullParserException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(AppsActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Apps. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected void onPostExecute(ArrayList<App> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			apps = result;
			adapter = new AppAdapter(AppsActivity.this,
					R.layout.row_item_layout, apps);
			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);
			progressDialog.dismiss();

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					history.add(apps.get(position));
					Intent intent = new Intent(AppsActivity.this,
							PreviewActivity.class);
					intent.putExtra(APPS_KEY, apps.get(position));
					startActivity(intent);

				}

			});

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		saveToSharedPreferences();
	}

	public void loadSavedPreferences() {
		sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
		if (sharedPreferences.contains(HISTORY)) {

			String jsonTasks = sharedPreferences.getString(HISTORY, null);
			Gson gson = new Gson();
			App[] st = gson.fromJson(jsonTasks, App[].class);

			for (App t : st) {
				history.add(t);
			}
		}
	}

	public void saveToSharedPreferences() {
		Editor editor;
		sharedPreferences = AppsActivity.this.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String jsonTasks = gson.toJson(history);

		editor.putString(HISTORY, jsonTasks);

		editor.commit();
	}

	public void loadHistory() {
		adapterHistory = new AppAdapter(AppsActivity.this,
				R.layout.row_item_layout, history);
		adapterHistory.setNotifyOnChange(true);
		listView.setAdapter(adapterHistory);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(AppsActivity.this,
						PreviewActivity.class);
				intent.putExtra(APPS_KEY, history.get(position));
				startActivity(intent);

			}

		});
	}

	public class CustomComparator implements Comparator<App> {

		String compareData;

		public CustomComparator(String compareData) {
			this.compareData = compareData;
		}

		@Override
		public int compare(App lhs, App rhs) {
			// TODO Auto-generated method stub

			if (compareData.equals("developer")) {
				return lhs.getDevName().compareTo(rhs.getDevName());
			} else if (compareData.equals("title")) {
				return lhs.getTitle().compareTo(rhs.getTitle());
			} else if (compareData.equals("price")) {
				Double v1 = Double.parseDouble(lhs.getPrice());
				Double v2 = Double.parseDouble(rhs.getPrice());
				return v1.compareTo(v2);
			}

			return 0;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.apps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (inputType.equals("appsList")) {
			if (id == R.id.sortDevName) {
				Collections.sort(apps, new CustomComparator("developer"));
				adapter.notifyDataSetChanged();
				return true;
			} else if (id == R.id.sortPrice) {
				Collections.sort(apps, new CustomComparator("price"));
				adapter.notifyDataSetChanged();
				return true;

			} else if (id == R.id.sortTitle) {
				Collections.sort(apps, new CustomComparator("title"));
				adapter.notifyDataSetChanged();
				return true;
			}
		}

		if (id == R.id.clearHistory) {
              if(history.size() != 0){
            	  history.removeAll(history);
            	  adapterHistory.notifyDataSetChanged();
              }
		}
		return super.onOptionsItemSelected(item);
	}
}
