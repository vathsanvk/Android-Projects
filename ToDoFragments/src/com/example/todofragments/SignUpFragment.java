package com.example.todofragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpFragment extends Fragment {

	public onSignUpFragmentChangeListener mListener;
	EditText signName, signEmail, signPassword, signConfirmPassword;
	boolean validUser, resultSignUp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_signup, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (onSignUpFragmentChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "should implement OnSignUpFragmentChangeListener");
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		signName = (EditText) getActivity().findViewById(R.id.signName);
		signEmail = (EditText) getActivity().findViewById(R.id.signEmail);
		signPassword = (EditText) getActivity().findViewById(R.id.signPassword);
		signConfirmPassword = (EditText) getActivity().findViewById(
				R.id.signConfirmPassword);

		getActivity().findViewById(R.id.btnSignUp).setOnClickListener(
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

		getActivity().findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mListener.gotoLogin();
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
				Toast.makeText(getActivity(), "Passwords do not match",
						Toast.LENGTH_LONG).show();
			}

			if (isValidEmail(signEmail.getText().toString())) {
				validUser = true;
			} else {
				validUser = false;
				Toast.makeText(getActivity(), "Invalid email format",
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(getActivity(), "Please complete all the details",
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
					mListener.gotoToDo(signEmail.getText().toString(),false,-1);
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
					e.printStackTrace();
					Toast.makeText(
							getActivity(),
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

	public interface onSignUpFragmentChangeListener {
		void gotoLogin();

		void gotoToDo(String email,boolean delete, int pos);
	}
}
