package com.example.selectiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static int focusImageCount;
	int focusImageId;
	ArrayList<Item> arrayItem = new ArrayList<Item>();
	ArrayList<ImageButton> imgButton = new ArrayList<ImageButton>();
	public static String focusImage;
	RelativeLayout relativeLayout;
	TextView mainText;
	TableLayout tableLayout;
	boolean contentChanged;
	TableRow row1;
	boolean reset;
	String focusImageCaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		// CreateItemLayout createItemLayout = new CreateItemLayout();

		// setContentView(createItemLayout.createLayout(this));

		// ArrayList<Item> arrayItem = new ArrayList<Item>();
		arrayItem.add(new Item("apple", R.drawable.apple));
		arrayItem.add(new Item("lemon", R.drawable.lemon));
		arrayItem.add(new Item("mango", R.drawable.mango));
		arrayItem.add(new Item("peach", R.drawable.peach));
		arrayItem.add(new Item("strawberry", R.drawable.strawberry));
		arrayItem.add(new Item("tomato", R.drawable.tomato));

		relativeLayout = new RelativeLayout(this);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		relativeLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		mainText = new TextView(this);

		mainText.setId(100);
		
		Button resetButton = new Button(this);
		resetButton.setId(101);
		relativeLayout.addView(mainText);

		tableLayout = new TableLayout(this);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		p.addRule(RelativeLayout.BELOW, mainText.getId());
		
		tableLayout.setPadding(0,16, 0, 16);
		
		tableLayout.setLayoutParams(p);
       
		relativeLayout.addView(tableLayout);

		// add buttons

		//Button resetButton = new Button(this);
		Button exitButton = new Button(this);
		RelativeLayout.LayoutParams bl = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams br = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		exitButton.setId(102);

		bl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	
		bl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		br.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		br.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	
		resetButton.setLayoutParams(bl);
		resetButton.setText("   Reset   ");
		resetButton.setBackgroundColor(Color.parseColor("#1B5E20"));
		resetButton.setTextColor(Color.WHITE);
		exitButton.setLayoutParams(br);
		exitButton.setBackgroundColor(Color.parseColor("#F44336"));
		exitButton.setTextColor(Color.WHITE);
		exitButton.setText("   Exit   ");
		relativeLayout.addView(resetButton);
		relativeLayout.addView(exitButton);

		exitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				System.exit(0);
			}
		});

		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = getIntent();
				// finish();
				// startActivity(intent);;

				imgButton.removeAll(imgButton);
				generateButtons();
				Collections.shuffle(imgButton);

				tableLayout.removeAllViews();

				relativeLayout.removeView(tableLayout);
				reset = true;
				createLayout();
			}
		});

		generateButtons();
		Collections.shuffle(imgButton);
		createLayout();

		setContentView(relativeLayout);

	}

	void generateButtons() {
		Collections.shuffle(arrayItem);
		focusImage = arrayItem.get(0).getName();
		focusImageId = arrayItem.get(0).getDraw_id();
		Random rand = new Random();

		focusImageCount = rand.nextInt((8 - 1) + 1) + 1;

		mainText.setTextAppearance(this, android.R.style.TextAppearance_Large);
		
		RelativeLayout.LayoutParams g = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		mainText.setGravity(Gravity.CENTER_HORIZONTAL);
		mainText.setLayoutParams(g);
		
		
       focusImageCaps = Character.toUpperCase(focusImage.charAt(0)) + focusImage.substring(1);
		mainText.setText("Find All " + focusImageCaps + "s (" + focusImageCount
				+ ")");

		for (int i = 0; i < focusImageCount; i++) {

			ImageButton imgB = new ImageButton(this);
			imgB.setImageResource(focusImageId);
			imgB.setTag(focusImage);

			imgButton.add(imgB);

		}

		int otherImageCount = 25 - focusImageCount;
		int temp = 0;

		for (int i = 0; i < 5; i++) {

			for (Item item : arrayItem) {
				if (temp < otherImageCount) {
					if (!item.getName().equals(focusImage)) {
						ImageButton imgB = new ImageButton(this);
						imgB.setImageResource(item.getDraw_id());
						imgB.setTag(item.getName());

						imgButton.add(imgB);
						temp++;

					}

				}
			}
		}

	}

	public void createLayout() {

		int fiveCount;
		String src;

		if (contentChanged == true || reset == true) {
			Log.d("TAG", Integer.toString(relativeLayout.getChildCount()));
			relativeLayout.addView(tableLayout);
			// Collections.shuffle(imgButton);
			myShuffle(imgButton);
		}
		

		row1 = new TableRow(this);
		row1.setLayoutParams(new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT,1.0f));

		fiveCount = 0;
		for (final ImageButton imgButtonNew : imgButton) {

			fiveCount++;
;

			imgButtonNew.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					175,1.0f));
		
			row1.addView(imgButtonNew);

			if (fiveCount % 5 == 0) {

				tableLayout.addView(row1);
				row1 = new TableRow(this);

			}

			imgButtonNew.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					/*
					 * Toast.makeText(MainActivity.this,
					 * imgButtonNew.getTag().toString(),
					 * Toast.LENGTH_SHORT).show();
					 */

					if (imgButtonNew.getTag().toString() == focusImage
							&& imgButtonNew.getAlpha() == 1) {
						focusImageCount--;
						if(focusImageCount == 0){
							generateAlert();
						}
						imgButtonNew.setAlpha(0.3f);
						mainText.setText("Find all " + focusImageCaps + "s ("
								+ focusImageCount + ")");

						for (ImageButton ib : imgButton) {
							((ViewGroup) ib.getParent()).removeView(ib);
						}
						tableLayout.removeAllViews();

						relativeLayout.removeView(tableLayout);

						contentChanged = true;
						createLayout();

					}

				}
			});

		}

	}

	public void generateAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Found all shapes")
		.setMessage("Congratulations !! You have found all the " + focusImageCaps + "s.")
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setNegativeButton("New Game", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				imgButton.removeAll(imgButton);
				generateButtons();
				Collections.shuffle(imgButton);

				tableLayout.removeAllViews();

				relativeLayout.removeView(tableLayout);
				reset = true;
				createLayout();
				
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
	}
	@SuppressWarnings("unchecked")
	public void myShuffle(ArrayList<ImageButton> arrButton) {
		ImageButton[] shuffledImgButtons = new ImageButton[25];

		int[] focusPos = new int[8];
		int index = 0;
		int fIndx = 0;
		for (ImageButton temp : arrButton) {
			if (temp.getAlpha() != 1) {
				focusPos[fIndx] = index;
				fIndx++;

			}
			index++;
		}

		Collections.shuffle(arrButton);
		int i = 0;
		for (ImageButton t : arrButton) {
			if (t.getAlpha() != 1) {

				// shuffledImgButtons.add(focusPos[i],t);
				shuffledImgButtons[focusPos[i]] = t;

				// Toast.makeText(this,
				// arrButton.get(focusPos[i]).getTag().toString(),
				// Toast.LENGTH_SHORT).show();

				i++;
			}
		}

		// arrButton = shuffledImgButtons;

		int pos;

		for (ImageButton s : arrButton) {

			if (s.getAlpha() == 1) {

				pos = 0;
				for (ImageButton sh : shuffledImgButtons) {
					if (sh == null) {
						shuffledImgButtons[pos] = s;

						break;

					}
					pos++;

				}
			}

		}

		/*
		 * for(ImageButton n : shuffledImgButtons){ Toast.makeText(this,
		 * n.getTag().toString(), Toast.LENGTH_SHORT).show(); }
		 */
		// for(ImageButton sh:shuffledImgButtons){

		// if(sh != null)
		// Toast.makeText(this, sh.getTag().toString(),
		// Toast.LENGTH_SHORT).show();
		// }
		// arrButton.removeAll(arrButton);

		/*
		 * for(ImageButton im : shuffledImgButtons){
		 * 
		 * 
		 * //arrButton.add(im); //Toast.makeText(this, im.getTag().toString(),
		 * //Toast.LENGTH_SHORT).show();
		 * 
		 * Log.d("My", im.getTag().toString()); }
		 */

		arrButton.removeAll(arrButton);
		for (ImageButton im : shuffledImgButtons) {
			arrButton.add(im);
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
