package com.connection;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Class used to establish a connection with the Yahoo! Weather API. This is
 * done via an Asynchronous task so as to not run on the UI thread.
 * 
 * @author Dan
 * 
 */
public class WebConnection extends AsyncTask<String, Void, InputStream> {

	private InputStream is;

	/**
	 * Create a new web connection with the Yahoo! Weather API.
	 */
	public WebConnection() {
		is = null;
	}

	@Override
	protected InputStream doInBackground(String... params) {

		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(params[0]);

		try {
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			// Shows any errors in LogCat.
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		return is;
	}

	@Override
	protected void onPostExecute(InputStream result) {
		// Don't need anything to occur here as of right now.
	}
}
