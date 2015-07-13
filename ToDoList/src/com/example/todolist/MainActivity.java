package com.example.todolist;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends Activity {

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Editor editor;
		sharedPreferences = MainActivity.this.getSharedPreferences("User", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Gson gson = new Gson();
        String jsonTasks = gson.toJson(tasks);
 
        editor.putString(TASKS, jsonTasks);
 
        editor.commit();
	}

	protected static final int REQ_CODE_CREATE_TASK = 101;
	protected static final int REQ_CODE_DISPLAY_TASK = 102;

	final static String NEW_TASK = "new_task";
	final static String DISPLAY_TASK = "display_task";
	final static String DISPLAY_TASK_INDEX = "display_task_index";
	final static String DISPLAY_TASK_ACTION = "display_task_action";
	RelativeLayout relativeLayout;
	LinearLayout linearLayout;
	TextView textView, taskCount;
	LinkedList<Task> tasks;
	Task newTask;
	Task oldTask;
	SharedPreferences sharedPreferences;
	final static String TASKS ="tasks";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tasks = new LinkedList<Task>();

		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		taskCount = (TextView) findViewById(R.id.taskCount);
		

		RelativeLayout.LayoutParams rule = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		linearLayout = new LinearLayout(this);
		rule.addRule(RelativeLayout.BELOW, R.id.taskCount);
		rule.setMargins(0, 40, 0, 0);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		linearLayout.setLayoutParams(rule);

		relativeLayout.addView(linearLayout);

		
        loadSavedPreferences();
        taskCount.setText(tasks.size() + " ");
		updateTask();

		findViewById(R.id.addTask).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,
								CreateTask.class);
						startActivityForResult(intent, REQ_CODE_CREATE_TASK);

					}
				});

	}
	
	public void loadSavedPreferences(){
		sharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE);
		if(sharedPreferences.contains(TASKS)){
		   String jsonTasks = sharedPreferences.getString(TASKS, null);
		   Gson gson = new Gson();
		   Task[] task = gson.fromJson(jsonTasks,Task[].class);
		   tasks=new LinkedList<Task>();
		   for(Task t : task){
			   tasks.add(t);
		   }
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQ_CODE_CREATE_TASK) {
			if (resultCode == RESULT_OK) {
				newTask = (Task) data.getExtras().getSerializable(NEW_TASK);
				tasks.add(newTask);
				taskCount.setText(tasks.size() + " ");
				displayTask();

			}
		}

		if (requestCode == REQ_CODE_DISPLAY_TASK) {
			if (resultCode == RESULT_OK) {

				String action = data.getExtras().getString(DISPLAY_TASK_ACTION);
				int indx = data.getExtras().getInt(DISPLAY_TASK_INDEX);
				if (action.equals("edit")) {
					Task changedTask = (Task) data.getExtras().getSerializable(
							DISPLAY_TASK);

					Task sentTask = tasks.get(indx);
					// Toast.makeText(MainActivity.this,
					// "old" + sentTask.toString(), Toast.LENGTH_LONG)
					// .show();

					if (!sentTask.equals(changedTask)) {
						sentTask = changedTask;
						tasks.remove(indx);
						tasks.add(indx, changedTask);
						// Toast.makeText(MainActivity.this,
						// "updated" + tasks.get(indx).toString(),
						// Toast.LENGTH_LONG).show();
						updateTask();
					}

				} else if (action.equals("delete")) {
					
					tasks.remove(indx);
					taskCount.setText(tasks.size() + " ");
					updateTask();
				}

			}
		}
	}

	
	public void updateTask() {
		linearLayout.removeAllViews();
		if (tasks.size() != 0) {

			for (Task t : tasks) {

				LinearLayout linearLayoutChild = new LinearLayout(this);

				linearLayoutChild.setOrientation(LinearLayout.VERTICAL);

				linearLayout.addView(linearLayoutChild);
				textView = new TextView(this);

				textView.setPadding(0, 30, 0, 0);
				TextView textViewDate = new TextView(this);
				TextView textViewTime = new TextView(this);

				textView.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

				textViewDate.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

				textViewTime.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

				textView.setText(t.getTaskName());
				textViewDate.setText(t.getDate());
				textViewTime.setText(t.getTime());
				textView.setOnClickListener(new TaskItemListener(t));
				textView.setTag(tasks.indexOf(t));

				textView.setTextAppearance(this,
						android.R.style.TextAppearance_Large);
				linearLayoutChild.addView(textView);
				linearLayoutChild.addView(textViewDate);
				linearLayoutChild.addView(textViewTime);

			}

		}
	}

	public void displayTask() {

		// for (Task task : tasks) {

		if (tasks.size() != 0) {
			LinearLayout linearLayoutChild = new LinearLayout(this);

			linearLayoutChild.setOrientation(LinearLayout.VERTICAL);

			linearLayout.addView(linearLayoutChild);
			textView = new TextView(this);
			textView.setPadding(0, 30, 0, 0);
			TextView textViewDate = new TextView(this);
			TextView textViewTime = new TextView(this);

			textView.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));

			textViewDate.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));

			textViewTime.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			textView.setTag(tasks.indexOf(tasks.getLast()));
			textView.setText(tasks.getLast().getTaskName());
			textViewDate.setText(tasks.getLast().getDate());
			textViewTime.setText(tasks.getLast().getTime());
			textView.setOnClickListener(new TaskItemListener(newTask));

			textView.setTextAppearance(this,
					android.R.style.TextAppearance_Large);
			linearLayoutChild.addView(textView);
			linearLayoutChild.addView(textViewDate);
			linearLayoutChild.addView(textViewTime);
		}
		// }
	}

	public class TaskItemListener implements View.OnClickListener {

		Task myTask;
		int index;

		public TaskItemListener(Task myTask) {

			this.myTask = myTask;

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
			index = (int) v.getTag();
			intent.putExtra(DISPLAY_TASK_INDEX, index);
			intent.putExtra(DISPLAY_TASK, myTask);
			startActivityForResult(intent, REQ_CODE_DISPLAY_TASK);

		}

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
