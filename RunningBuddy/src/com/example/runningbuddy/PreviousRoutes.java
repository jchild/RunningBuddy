package com.example.runningbuddy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class PreviousRoutes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previous_routes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.previous_routes, menu);
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
