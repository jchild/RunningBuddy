package com.example.runningbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class Map extends FragmentActivity {
	private GoogleMap googleMap;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        googleMap =((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(); 
	 
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
			return true;
		case R.id.action_exit:
			finish();
			return true;
		case R.id.back:
			Intent intent = new Intent(Map.this, MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}

}
