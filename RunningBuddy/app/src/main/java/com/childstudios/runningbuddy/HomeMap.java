package com.childstudios.runningbuddy;

import android.content.res.Configuration;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeMap extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Location currentLocation;
    private Boolean mRequestingLocationUpdates;


    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setUpMapIfNeeded();

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
        getLocation();
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

    public void getLocation(){
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(currentLocation == null){
            Toast.makeText(getApplicationContext(), "getting location", Toast.LENGTH_SHORT).show();
            getLocation();
        }
        else{
            LatLng myLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc,16));
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
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
        getLocation();

        Button start = (Button) findViewById(R.id.startRun);
        Button stop = (Button) findViewById(R.id.stopRun);
        start.setVisibility(view.GONE);
        stop.setVisibility(view.VISIBLE);
    }

    public void stopRecord(View view){
        Toast.makeText(getApplicationContext(), "Stopping run", Toast.LENGTH_SHORT).show();
        getLocation();

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
