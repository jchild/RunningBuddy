package com.example.runningbuddy;


import java.util.Stack;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends FragmentActivity implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener {
	
	private GoogleMap map;
	private LocationClient LocationClient;
	private static final LocationRequest REQUEST = LocationRequest.create()
	            .setInterval(5000)         // 5 seconds
	            .setFastestInterval(16)    // 16ms = 60fps
	            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	private Stack<LatLng> points = new Stack<LatLng>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		startMap();
	}
	
    @Override
    public void onConnected(Bundle connectionHint) {
        LocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
        Location location = LocationClient.getLastLocation();				
		if(location == null){
			Toast.makeText(getApplicationContext(), "location returned null" , Toast.LENGTH_SHORT).show();
		}

		LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15)); //keep zoom at 15
		traceRoute();
    }
    
    public void traceRoute(){
    	Location location = LocationClient.getLastLocation();
    	LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
    	points.push(myLocation);
    	drawRoute();
    }
    public void drawRoute(){
    	PolylineOptions polylineOptions = new PolylineOptions();
    	polylineOptions.addAll(points);
    	Polyline route = map.addPolyline(polylineOptions);
    	Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();
    }
  
    @Override
    public void onDisconnected() {
        // Do nothing
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
				
				map.setMyLocationEnabled(true);
				setUpLocationClient();
				
				
			}
		}
	}

	private void setUpLocationClient() {
        if (LocationClient == null) {
            LocationClient = new LocationClient(getApplicationContext(), this, this);
        }
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        startMap();
        setUpLocationClient();
        LocationClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }
    @Override
	public void onLocationChanged(Location location) {
    	traceRoute();
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
			LocationClient.disconnect();
			finish();
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}
}
