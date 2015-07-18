package com.example.nprclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoryActivity extends Activity {

	TextView sTitle, sReporter, SDateAired, sTeaserText;
	Story story;
	ImageView favIcon;
	int mins, secs;
	public static RelativeLayout storyView;

	MediaPlayer mediaPlayer;
	MediaController mediaController;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		storyView = (RelativeLayout) findViewById(R.id.storyView);

		story = (Story) getIntent().getExtras().getSerializable(
				StoriesActivity.STORY_KEY);

		sTitle = (TextView) findViewById(R.id.sTitle);
		sReporter = (TextView) findViewById(R.id.sReporter);
		SDateAired = (TextView) findViewById(R.id.sDateAired);
		sTeaserText = (TextView) findViewById(R.id.sTeaserText);
		favIcon = (ImageView) findViewById(R.id.favIcon);

		mins = story.lenAudio / 60;
		secs = (story.lenAudio % 60);
		// String lenAudio = String.format(Locale.US,"%02min%02dsec", mins,
		// secs);
		String duration = " | " + mins + " min " + secs + " sec ";
		sTitle.setText(story.getTitle());
		sReporter.setText(story.getRepName());
		if (story.streamUrl == null || story.streamUrl.length() == 0) {
			findViewById(R.id.audio).setVisibility(ImageView.INVISIBLE);
		}
		if (story.lenAudio == 0) {
			duration = "";
			
		}
		SDateAired.setText(story.getPubDate() + duration);
		sTeaserText.setText(story.getTeaserText());

		if (story.isFav) {
			favIcon.setImageResource(R.drawable.fav);
		} else {
			favIcon.setImageResource(R.drawable.not_fav);
		}

		findViewById(R.id.globe).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(android.content.Intent.ACTION_VIEW, Uri
						.parse(story.getLink()));
				startActivity(in);
			}
		});

		findViewById(R.id.nav_back).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mediaController != null) {
							mediaController.hide();
							mediaPlayer.stop();
							mediaPlayer.release();
						}
						Intent intent = new Intent();
						intent.putExtra(StoriesActivity.STORY_KEY, story);
						setResult(RESULT_OK, intent);
						finish();
					}
				});

		findViewById(R.id.audio).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mediaController == null) {
					new GetMediaStream().execute(story.streamUrl);
				}
			}
		});

		/*
		 * findViewById(R.id.favIcon).setOnClickListener( new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub if (!story.isFav) { Log.d("fav", "not fav");
		 * favIcon.setImageResource(R.drawable.fav); story.setFav(true); }
		 * 
		 * } });
		 */

		findViewById(R.id.favIcon).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (!story.isFav) {
							Log.d("fav", "fav");
							favIcon.setImageResource(R.drawable.fav);
							story.setFav(true);

						} else {
							Log.d("fav", "not fav");
							favIcon.setImageResource(R.drawable.not_fav);
							story.setFav(false);
						}
						return false;
					}
				});

	}

	public class PlayAudio implements OnPreparedListener, MediaPlayerControl {

		public PlayAudio(String url) {
			handler = new Handler();

			mediaPlayer = new MediaPlayer();
			mediaController = new MediaController(StoryActivity.this);
			mediaPlayer.setOnPreparedListener(this);

			try {
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepareAsync();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void start() {
			// TODO Auto-generated method stub
			mediaPlayer.start();
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			mediaPlayer.pause();
		}

		@Override
		public int getDuration() {
			// TODO Auto-generated method stub
			return mediaPlayer.getDuration();
		}

		@Override
		public int getCurrentPosition() {
			// TODO Auto-generated method stub
			return mediaPlayer.getCurrentPosition();
		}

		@Override
		public void seekTo(int pos) {
			// TODO Auto-generated method stub
			mediaPlayer.seekTo(pos);
		}

		@Override
		public boolean isPlaying() {
			// TODO Auto-generated method stub
			return mediaPlayer.isPlaying();
		}

		@Override
		public int getBufferPercentage() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean canPause() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean canSeekBackward() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean canSeekForward() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public int getAudioSessionId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mediaPlayer.start();
			mediaController.setMediaPlayer(this);
			mediaController.setAnchorView(StoryActivity.storyView);

			handler.post(new Runnable() {
				public void run() {
					mediaController.setEnabled(true);
					mediaController.show();
				}
			});
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mediaController != null) {
			mediaController.show();
		}
		return false;
	}

	public class GetMediaStream extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
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

					return sb.toString();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			new PlayAudio(result);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.story, menu);
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mediaController != null) {
			mediaController.hide();
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		Intent intent = new Intent();
		intent.putExtra(StoriesActivity.STORY_KEY, story);
		setResult(RESULT_OK, intent);
		finish();
		super.onBackPressed();
	}

}
