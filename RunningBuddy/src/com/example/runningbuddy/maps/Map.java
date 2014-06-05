/*
 * Map.java
 * 
 * Main map class that will track the users location
 * and draw a route of where the user has been.
 * 
 * 
 * TODO:
 * -implement a pause button to pause the tracking of the user
 * -save users routes
 * -improve tracking system (still quite buggy)
 * -implement more graceful way of exiting to main menu
 * 
 * 
 * Author: Jonathan Child
 */

package com.example.runningbuddy.maps;


import java.util.Stack;

//import org.w3c.dom.Text;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runningbuddy.MainActivity;
import com.example.runningbuddy.R;
import com.example.runningbuddy.Settings;
import com.example.runningbuddy.R.id;
import com.example.runningbuddy.R.layout;
import com.example.runningbuddy.R.menu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends FragmentActivity implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener {
	
	private GoogleMap map;
	private LocationClient mLocationClient;
	private static final LocationRequest REQUEST = LocationRequest.create()
	            .setInterval(5000)         // 5 seconds
	            .setFastestInterval(16)    // 16ms = 60fps
	            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	private Stack<LatLng> points = new Stack<LatLng>(); // an array of LatLng points that will be used to  redraw and save the route
	private float distance = 0;
	private Location location, prevLocation = null;
	private Boolean paused = false;
	
	//TODO implement a pause button
	/*private OnClickListener pause = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			if(!paused){
				//no listener implemented yet to listen for updates... TODO
				//LocationClient.removeLocationUpdates(listener);
			}
			
		}};*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//methods for pause button
		//Button button = (Button) findViewById(R.id.pausebutton);
		//button.setOnClickListener(pause);
		setContentView(R.layout.activity_map);
	}
	
    @Override
    public void onConnected(Bundle connectionHint) {
        //Location listener, will listen for gps updates at the rate REQUEST is set at
    	mLocationClient.requestLocationUpdates(REQUEST,this); 
        getFirstLocation();
      				
		
    }
    
    public void getFirstLocation(){
    	location = mLocationClient.getLastLocation();
    	if(location == null){
			Toast.makeText(getApplicationContext(), "getting location" , Toast.LENGTH_SHORT).show();
			getFirstLocation();
		}
    	else{
    		LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
    		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15)); //keep zoom at 15
    		prevLocation = location;
    	} 
    }
    
    public void drawRoute(){
    	
    	LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
    	LatLng prevLoc = new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude());

    	PolylineOptions line = new PolylineOptions().add(loc,prevLoc);
    	
    	map.addPolyline(line);
    }
  

    	
	private void startMap(){
		if(map == null){
			 FragmentManager myFragmentManager = getSupportFragmentManager();
	         SupportMapFragment myMapFragment = (SupportMapFragment)myFragmentManager.findFragmentById(R.id.map);
			map = myMapFragment.getMap();
			if(map == null){
				Toast.makeText(getApplicationContext(), "Could not display map", Toast.LENGTH_SHORT).show();
			}
			else{
				setUpLocationClient();
			}
		}
	}
	@Override
	public void onLocationChanged(Location location) {
    	
		if(prevLocation == null){
			startMap();
		}
		else{
			this.location = location;
			distance += prevLocation.distanceTo(location);
    		LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
    		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15)); //keep zoom at 15
			TextView text = (TextView) findViewById(R.id.dist);
			text.setText(Float.toString(distance));
			drawRoute();
		}
	}
    

	private void setUpLocationClient() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(getApplicationContext(), this, this);
        }
    }
	@Override
	protected void onStart(){
		super.onStart();
		startMap();
		//TODO onStart event
	}
	@Override
	protected void onStop(){
		super.onStop();
		//TODO onStop event
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        //TODO when user resumes need to reconnect the location client and location listener
        startMap();
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	//TODO needs to disconnect location listener to stop listening for updates
    	mLocationClient.disconnect();
    	
    }
    @Override
    public void onDisconnected(){
    	//TODO
    	// prompted user GPS disconnected and trying to reconnect
    	// repeat prompted every 20 seconds until connection reestablished 
    }

    @Override
    public void onDestroy(){
    	//TODO
    	//save map, return back to home screen
    	super.onDestroy();
    	mLocationClient.disconnect();
    	finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    	//TODO
        // prompted user service is temporary unavailable
    }

    //Menu Items
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
			mLocationClient.disconnect();
			finish();
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}

	
}
