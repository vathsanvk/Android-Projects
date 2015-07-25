package com.example.todonotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends Activity {

	RelativeLayout relativeLayout;
	EditText editPassword, editEmail;
	Button btnLogin, btnSignUp;
	boolean resultLogin;
	public final static String USER_KEY = "user_key";
	public final static int SIGNUP_KEY = 101;
	public final static int TODO_KEY = 102;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);

		try {
			Parse.initialize(this, "oRh596TTlnphjddqUWkVPIidBuozdP8ij125a6rr",
					"qJJQVncUE6WYf03IagEfSHZInkqCCGVRyGOEQenC");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		checkUser();

	}

	public void checkUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {
			Intent intent = new Intent(Login.this, ToDoList.class);
			intent.putExtra(USER_KEY, currentUser.getEmail());
			startActivity(intent);
			finish();

		} else {
			// show the signup or login screen
			// Parse.enableLocalDatastore(this);

			editEmail = (EditText) findViewById(R.id.editEmail);
			editPassword = (EditText) findViewById(R.id.editPassword);

			btnLogin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (editEmail.getText().toString().length() != 0
							&& editPassword.getText().toString().length() != 0) {

						authenticateUser();

					} else {
						Toast.makeText(Login.this,
								"Please complete the Login details",
								Toast.LENGTH_LONG).show();
					}

				}
			});

			btnSignUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Login.this, SignUp.class);
					startActivity(intent);

				}
			});

		}
	}

	public void authenticateUser() {

		ParseUser.logInInBackground(editEmail.getText().toString(),
				editPassword.getText().toString(), new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							// Hooray! The user is logged in.
							Intent intent = new Intent(Login.this,
									ToDoList.class);
							intent.putExtra(USER_KEY, editEmail.getText()
									.toString());
							startActivity(intent);
							finish();
						} else {
							// Signup failed. Look at the ParseException to see
							// what happened.
							e.printStackTrace();
							Toast.makeText(Login.this, "Invalid details",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.exit(0);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
