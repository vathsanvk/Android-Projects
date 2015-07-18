package com.example.nprclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends Activity {

	ProgressDialog progressDialog;
	ListView listView;
	String contentValue;
	public final static String CONTENT_KEY = "content_key";
	public final static String STORY_ID = "story_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		listView = (ListView) findViewById(R.id.listView);

		contentValue = getIntent().getExtras().getString(
				MainActivity.CONTENT_KEY);

		if (contentValue.equals("programs")) {

			new GetProgramsAsyncTask()
					.execute("http://api.npr.org/list?id=3004&output=JSON");

		} else if (contentValue.equals("topics")) {
			new GetTopicsAsyncTask()
					.execute("http://api.npr.org/list?id=3002&output=JSON");
		}
	}

	public class GetProgramsAsyncTask extends
			AsyncTask<String, Void, ArrayList<Program>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(ListActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Programs. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected ArrayList<Program> doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(params[0]);
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

					return ContentUtil.ContentJSONParser.parsePrograms(sb
							.toString());
				}

			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Program> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			final ArrayList<Program> programs = result;

			ArrayAdapter<Program> adapter = new ArrayAdapter<Program>(
					ListActivity.this, android.R.layout.simple_list_item_1,
					programs);

			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);
			progressDialog.dismiss();

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(ListActivity.this,
							StoriesActivity.class);
					intent.putExtra(CONTENT_KEY, "programs");
					intent.putExtra(STORY_ID, programs.get(position).getId());
					startActivity(intent);

				}

			});

		}

	}

	public class GetTopicsAsyncTask extends
			AsyncTask<String, Void, ArrayList<Topic>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(ListActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Topics. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected ArrayList<Topic> doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(params[0]);
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

					return ContentUtil.ContentJSONParser.parseTopics(sb
							.toString());
				}

			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Topic> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			final ArrayList<Topic> topics = result;

			ArrayAdapter<Topic> adapter = new ArrayAdapter<Topic>(
					ListActivity.this, android.R.layout.simple_list_item_1,
					topics);

			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);
			progressDialog.dismiss();

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent intent = new Intent(ListActivity.this,
							StoriesActivity.class);
					intent.putExtra(CONTENT_KEY, "topics");
					intent.putExtra(STORY_ID, topics.get(position).getId());
					startActivity(intent);
				}

			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
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
