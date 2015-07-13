package com.example.studentregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends Activity {

	Student student;
	Student student1;
	final static String STUDENT_KEY = "student";
	final static String INFO_KEY = "info";
	final static int REQ_CODE = 101;
	TextView dispNameValue;
	TextView dispMailValue;
	TextView dispLangValue;
	TextView dispStateValue;
	TextView dispMoodValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		dispNameValue = (TextView) findViewById(R.id.dispNameValue);
		dispMailValue = (TextView) findViewById(R.id.dispMailValue);
		dispLangValue = (TextView) findViewById(R.id.dispLangValue);
		dispStateValue = (TextView) findViewById(R.id.dispStateValue);
		dispMoodValue = (TextView) findViewById(R.id.dispMoodValue);

		if (getIntent().getExtras() != null) {
			student = (Student) getIntent().getExtras().getSerializable(
					MainActivity.STUDENT_KEY);
			dispNameValue.setText(student.getName());
			dispMailValue.setText(student.getEmail());
			dispLangValue.setText(student.getProLang());
			dispStateValue.setText(student.getAcctState());
			dispMoodValue.setText(Integer.toString(student.getMood())
					+ "% Positive");
		}

		ImageView iView = (ImageView) findViewById(R.id.editName);
		iView.setOnClickListener(new ImageViewClass("name"));

		iView = (ImageView) findViewById(R.id.editMail);
		iView.setOnClickListener(new ImageViewClass("email"));

		iView = (ImageView) findViewById(R.id.editLang);
		iView.setOnClickListener(new ImageViewClass("lang"));

		iView = (ImageView) findViewById(R.id.editState);
		iView.setOnClickListener(new ImageViewClass("state"));

		iView = (ImageView) findViewById(R.id.editMood);
		iView.setOnClickListener(new ImageViewClass("mood"));

	}

	public class ImageViewClass implements View.OnClickListener {

		String info;

		public ImageViewClass(String info) {
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(
					"com.example.studentregistration.intent.action.VIEW");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.putExtra(INFO_KEY, this.info);
			if (student1 != null && !student1.equals(student)) {
				intent.putExtra(STUDENT_KEY, student1);
			} else {
				intent.putExtra(STUDENT_KEY, student);
			}
			startActivityForResult(intent, REQ_CODE);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub
		if (requestCode == REQ_CODE) {
			if (resultCode == RESULT_OK) {
				student1 = (Student) data.getExtras().getSerializable(
						STUDENT_KEY);
			
				dispNameValue.setText(student1.getName());
				dispMailValue.setText(student1.getEmail());
				dispLangValue.setText(student1.getProLang());
				dispStateValue.setText(student1.getAcctState());
				dispMoodValue.setText(Integer.toString(student1.getMood())
						+ "% Positive");

			} else {

				dispNameValue.setText(student.getName());
				dispMailValue.setText(student.getEmail());
				dispLangValue.setText(student.getProLang());
				dispStateValue.setText(student.getAcctState());
				dispMoodValue.setText(Integer.toString(student.getMood())
						+ "% Positive");

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
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
