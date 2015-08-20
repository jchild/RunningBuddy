package com.childstudios.runningbuddy;


import android.content.res.Configuration;
import android.location.Location;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;



public class HomeMap extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Location currentLocation;
    private Location oldLocation;
    private float distance;
    private boolean recording;
    private Boolean mRequestingLocationUpdates;
    private TextToSpeech mTTS;

    TextView dist;
    TextView timer;
    long startTime;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long milis = System.currentTimeMillis() - startTime;
            int seconds = (int) (milis / 1000);
            int minutes = seconds / 60;
            int hours = minutes/60;
            seconds = seconds % 60;

            timer.setText(String.format("%d:%d:%02d",hours,minutes,seconds));

            timerHandler.postDelayed(this, 500);

        }
    };


    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);
        recording = false;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setUpMapIfNeeded();



        dist = (TextView) findViewById(R.id.distance);
        timer = (TextView) findViewById(R.id.time);

        dist.setText(String.valueOf(distance));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    /************************* map stuff **********************/

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        StartLocation();
        startLocationUpdates();
    }


    public void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);
        mRequestingLocationUpdates = true;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }

    public void StartLocation(){
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(currentLocation == null){
            Toast.makeText(getApplicationContext(), "getting location", Toast.LENGTH_SHORT).show();
            StartLocation();
        }
        else{
            LatLng myLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc,16));
            oldLocation = currentLocation;
        }

    }

    @Override
    public void onLocationChanged(Location location) {


        if(location != null && recording == true){
            LatLng myLoc = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc,16));
            distance += oldLocation.distanceTo(location);
            dist.setText(String.valueOf(distance));
            oldLocation = location;
        }

    }
    @Override
    public void onConnectionSuspended(int cause) {
        // Do nothing
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    public void startRecord(View view){
        Toast.makeText(getApplicationContext(), "Starting run", Toast.LENGTH_SHORT).show();
        recording = true;
        distance = 0;
        StartLocation();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        Button start = (Button) findViewById(R.id.startRun);
        Button stop = (Button) findViewById(R.id.stopRun);
        start.setVisibility(view.GONE);
        stop.setVisibility(view.VISIBLE);
    }

    public void stopRecord(View view){
        Toast.makeText(getApplicationContext(), "Stopping run", Toast.LENGTH_SHORT).show();

        recording = false;
        timerHandler.removeCallbacks(timerRunnable);

        Button start = (Button) findViewById(R.id.startRun);
        Button stop = (Button) findViewById(R.id.stopRun);
        start.setVisibility(view.VISIBLE);
        stop.setVisibility(view.GONE);
    }



    /**************** non map functions *************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homemap, menu);
        return true;
    }
    private void setupDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                com.childstudios.runningbuddy.R.string.drawer_open, com.childstudios.runningbuddy.R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Player List");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle().toString());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }




}
