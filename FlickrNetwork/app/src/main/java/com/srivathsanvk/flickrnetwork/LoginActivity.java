package com.srivathsanvk.flickrnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            Parse.initialize(this, "Cw1uGaGYBORs27dCoSYS2fOhcPhnhgyPfHVjmWv2",
                    "5ZBX2nhvUoZBqprnxg04j4EYFZ6pMxK77Cse49g9");
        } catch (Exception e) {

        }

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, ForumActivity.class);
            startActivity(intent);
            finish();
        } else {
            editEmail = (EditText) findViewById(R.id.editEmail);
            editPassword = (EditText) findViewById(R.id.editPassword);

            findViewById(R.id.btnLogin).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (editEmail.getText().toString().length() != 0
                                    && editPassword.getText().toString().length() != 0) {

                                authenticateUser();

                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Please complete the Login details",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });

            findViewById(R.id.btnSignUp).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                            startActivity(intent);
                            finish();

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

                            Intent intent = new Intent(LoginActivity.this, ForumActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Signup failed. Look at the ParseException to see
                            // what happened.
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Invalid details",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
