package com.example.weatherbeacon;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.connection.WebConnection;

/**
 * Main activity for Weather Beacon program. Shows state of weather beacon
 * (based on the difference between today and tomorrow's forecasts) and has
 * options that pop up in a contextual menu when long pressing on the beacon
 * image.
 * 
 * @author Dan
 * 
 */
public class WeatherBeacon extends FragmentActivity {

	private String[] menuOptions = new String[] { "Weather Beacon Key",
			"Beacon Map" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Retrieve weather data from Yahoo.
		WebConnection wc = new WebConnection();
		wc.execute("http://weather.yahooapis.com/forecastrss?w=2391446");
		InputStream is = null;

		try {
			// Have to account for a possible delay. This line of code
			// retrieves
			// the result as long as the app is able to contact the Yahoo!
			// Weather service within the specified time.
			is = wc.get(3000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Log.e("log_tag", e.getMessage());
		}

		// Parse the XML data retrieved from the Yahoo! Weather API.
		WeatherParser wp = new WeatherParser(is);

		View view = this.getWindow().getDecorView();
		view.setBackgroundColor(Color.BLACK);

		// Store beacon to a variable so we can manipulate later.
		ImageView iv = (ImageView) findViewById(R.id.weatherbeacon);
		int beacon;

		// Change weather beacon color based on the difference between today and
		// tomorrow's forecasts.
		final int CHANGE_TOLERANCE = 4;
		if (wp.getTomorrowForecast() - wp.getTodayForecast() > CHANGE_TOLERANCE) {
			beacon = R.drawable.weatherbeacon_red; // Tomorrow is warmer
		} else if (wp.getTomorrowForecast() - wp.getTodayForecast() < -CHANGE_TOLERANCE) {
			beacon = R.drawable.weatherbeacon_white; // Tomorrow is cooler
		} else {
			beacon = R.drawable.weatherbeacon_green; // Tomorrow is about the
														// same
		}

		// Set image to color specified above.
		iv.setImageResource(beacon);

		// Determine whether tomorrow's forecast includes precipitation. This is
		// currently hardcoded as Yahoo's table for the values seems somewhat
		// arbitrary at first glance.
		int conditions = wp.getConditions();
		if ((conditions <= 18 || conditions >= 35) && conditions != 36
				&& conditions != 44) {
			// If tomorrow's forecast includes precipitation, make beacon flash.
			Animation fadeInAnimation = AnimationUtils.loadAnimation(this,
					R.anim.tween);
			iv.startAnimation(fadeInAnimation);
		}

		// Enable contextual menu on long press of beacon.
		registerForContextMenu(iv);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options");

		// Add pre-defined options to contextual menu.
		for (int i = 0; i < menuOptions.length; ++i) {
			menu.add(Menu.NONE, i, i + 1, menuOptions[i]);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// TODO Change this to switch on the titles of menu items instead of
		// Item IDs.
		switch (item.getItemId()) {
		case 0:
			new AlertDialog.Builder(this)
					.setTitle("WEATHER BEACON KEY")
					.setMessage(
							"Red: Warm weather ahead.\nWhite: Cold weather in sight.\nGreen: No change forseen.\nFlashing: Precipitation.")
					.setNeutralButton("Great.",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
								}
							})

					.show();
			break;
		case 1:
			Intent intent = new Intent(this, WeatherMap.class);
			startActivity(intent);
		default:
			break;
		}

		return true;
	}

}
