package com.example.todonotes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ToDoList extends Activity {

	String user;

	List<String> notesFromParse;
	ArrayList<String> notes;
	ListView listView;
	ArrayAdapter<String> adapter;
	EditText dialogText;
	String objectId;
	public final static int RESULT_CLOSE_ALL = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_list);

		listView = (ListView) findViewById(R.id.listView);

		notes = new ArrayList<String>();

		if (getIntent().getExtras().getString(SignUp.USER_KEY) != null) {
			user = getIntent().getExtras().getString(SignUp.USER_KEY);
		} else if (getIntent().getExtras().getString(Login.USER_KEY) != null) {
			user = getIntent().getExtras().getString(Login.USER_KEY);
		}

		if (user.length() != 0) {

			getDataFromParse();

			adapter = new ArrayAdapter<>(ToDoList.this,
					android.R.layout.simple_list_item_1, notes);

			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);
		}

	}

	public void getDataFromParse() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
		query.whereEqualTo("Email", user);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Toast.makeText(ToDoList.this, "No data", Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				} else {
					notesFromParse = object.getList("Notes");
					objectId = object.getObjectId();
					if (notesFromParse != null && notesFromParse.size() != 0) {
						for (String s : notesFromParse) {
							notes.add(s);
							adapter.notifyDataSetChanged();
						}
					}

				}
			}
		});

	}

	public void addItemDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View layout = View.inflate(this, R.layout.custom_dialog, null);

		dialogText = ((EditText) layout.findViewById(R.id.dialogText));
		builder.setView(layout);

		builder.setTitle("Add an item")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (dialogText != null
								&& dialogText.getText().toString().length() != 0) {

							updateItemList();

						} else {
							Toast.makeText(ToDoList.this,
									"Please complete the text",
									Toast.LENGTH_LONG).show();
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});

		AlertDialog alert = builder.create();

		alert.show();
	}

	public void updateItemList() {
		notes.add(dialogText.getText().toString());
		adapter.notifyDataSetChanged();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");

		// Retrieve the object by id
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject obj, ParseException e) {
				if (e == null) {

					obj.add("Notes", dialogText.getText().toString());
					obj.saveInBackground();
				} else {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addItem) {
			addItemDialog();
			return true;
		} else if (id == R.id.Logout) {
			ParseUser.logOut();
			ParseUser currentUser = ParseUser.getCurrentUser();
			Intent intent = new Intent(ToDoList.this, Login.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
