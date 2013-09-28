package com.example.weatherbeacon;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class WeatherBeacon extends Activity {

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
		
		if (wp.getTomorrowForecast() - wp.getTodayForecast() > 4) {
			view.setBackgroundResource(R.drawable.weatherbeacon_red);
		} else if (wp.getTomorrowForecast() - wp.getTodayForecast() < -4) {
			view.setBackgroundResource(R.drawable.weatherbeacon_white);
		} else {
			view.setBackgroundResource(R.drawable.weatherbeacon_green);
		}
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
