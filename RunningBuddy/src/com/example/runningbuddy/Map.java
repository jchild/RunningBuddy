package com.example.runningbuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class Map extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	@Override
	//menu handler
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(Map.this, Settings.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.action_exit:
			finish();
			return true;
		case R.id.back:
			Intent i = new Intent(Map.this, MainActivity.class);
			startActivity(i);
			finish();
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}

}
