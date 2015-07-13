package com.example.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends Activity {

	TextView dispNameValue, dispDateValue, dispPriorityValue, dispTimeValue;
	Task task, newTask;
	int index;
	final static String EDIT_TASK = "edit_task";

	final static int REQ_EDIT_TASK = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
        index = getIntent().getExtras().getInt(MainActivity.DISPLAY_TASK_INDEX);
		task = (Task) getIntent().getExtras().getSerializable(
				MainActivity.DISPLAY_TASK);
		
	

		dispNameValue = (TextView) findViewById(R.id.dispNameValue);
		dispDateValue = (TextView) findViewById(R.id.dispDateValue);
		dispTimeValue = (TextView) findViewById(R.id.dispTimeValue);
		dispPriorityValue = (TextView) findViewById(R.id.dispPriorityValue);

		dispNameValue.setText(task.getTaskName());
		dispDateValue.setText(task.getDate());
		dispTimeValue.setText(task.getTime());
		dispPriorityValue.setText(task.getPriority());

		findViewById(R.id.editImage).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(DisplayActivity.this,
								EditTask.class);

						intent.putExtra(EDIT_TASK, task);
						startActivityForResult(intent, REQ_EDIT_TASK);
					}
				});
		
		
		findViewById(R.id.deleteImage).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			     Intent intent = new Intent();
			    
			     intent.putExtra(MainActivity.DISPLAY_TASK_ACTION, "delete");
			     intent.putExtra(MainActivity.DISPLAY_TASK_INDEX, index);
			     setResult(RESULT_OK,intent);
			     finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		   //super.onBackPressed();

		
			Intent intent = new Intent();
			
			intent.putExtra(MainActivity.DISPLAY_TASK_ACTION, "edit");
			intent.putExtra(MainActivity.DISPLAY_TASK_INDEX, index);
			intent.putExtra(MainActivity.DISPLAY_TASK, task);
			setResult(RESULT_OK, intent);
			finish();
		 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQ_EDIT_TASK) {
			if (resultCode == RESULT_OK) {
				newTask = (Task) data.getExtras().getSerializable(EDIT_TASK);
				
				
				task = newTask;

				// if(!task.equals(newTask)){

				// }
				dispNameValue.setText(task.getTaskName());
				dispDateValue.setText(task.getDate());
				dispTimeValue.setText(task.getTime());
				dispPriorityValue.setText(task.getPriority());

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
