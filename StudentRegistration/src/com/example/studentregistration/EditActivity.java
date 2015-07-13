package com.example.studentregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

	EditText editName;
	EditText editMail;
	RadioGroup editRadio;
	TextView textViewLang;
	TextView textViewMood;
	Switch editAcct;
	SeekBar editMood;
	boolean allGood = true;
	String acct = "Unsearchable";
	String name;
	String email;
	int mood = 0;
	String lang;
	String info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		editName = (EditText) findViewById(R.id.editName);
		editMail = (EditText) findViewById(R.id.editMail);
		editRadio = (RadioGroup) findViewById(R.id.radioGroup1);
		editAcct = (Switch) findViewById(R.id.switch1);
		editMood = (SeekBar) findViewById(R.id.seekBar1);
		textViewLang = (TextView) findViewById(R.id.textViewLang);
		textViewMood = (TextView) findViewById(R.id.textViewMood);
		editName.setVisibility(View.INVISIBLE);
		editMood.setVisibility(View.INVISIBLE);
		editMail.setVisibility(View.INVISIBLE);
		editRadio.setVisibility(View.INVISIBLE);
		editAcct.setVisibility(View.INVISIBLE);
		textViewLang.setVisibility(View.INVISIBLE);
		textViewMood.setVisibility(View.INVISIBLE);

		 info = getIntent().getExtras().getString(
				DisplayActivity.INFO_KEY);
		Student student = (Student) getIntent().getExtras().getSerializable(
				DisplayActivity.STUDENT_KEY);
		name = student.getName();
		email = student.getEmail();
		lang = student.getProLang();
		acct = student.getAcctState();
		mood = student.getMood();

		switch (info) {
		case "name":
			editName.setText(student.getName());
			editName.setVisibility(View.VISIBLE);

			break;
		case "email":
			editMail.setText(student.getEmail());
			editMail.setVisibility(View.VISIBLE);

			break;
		case "lang":
			String radButton = student.getProLang();
			int radioButtonId;
			switch (radButton) {
			case "Java":
				radioButtonId = R.id.radio0;
				break;
			case "Python":
				radioButtonId = R.id.radio1;
				break;
			case "C#":
				radioButtonId = R.id.radio2;
				break;
			default:
				radioButtonId = R.id.radio0;
				break;

			}
			editRadio.check(radioButtonId);
			textViewLang.setVisibility(View.VISIBLE);
			editRadio.setVisibility(View.VISIBLE);
			
			break;
		case "state":
			if (student.getAcctState().equals("Searchable")) {
				editAcct.setTextOn("ON");
				editAcct.setChecked(true);
			} else {
				editAcct.setTextOff("OFF");
				editAcct.setChecked(false);
			}

			editAcct.setVisibility(View.VISIBLE);
			editAcct.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub

					if (isChecked) {
						acct = "Searchable";

					} else {
						acct = "Unsearchable";

					}

				}
			});
			break;
		case "mood":
			editMood.setProgress(student.getMood());
			textViewMood.setVisibility(View.VISIBLE);
			editMood.setVisibility(View.VISIBLE);
			editMood.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub

					mood = progress;

				}
			});
			break;
		default:
			Log.d("TAG", "No info passed");
			break;
		}

		findViewById(R.id.saveButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						switch(info){
						case "name":
							name = editName.getText().toString();
							break;
						case "email":
							email = editMail.getText().toString();
							break;
						case "lang":

							RadioButton radioButton = (RadioButton) findViewById(editRadio
									.getCheckedRadioButtonId());

							lang = radioButton.getText().toString();
							break;
						}
						
						

		
						Student student1 = new Student(name, email, lang, acct,
								mood);
						Intent intent = new Intent();
						intent.putExtra(DisplayActivity.STUDENT_KEY, student1);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
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
