package com.example.studentregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText editName;
	EditText editMail;
	RadioGroup editRadio;
	Switch editAcct;
	SeekBar editMood;
	boolean allGood = true;
	String acct = "Unsearchable";
	String name;
	String email;
	int mood = 0;
	String lang;
	static String STUDENT_KEY ="STUDENT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editName = (EditText) findViewById(R.id.editName);
		editMail = (EditText) findViewById(R.id.editMail);
		editRadio = (RadioGroup) findViewById(R.id.radioGroup1);
		editAcct = (Switch) findViewById(R.id.switch1);
		editMood = (SeekBar) findViewById(R.id.seekBar1);

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

		findViewById(R.id.submitButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (editName.getText().length() != 0) {
							name = editName.getText().toString();
							allGood = true;
						} else {
							allGood = false;
							name="";
							Toast.makeText(MainActivity.this,
									"Please enter your name",
									Toast.LENGTH_SHORT).show();
						}

						if (editMail.getText().length() != 0) {
							email = editMail.getText().toString();
							allGood = true;
						} else {
							allGood = false;
							email="";
							Toast.makeText(MainActivity.this,
									"Please enter your mail",
									Toast.LENGTH_SHORT).show();
						}

						// TODO Auto-generated method stub
						if (editRadio.getCheckedRadioButtonId() != -1) {

							RadioButton radioButton = (RadioButton) findViewById(editRadio
									.getCheckedRadioButtonId());

							lang = radioButton.getText().toString();
							allGood = true;

						} else {
							Toast.makeText(MainActivity.this,
									"Please select a programming language",
									Toast.LENGTH_SHORT).show();
							allGood =false;
						}

						

						if (name !=null && email !=null && lang != null && name.length() != 0 && email.length() !=0) {
							Intent intent = new Intent(MainActivity.this,
									DisplayActivity.class);
							Student student = new Student(name, email, lang,
									acct, mood);
							intent.putExtra(STUDENT_KEY, student);
							startActivity(intent);

						}

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
