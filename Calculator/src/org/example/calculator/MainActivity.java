package org.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText operand1;
	EditText operand2;
	Button btnPlus;
	Button btnSubtract;
	Button btnMultiply;
	Button btnDivide;
	TextView result;
	Button btnClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Input fields to be added/subt/mult/divided

		operand1 = (EditText) findViewById(R.id.editOperand1);
		operand2 = (EditText) findViewById(R.id.editOperand2);

		// Associated Buttons

		btnPlus = (Button) findViewById(R.id.btnPlus);
		btnSubtract = (Button) findViewById(R.id.btnSubtract);
		btnMultiply = (Button) findViewById(R.id.btnMultiply);
		btnDivide = (Button) findViewById(R.id.btnDivide);
		btnClear = (Button) findViewById(R.id.btnClear);

		// Associate Result field

		result = (TextView) findViewById(R.id.textResult);

		// Bind buttons to code
		btnPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (operand1.getText().toString().length() > 0
						&& operand2.getText().toString().length() > 0) {
					float oper1 = Float.parseFloat(operand1.getText()
							.toString());
					float oper2 = Float.parseFloat(operand2.getText()
							.toString());

					float theResult = oper1 + oper2;
					result.setText(Float.toString(theResult));
				}

			}
		});

		btnSubtract.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (operand1.getText().toString().length() > 0
						&& operand2.getText().toString().length() > 0) {
					float oper1 = Float.parseFloat(operand1.getText()
							.toString());
					float oper2 = Float.parseFloat(operand2.getText()
							.toString());

					float theResult = oper1 - oper2;
					result.setText(Float.toString(theResult));
				}
			}
		});

		btnMultiply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (operand1.getText().toString().length() > 0
						&& operand2.getText().toString().length() > 0) {
					float oper1 = Float.parseFloat(operand1.getText()
							.toString());
					float oper2 = Float.parseFloat(operand2.getText()
							.toString());

					float theResult = oper1 * oper2;
					result.setText(Float.toString(theResult));
				}
			}
		});

		btnDivide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (operand1.getText().toString().length() > 0
						&& operand2.getText().toString().length() > 0) {
					float oper1 = Float.parseFloat(operand1.getText()
							.toString());
					float oper2 = Float.parseFloat(operand2.getText()
							.toString());

					float theResult = oper1 / oper2;
					result.setText(Float.toString(theResult));
				}
			}
		});

		btnClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				operand1.setText("");
				operand2.setText("");
				result.setText("0.00");

			}
		});

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
