package com.example.weatherbeacon;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;

public class WeatherBeacon extends FragmentActivity {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		InputStream is = null;
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://weather.yahooapis.com/forecastrss?w=2391446");

		try {
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			// Shows any errors in LogCat.
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		WeatherParser wp = new WeatherParser(is);
		View view = this.getWindow().getDecorView();
		view.setBackgroundColor(Color.BLACK);
		ImageView iv = (ImageView) findViewById(R.id.weatherbeacon);
		int beacon;
		
		if (wp.getTomorrowForecast() - wp.getTodayForecast() > 4) {
			beacon = R.drawable.weatherbeacon_red;
		} else if (wp.getTomorrowForecast() - wp.getTodayForecast() < -4) {
			beacon = R.drawable.weatherbeacon_white;
		} else {
			beacon = R.drawable.weatherbeacon_green;
		}
		
		iv.setImageResource(beacon);
		
		int conditions = wp.getConditions();
		
		if ((conditions <= 18 || conditions >= 35) && conditions != 36 && conditions != 44) {
			Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.tween);
			iv.startAnimation(fadeInAnimation);
		}
		
		registerForContextMenu(iv);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options");
		menu.add("Additional Info");
		menu.add("Weather Beacon Key");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		
		new AlertDialog.Builder(this)
	    .setTitle("WEATHER BEACON KEY")
	    .setMessage("Red: Warm weather ahead.\nWhite: Cold weather in sight.\nGreen: No change forseen.\nFlashing: Precipitation.")
	    .setNeutralButton("Great.", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })

	     .show();
		
		return true;
	}

}
