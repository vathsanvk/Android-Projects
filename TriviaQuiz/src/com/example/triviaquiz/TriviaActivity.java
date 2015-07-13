package com.example.triviaquiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TriviaActivity extends Activity {

	ArrayList<String> completeQuestionText;
	public static String RESULT_VALUE = "result";
	TextView questNum, questTime, questText;
	int qNo = 1;
	ArrayList<Question> questions;
	RelativeLayout relativeLayout;
	RadioGroup radioGroup;
	int quesIndex;
	int correctAnswers;
	int correctAnsPct;
	Button btnNext;
	ProgressDialog progressDialog;
	ProgressBar progress;
	ImageView iv;
	boolean nextClicked;
	CountDownTimer countDowntimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trivia);

		questNum = (TextView) findViewById(R.id.questNum);
		questNum.setText("Q" + qNo);
		questTime = (TextView) findViewById(R.id.questTime);

		questText = (TextView) findViewById(R.id.quesText);
		quesIndex = 0;
		progress = (ProgressBar) findViewById(R.id.progress);

		btnNext = (Button) findViewById(R.id.btnNext);

		new GetQuestionsAsyncTask()
				.execute("http://dev.theappsdr.com/apis/trivia/getAll.php");

		findViewById(R.id.btnNext).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cancelTimer();
						updateQuestion();

					}
				});

		findViewById(R.id.btnQuit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cancelTimer();
						// Intent intent = new Intent(TriviaActivity.this,
						// MainActivity.class);

						// startActivity(intent);
						finish();

					}
				});

		/*
		 * relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		 * RelativeLayout.LayoutParams rule = new RelativeLayout.LayoutParams(
		 * ViewGroup.LayoutParams.WRAP_CONTENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT);
		 * 
		 * radioGroup = new RadioGroup(this); rule.addRule(RelativeLayout.BELOW,
		 * R.id.quesText); rule.setMargins(0, 10, 0, 0);
		 * radioGroup.setOrientation(RadioGroup.VERTICAL);
		 * 
		 * 
		 * 
		 * radioGroup.setLayoutParams(rule);
		 * 
		 * relativeLayout.addView(radioGroup);
		 */

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		cancelTimer();
	}

	public void updateQuestion() {
		validateAnswer();
		if (quesIndex == questions.size() - 1) {

			Intent intent = new Intent(TriviaActivity.this,
					ResultActivity.class);

			correctAnsPct = ((correctAnswers * 100) / questions.size());
			intent.putExtra(RESULT_VALUE, correctAnsPct);
			startActivity(intent);
			finish();
		} else {

			radioGroup.removeAllViews();
			radioGroup.clearCheck();
			qNo++;
			questNum.setText("Q" + qNo);

			quesIndex++;
			setupQuestion();
			new DownloadPhoto().execute(questions.get(quesIndex).getImageURL());
			setTimer();

		}
	}

	public void setTimer() {

		countDowntimer = new CountDownTimer(24000, 1000) {

			public void onTick(long millisUntilFinished) {
				questTime.setText("Time Left: " + millisUntilFinished / 1000);

			}

			public void onFinish() {
				updateQuestion();

			}
		}.start();

	}

	public void cancelTimer() {
		countDowntimer.cancel();
	}

	public class DownloadPhoto extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			URL url;
			try {
				if (!params[0].isEmpty()) {
					url = new URL(params[0]);
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setRequestMethod("GET");
					Bitmap image = BitmapFactory.decodeStream(con
							.getInputStream());

					return image;
				}

			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progress.setVisibility(ProgressBar.VISIBLE);

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			iv = (ImageView) findViewById(R.id.questImage);

			String uri = "@drawable/no_image";

			int imageResource = getResources().getIdentifier(uri, null,
					getPackageName());

			Drawable res = getResources().getDrawable(imageResource);
			iv.setImageDrawable(res);
			if (result != null) {

				iv.setImageBitmap(result);
				// iv.setScaleType(ScaleType.FIT_XY);
				// progressDialog.dismiss();
			}
			progress.setVisibility(ProgressBar.INVISIBLE);
			iv.setVisibility(ImageView.VISIBLE);

		}
	}

	public void validateAnswer() {
		RadioButton radioButton;

		if (radioGroup.getCheckedRadioButtonId() != -1) {
			radioButton = (RadioButton) findViewById(radioGroup
					.getCheckedRadioButtonId());

			int ans = radioButton.getId();
			Log.d("TAG_ANS", Integer.toString(ans));

			if (ans == (questions.get(quesIndex).correctAnswerIndex)) {
				correctAnswers++;

			}

		}

	}

	public void setupQuestion() {
		int i = 0;
		if (quesIndex < questions.size()) {
			questText.setText(questions.get(quesIndex).getQuestion());

			for (String s : questions.get(quesIndex).getAnswerOptions()) {

				RadioButton radioButton = new RadioButton(TriviaActivity.this);
				radioButton.setText(s);
				radioButton.setId(i);
				radioGroup.addView(radioButton);
				i++;
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trivia, menu);
		return true;
	}

	public class GetQuestionsAsyncTask extends
			AsyncTask<String, Void, ArrayList<Question>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(TriviaActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setMessage("Loading Questions. .");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected void onPostExecute(ArrayList<Question> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			questions = new ArrayList<Question>();
			for (Question q : result) {
				questions.add(q);

			}
			setupQuestion();
			progressDialog.dismiss();
			new DownloadPhoto().execute(questions.get(quesIndex).getImageURL());

			setTimer();
			/*
			 * questText.setText(questions.get(quesIndex).getQuestion()); int i
			 * = 0; for (String s : questions.get(quesIndex).getAnswerOptions())
			 * { Log.d("TAG", s); RadioButton radioButton = new
			 * RadioButton(TriviaActivity.this); radioButton.setText(s);
			 * radioButton.setId(i); radioGroup.addView(radioButton); i++; }
			 */
		}

		@Override
		protected ArrayList<Question> doInBackground(String... params) {
			// TODO Auto-generated method stub
			URL url;
			BufferedReader reader = null;
			try {
				url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestMethod("GET");
				reader = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				completeQuestionText = new ArrayList<String>();
				while ((line = reader.readLine()) != null) {
					// sb.append(line + "/n");
					completeQuestionText.add(line);

				}
				return QuestionUtil.QuestionParser
						.parseQuestion(completeQuestionText);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

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
