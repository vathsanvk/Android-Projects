package com.example.flickrbrowser;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends BaseActivity {

	private static final String LOG_TAG = "MainActivity";
	private List<Photo> photosList = new ArrayList<Photo>();
	private RecyclerView recyclerView;
	private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activateToolbar();
		recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		ProcessPhotos processPhotos = new ProcessPhotos("android,lollipop", true);
		processPhotos.execute();
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class ProcessPhotos extends GetFlickrJsonData {
		public ProcessPhotos(String searchCriteria, boolean matchAll) {
			super(searchCriteria, matchAll);
		}

		public void execute() {
			super.execute();
			ProcessData processData = new ProcessData();
			processData.execute();
		}

		public class ProcessData extends DownloadJsonData {
			protected void onPostExecute(String webData) {
				super.onPostExecute(webData);
				flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(
						MainActivity.this, getPhotos());
				recyclerView.setAdapter(flickrRecyclerViewAdapter);
			}
		}

	}
}
