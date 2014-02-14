package com.example.runningbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.runningbuddy.users.User;

public class MainActivity extends Activity {
	private int exit = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		
		gridview.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				if (position == 0){
				Intent intent = new Intent(MainActivity.this, User.class);
				startActivity(intent);
				}
				else if(position == 1){
					Intent intent = new Intent(MainActivity.this, Map.class);
					startActivity(intent);
				}
				else if(position == 2){
					Intent intent = new Intent(MainActivity.this, PreviousRoutes.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			finish();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
			finish();
			return true;
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {	
	   //if ( keyCode == KeyEvent.KEYCODE_MENU ) {
		   //override the menu key
		   // perform your desired action here
	      // return true;
	   //}
	   if (keyCode == KeyEvent.KEYCODE_BACK){
		   Toast.makeText(getApplicationContext(), "You pressed the Back Button",Toast.LENGTH_SHORT).show();
		   return false;
	   }

	   // let the system handle all other key events
	   return super.onKeyDown(keyCode, event);
	}
	

}
