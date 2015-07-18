package com.example.nprclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONException;

import android.R.menu;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class StoriesActivity extends Activity {

	String contentType;
	int storyID;
	ListView listView;
	ProgressDialog progressDialog;
	public final static String STORY_KEY = "story_key";
	public final static int STORIES_ACTIVITY_ID = 101;

	ArrayList<Story> stories;
	SharedPreferences sharedPreferences;
	LinkedList<Story> favs;
	final static String FAVORITES = "favorites";
	StoryAdapter adapterFav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stories);

		favs = new LinkedList<Story>();
		loadSavedPreferences();

		listView = (ListView) findViewById(R.id.storiesListView);

		if (getIntent().getExtras().getString(ListActivity.CONTENT_KEY) != null) {
			contentType = getIntent().getExtras().getString(
					ListActivity.CONTENT_KEY);
			storyID = getIntent().getExtras().getInt(ListActivity.STORY_ID);
		} else {
			contentType = getIntent().getExtras().getString(
					MainActivity.CONTENT_KEY);
		}

		if (contentType.equals("programs")) {
			RequestParams params = new RequestParams("GET",
					"http://api.npr.org/query?");

			params.addParams("apiKey", "MDE5ODg4ODY4MDE0MzY5ODEzMzUzNGM3Zg001");
			params.addParams("id", Integer.toString(storyID));
			params.addParams("output", "JSON");
			params.addParams("numResults", "20");
			new GetStoryAsyncTask().execute(params);
		} else if (contentType.equals("topics")) {
			RequestParams params = new RequestParams("GET",
					"http://api.npr.org/query?");

			params.addParams("apiKey", "MDE5ODg4ODY4MDE0MzY5ODEzMzUzNGM3Zg001");
			params.addParams("id", Integer.toString(storyID));
			params.addParams("output", "JSON");
			params.addParams("numResults", "20");
			new GetStoryAsyncTask().execute(params);
		} else if (contentType.equals("favorites")) {
			if (favs.size() == 0) {
				findViewById(R.id.storiesListView).setVisibility(
						ListView.INVISIBLE);
				findViewById(R.id.noFavText).setVisibility(TextView.VISIBLE);
			} else {

				loadFavorites();
			}
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
		if (sharedPreferences.contains(FAVORITES)) {
			String jsonTasks = sharedPreferences.getString(FAVORITES, null);
			Gson gson = new Gson();
			Story[] st = gson.fromJson(jsonTasks, Story[].class);

			for (Story t : st) {
				favs.add(t);
			}
		}
	}

	public void saveToSharedPreferences() {
		Editor editor;
		sharedPreferences = StoriesActivity.this.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String jsonTasks = gson.toJson(favs);

		editor.putString(FAVORITES, jsonTasks);

		editor.commit();
	}

	public void loadFavorites() {
		adapterFav = new StoryAdapter(StoriesActivity.this,
				R.layout.row_stories_item, favs);
		adapterFav.setNotifyOnChange(true);
		listView.setAdapter(adapterFav);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(StoriesActivity.this,
						StoryActivity.class);
				intent.putExtra(STORY_KEY, favs.get(position));
				startActivityForResult(intent, STORIES_ACTIVITY_ID);

			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == STORIES_ACTIVITY_ID) {
			if (resultCode == RESULT_OK) {
				Story newStory = (Story) data.getExtras().getSerializable(
						STORY_KEY);

				if (!contentType.equals("favorites")) {
					for (Story s : stories) {
						if (newStory.equals(s)) {
							if (newStory.isFav != s.isFav) {
								s.isFav = newStory.isFav;
								if (newStory.isFav) {

									favs.add(s);
								} else {
									favs.remove(s);
								}
							}
						}
					}
				} else if (contentType.equals("favorites")) {
					for (Story s : favs) {
						if (newStory.equals(s)) {
							if (newStory.isFav != s.isFav) {

								favs.remove(s);
								adapterFav.notifyDataSetChanged();
								if (favs.size() == 0) {
									findViewById(R.id.noFavText).setVisibility(
											TextView.VISIBLE);
								}
								break;

							}
						}
					}
				}
			}

		}

	}

	public class GetStoryAsyncTask extends
			AsyncTask<RequestParams, Void, ArrayList<Story>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(StoriesActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Stories. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected ArrayList<Story> doInBackground(RequestParams... params) {
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

					return ContentUtil.ContentJSONParser.parseStories(sb
							.toString());
				}

			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Story> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			stories = result;

			StoryAdapter adapter = new StoryAdapter(StoriesActivity.this,
					R.layout.row_stories_item, stories);
			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);
			progressDialog.dismiss();

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					for (Story s : favs) {
						if (stories.get(position).equals(s)) {
							stories.get(position).setFav(true);
						}
					}
					Intent intent = new Intent(StoriesActivity.this,
							StoryActivity.class);
					intent.putExtra(STORY_KEY, stories.get(position));
					startActivityForResult(intent, STORIES_ACTIVITY_ID);

				}

			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		if (contentType.equals("favorites")) {
			getMenuInflater().inflate(R.menu.stories, menu);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_clear) {
			if (favs.size() > 0) {

				favs.removeAll(favs);
				adapterFav.notifyDataSetChanged();
			}
			if (favs.size() == 0) {
				findViewById(R.id.noFavText).setVisibility(TextView.VISIBLE);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
