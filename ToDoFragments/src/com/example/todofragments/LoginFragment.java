package com.example.todofragments;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

	EditText editEmail, editPassword;
	public onLoginFragmentChangeListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (onLoginFragmentChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "should implement OnLoginFragmentChangeListener");
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		editEmail = (EditText) getActivity().findViewById(R.id.editEmail);
		editPassword = (EditText) getActivity().findViewById(R.id.editPassword);

		getActivity().findViewById(R.id.btnLogin).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (editEmail.getText().toString().length() != 0
								&& editPassword.getText().toString().length() != 0) {

							authenticateUser();

						} else {
							Toast.makeText(getActivity(),
									"Please complete the Login details",
									Toast.LENGTH_LONG).show();
						}

					}
				});

		getActivity().findViewById(R.id.btnSignUp).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mListener.gotoSignUp();

					}
				});
	}

	public void authenticateUser() {

		ParseUser.logInInBackground(editEmail.getText().toString(),
				editPassword.getText().toString(), new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							// Hooray! The user is logged in.

							mListener.gotoToDo(editEmail.getText().toString(), false,-1);
						} else {
							// Signup failed. Look at the ParseException to see
							// what happened.
							e.printStackTrace();
							Toast.makeText(getActivity(), "Invalid details",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}

	public interface onLoginFragmentChangeListener {
		void gotoSignUp();

		void gotoToDo(String email, boolean delete,int pos);
	}

}
