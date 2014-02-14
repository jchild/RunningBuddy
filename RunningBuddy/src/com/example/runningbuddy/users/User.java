package com.example.runningbuddy.users;

import com.example.runningbuddy.MainActivity;
import com.example.runningbuddy.R;
import com.example.runningbuddy.Settings;
import com.example.runningbuddy.R.id;
import com.example.runningbuddy.R.layout;
import com.example.runningbuddy.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class User extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.action_exit:
			finish();
			return true;
		case R.id.back:
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}


}
