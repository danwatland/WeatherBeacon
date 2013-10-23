package com.example.weatherbeacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Class to parse and extract values from a Yahoo! Weather API XML feed.
 * 
 * @author Dan
 * 
 */
public class WeatherParser {

	private String todayForecast;
	private String tomorrowForecast;
	private String conditions;
	private InputStream is;

	/**
	 * Creates a new instance of WeatherParser and extracts all relevant data
	 * from the InputStream.
	 * 
	 * @param is
	 *            InputStream containing XML data from the Yahoo! Weather API.
	 */
	public WeatherParser(InputStream is) {
		this.is = is;
		parse();
	}

	/**
	 * Returns today's temperature.
	 * 
	 * @return Today's temperature.
	 */
	public int getTodayForecast() {
		return Integer.valueOf(todayForecast);
	}

	/**
	 * Returns tomorrow's temperature.
	 * 
	 * @return Tomorrow's temperature.
	 */
	public int getTomorrowForecast() {
		return Integer.valueOf(tomorrowForecast);
	}

	/**
	 * Returns tomorrow's weather conditions (i.e. Sunny, partly cloudy, rain,
	 * etc.)
	 * 
	 * @return Tomorrow's weather conditions.
	 */
	public int getConditions() {
		return Integer.valueOf(conditions);
	}

	/**
	 * Parses the InputStream defined upon instantiation.
	 */
	private void parse() {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

		String line;
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains("yweather:forecast day=\"" + dayOfWeek(day))) {
					todayForecast = parseLine(line, "high");
					line = br.readLine();
					tomorrowForecast = parseLine(line, "high");
					conditions = parseLine(line, "code");
				}
			}
		} catch (IOException e) {

		}
	}

	/**
	 * Converts a value for day of the week (1 through 7) to a three letter
	 * abbreviation representing that day. This is used to match up between
	 * Java's Calendar class and the Yahoo! Weather XML feed.
	 * 
	 * @param d
	 *            Integer day of week - 1 through 7, where 1 is Sunday.
	 * @return Three-letter abbreviation representing the day.
	 */
	private String dayOfWeek(int d) {
		String day = null;

		switch (d) {
		case 1:
			day = "Sun";
			break;
		case 2:
			day = "Mon";
			break;
		case 3:
			day = "Tue";
			break;
		case 4:
			day = "Wed";
			break;
		case 5:
			day = "Thu";
			break;
		case 6:
			day = "Fri";
			break;
		case 7:
			day = "Sat";
			break;
		default:
			// Anything besides 1 through 7 will throw an exception due to
			// otherwise returning a null value.
			throw new IllegalArgumentException();
		}
		return day;
	}

	/**
	 * Returns the value in a line for a specified key.
	 * @param line Full text of the line.
	 * @param key Key to extract value for.
	 * @return Value for the specified key.
	 */
	private String parseLine(String line, String key) {
		String str = line.substring(line.indexOf(key + "=\"") + 6);
		String result = findValue(str);

		return result;
	}

	/**
	 * Finds value between a set of quotes.
	 * @param txt Text to find value in.
	 * @return Value between the first set of quotes.
	 */
	private String findValue(String txt) {
		int i = 0;
		while (txt.charAt(i) != '\"') {
			i++;
		}

		return txt.substring(0, i);
	}
}
