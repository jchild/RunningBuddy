package com.example.runningbuddy.users;

import com.example.runningbuddy.R;
import com.example.runningbuddy.R.layout;
import com.example.runningbuddy.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NewUser extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

}