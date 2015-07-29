package com.example.todofragments;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends Activity implements
		LoginFragment.onLoginFragmentChangeListener,
		SignUpFragment.onSignUpFragmentChangeListener,
		ToDoFragment.onToDoFragmentChangeListener,
		ItemDetailsFragment.onItemDetailsFragmentChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			Parse.initialize(this, "oRh596TTlnphjddqUWkVPIidBuozdP8ij125a6rr",
					"qJJQVncUE6WYf03IagEfSHZInkqCCGVRyGOEQenC");
		} catch (Exception e) {

		}

		getFragmentManager().beginTransaction()
				.add(R.id.container, new SplashFragment(), "splash").commit();

		new CountDownTimer(5000, 1000) {

			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {

				ParseUser currentUser = ParseUser.getCurrentUser();

				if (currentUser != null) {
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.container, new ToDoFragment(currentUser.getEmail(),false,-1),
							"todo").commit();

				} else {

					getFragmentManager()
							.beginTransaction()
							.replace(R.id.container, new LoginFragment(),
									"login").commit();
				}

			}
		}.start();
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
		ToDoFragment f = (ToDoFragment) getFragmentManager().findFragmentByTag(
				"todo");

		if (f != null && f.isVisible()) {
			if (id == R.id.addItem) {

				f.addItemDialog();
				return true;
			} else if (id == R.id.logout) {
				ParseUser.logOut();
				ParseUser currentUser = ParseUser.getCurrentUser();

				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void gotoLogin() {
		getFragmentManager().beginTransaction()
				.replace(R.id.container, new LoginFragment(), "login").commit();

	}

	@Override
	public void gotoSignUp() {
		// TODO Auto-generated method stub
		getFragmentManager().beginTransaction()
				.replace(R.id.container, new SignUpFragment(), "signup")
				.commit();

	}

	@Override
	public void gotoToDo(String email, boolean delete, int pos) {
		// TODO Auto-generated method stub
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.container, new ToDoFragment(email, delete, pos),
						"todo").commit();

	}

	@Override
	public void gotoItemDetails(String email, String notes, int pos,
			List<String> notesList, String objId) {
		// TODO Auto-generated method stub
		getFragmentManager()
				.beginTransaction()
				.replace(
						R.id.container,
						new ItemDetailsFragment(email, notes, pos, notesList,
								objId), "itemdetails").addToBackStack(null)
				.commit();

	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}
	}

}
