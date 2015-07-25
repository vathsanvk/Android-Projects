package com.example.todonotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends Activity {

	EditText signName, signEmail, signPassword, signConfirmPassword;
	Button btnSignUp, btnCancel;
	boolean validUser;
	boolean resultSignUp;
	public final static String USER_KEY = "user_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		signName = (EditText) findViewById(R.id.signName);
		signEmail = (EditText) findViewById(R.id.signEmail);
		signPassword = (EditText) findViewById(R.id.signPassword);
		signConfirmPassword = (EditText) findViewById(R.id.signConfirmPassword);

		findViewById(R.id.btnSignUp).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						validateUserDetails();
						if (validUser) {
							createUser();

						}
					}
				});

		findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

	}

	public void validateUserDetails() {
		validUser = false;
		if (signEmail.getText().toString().length() != 0
				&& signName.getText().toString().length() != 0
				&& signPassword.getText().toString().length() != 0
				&& signConfirmPassword.getText().toString().length() != 0) {
			validUser = true;

			if (signPassword.getText().toString()
					.equals(signConfirmPassword.getText().toString())) {
				validUser = true;
			} else {
				validUser = false;
				Toast.makeText(SignUp.this, "Passwords do not match",
						Toast.LENGTH_LONG).show();
			}

			if (isValidEmail(signEmail.getText().toString())) {
				validUser = true;
			} else {
				validUser = false;
				Toast.makeText(SignUp.this, "Invalid email format",
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(SignUp.this, "Please complete all the details",
					Toast.LENGTH_LONG).show();
		}

	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}

	public boolean createUser() {
		ParseUser user = new ParseUser();
		user.setUsername(signEmail.getText().toString());
		user.setPassword(signPassword.getText().toString());
		user.setEmail(signEmail.getText().toString());

		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					insertDataIntoToDoClass();
					Intent intent = new Intent(SignUp.this, ToDoList.class);
					intent.putExtra(USER_KEY, signEmail.getText().toString());
					startActivity(intent);
					finish();
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
					e.printStackTrace();
					Toast.makeText(
							SignUp.this,
							"Email already exists. Please select a different Email",
							Toast.LENGTH_LONG).show();
				}

			}

		});

		return resultSignUp;
	}

	public void insertDataIntoToDoClass() {
		ParseObject toDo = new ParseObject("ToDo");
		toDo.put("Email", signEmail.getText().toString());
		toDo.saveInBackground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
