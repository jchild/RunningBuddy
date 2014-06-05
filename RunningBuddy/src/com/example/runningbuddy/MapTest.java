/*	MapTest.java
 * 
 * 	This is a test class to test out ideas for Map.java
 * 	so you wont adversely affect the working code. Anything
 * 	can be changed here to test new ideas. If you want to
 * 	point the app to this page will need to change the pointer
 * 	in MainActivity.java to point to MapTest.java instead of
 * 	Map.java
 * 
 * 
 */



package com.example.runningbuddy;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.Toast;

public class MapTest extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	private GoogleMap map;
	private LocationClient mLocationClient;
	private Location currentLoc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_test);
		mLocationClient = new LocationClient (getApplicationContext(), this, this);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_test, menu);
		return true;
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		//Connect to client
		mLocationClient.connect();
		

	}
	
	protected void getLoc(){
		currentLoc = mLocationClient.getLastLocation();
		if(currentLoc != null){
			LatLng myLocation = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15)); //keep zoom at 15
		}
	}
	
	
	@Override
	protected void onStop(){
		super.onStop();
		mLocationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		getLoc();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
