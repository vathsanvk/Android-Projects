package com.example.androidconcurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	SeekBar seekBar;
	int complexity;
	ProgressDialog progressDialog;
	ProgressDialog progressDialog1;
	TextView textAnswer;
	TextView textSelectValue;
	double result;
	Handler handler;
	ExecutorService threadPool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		threadPool = Executors.newFixedThreadPool(2);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		textAnswer = (TextView) findViewById(R.id.textAnswer);
		seekBar.setMax(10);
		
		handler = new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case MyHeavyWorkThread.STATUS_START:
					progressDialog1 = new ProgressDialog(MainActivity.this);
					progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

					progressDialog1.setMessage("Retrieving the number. .");
					progressDialog1.setCancelable(false);
					progressDialog1.setMax(complexity);
					progressDialog1.show();
					break;
				case MyHeavyWorkThread.STATUS_STEP:
					progressDialog1.setProgress(msg.getData().getInt("PROGRESS"));
                    break;
				case MyHeavyWorkThread.STATUS_STOP:
					progressDialog1.dismiss();
					textAnswer.setText(Double.toString(msg.getData().getDouble("RESULT")));
					
				}
				return false;
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				complexity = progress;
				textSelectValue =(TextView) findViewById(R.id.textSelectValue); 
				textSelectValue.setText(complexity + " times");

			}
		});
		
		
		
		findViewById(R.id.buttonAsync).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new MyHeavyWork().execute(complexity);
			}
		});
		

		findViewById(R.id.buttonThread).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				threadPool.execute(new MyHeavyWorkThread());
			}
		});
		
		
		
		
		
		


	}
	public class MyHeavyWorkThread implements Runnable{
		static final int STATUS_START = 101;
		static final int STATUS_STEP = 102;
		static final int STATUS_STOP = 103;
		private double valueReturned;;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Message msg = new Message();
			msg.what = STATUS_START;
			handler.sendMessage(msg);
			valueReturned =0;
			
			for (int i = 0; i < complexity; i++) {
				valueReturned += HeavyWork.getNumber();
				msg = new Message();
				msg.what = STATUS_STEP;
				Bundle data = new Bundle();
				data.putInt("PROGRESS", i + 1);
				msg.setData(data);
				handler.sendMessage(msg);

			}
			msg = new Message();
			 Bundle data = new Bundle();
			 msg.what = STATUS_STOP;
			 data.putDouble("RESULT", valueReturned/complexity);
			 msg.setData(data);
			 handler.sendMessage(msg);
			
			
		}
		
		
	}

	public class MyHeavyWork extends AsyncTask<Integer, Integer, Double> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

			progressDialog.setMessage("Retrieving the number. .");
			progressDialog.setCancelable(false);
			progressDialog.setMax(complexity);
			progressDialog.show();

		}

		@Override
		protected void onPostExecute(Double result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			textAnswer.setText(Double.toString(result));
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected Double doInBackground(Integer... params) {
			int times;
			double valueReturned = 0;

			// TODO Auto-generated method stub

			times = params[0];

			for (int i = 0; i < times; i++) {
				valueReturned += HeavyWork.getNumber();
				publishProgress(i + 1);

			}

			return valueReturned / times;
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
