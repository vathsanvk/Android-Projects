package com.example.triviaquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultActivity extends Activity {
    int correctAnswerPct;
    TextView pctCorrect;
    ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		correctAnswerPct=getIntent().getExtras().getInt(TriviaActivity.RESULT_VALUE);
		
		pctCorrect = (TextView) findViewById(R.id.pctCorrect);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		pctCorrect.setText(Integer.toString(correctAnswerPct) + "%");
		progressBar.setProgress(correctAnswerPct);
		
		findViewById(R.id.btnQuit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(ResultActivity.this,
					//	MainActivity.class);
				//startActivity(intent);
				finish();	
			}
		});
		
		findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ResultActivity.this,
						TriviaActivity.class);
				startActivity(intent);
				finish();	
			}
		});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
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
