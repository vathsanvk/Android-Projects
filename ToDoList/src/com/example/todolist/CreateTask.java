package com.example.todolist;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateTask extends FragmentActivity {

	EditText editTitle, editDate, editTime;
	RadioGroup editPriority;
	String taskName, date, time, priority;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);

		editTitle = (EditText) findViewById(R.id.editTitle);
		editDate = (EditText) findViewById(R.id.editDate);
		editTime = (EditText) findViewById(R.id.editTime);
		editPriority = (RadioGroup) findViewById(R.id.editPriority);

		editDate.setKeyListener(null);
		editTime.setKeyListener(null);

		editDate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					DialogFragment newFragment = new DatePickerFragment();
					newFragment.show(getSupportFragmentManager(), "datePicker");
				}

			}
		});

		editTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					DialogFragment newFragment = new TimePickerFragment();
					newFragment.show(getSupportFragmentManager(), "timePicker");
				}
			}
		});

		findViewById(R.id.saveButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (editTitle != null && editDate != null
								&& editTime != null && editPriority != null) {

							if (editTitle.length() != 0
									&& editDate.length() != 0
									&& editTime.length() != 0) {

								taskName = editTitle.getText().toString();
								Log.d("CTAG", taskName);
								time = editTime.getText().toString();
								date = editDate.getText().toString();
								if (editPriority.getCheckedRadioButtonId() != -1) {

									RadioButton radioButton = (RadioButton) findViewById(editPriority
											.getCheckedRadioButtonId());

									priority = radioButton.getText().toString();

								}

								Task newTask = new Task(taskName, time, date,
										priority);
								Intent intent = new Intent();
								intent.putExtra(MainActivity.NEW_TASK, newTask);
								setResult(RESULT_OK, intent);
								finish();
							} else {

								Toast.makeText(CreateTask.this,
										"Please complete all the details",
										Toast.LENGTH_LONG).show();

							}
						}

					}
				});

	}

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			editDate.setText((month + 1) + "/" + day + "/" + year);
		}
	}

	public class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		String amPm;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			final Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);
			if (c.get(Calendar.AM_PM) == Calendar.AM) {
				amPm = "AM";
			} else {
				amPm = "PM";
			}
			// Do something with the time chosen by the user

			if (DateFormat.is24HourFormat(getActivity())) {
				editTime.setText(hourOfDay + " : " + minute);
			} else {

				editTime.setText(hourOfDay + " : " + minute + "  " + amPm);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_task, menu);
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
