package com.example.nprclient;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	public final static String CONTENT_KEY = "content_key";

	public final static String FAVORITES_KEY = "favorites_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnExit = (Button) findViewById(R.id.btnExit);
		

		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				System.exit(0);
			}
		});

		findViewById(R.id.btnPrograms).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						try {
							Intent intent = new Intent(MainActivity.this,
									com.example.nprclient.ListActivity.class);
							intent.putExtra(CONTENT_KEY, "programs");
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
					}
				});

		findViewById(R.id.btnTopics).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,
								com.example.nprclient.ListActivity.class);
						intent.putExtra(CONTENT_KEY, "topics");
						startActivity(intent);
					}
				});

		findViewById(R.id.btnFavorites).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(MainActivity.this,
								StoriesActivity.class);
						intent.putExtra(CONTENT_KEY, "favorites");
						startActivity(intent);

					}
				});

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
