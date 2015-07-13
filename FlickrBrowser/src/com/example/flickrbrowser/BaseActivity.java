package com.example.flickrbrowser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends ActionBarActivity{
   private Toolbar toolbar;
   protected Toolbar activateToolbar(){
	   if(toolbar == null){
		   toolbar =(Toolbar) findViewById(R.id.app_bar);
		   if(toolbar != null){
			   setSupportActionBar(toolbar);
		   }
	   }
	   return toolbar;
   }
}
