package com.example.topiosapps;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PreviewActivity extends Activity {

	App app;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		app = (App) getIntent().getExtras().getSerializable(
				AppsActivity.APPS_KEY);

		TextView title = (TextView) findViewById(R.id.textViewTitle);
		title.setText(app.getTitle());
        progress = (ProgressBar) findViewById(R.id.progress);
		ImageView largeImage = (ImageView) findViewById(R.id.largeImage);
		progress.setVisibility(ProgressBar.VISIBLE);
		Picasso.with(PreviewActivity.this).load(app.getLargePhotoUrl())
				.into(largeImage);
		progress.setVisibility(ProgressBar.INVISIBLE);
		
		
		largeImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri
						.parse(app.getUrl()));
			    startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview, menu);
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
