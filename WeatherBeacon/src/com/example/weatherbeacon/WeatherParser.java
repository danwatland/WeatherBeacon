package com.example.weatherbeacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class WeatherParser {

	private String todayForecast;
	private String tomorrowForecast;
	private String conditions;
	private InputStream is;
	

	public WeatherParser(InputStream is) {
		this.is = is;
		parse();
	}
	
	public int getTodayForecast() {
		return Integer.valueOf(todayForecast);
	}

	public int getTomorrowForecast() {
		return Integer.valueOf(tomorrowForecast);
	}

	public int getConditions() {
		return Integer.valueOf(conditions);
	}
	
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
		}
		return day;
	}
	
	private String parseLine(String line, String key) {
		String str = line.substring(line.indexOf(key +"=\"") + 6);
		String result = findValue(str);
		
		return result;
	}
	
	private String findValue(String txt) {
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		while (txt.charAt(i) != '\"') {
			i++;
		}
		
		return txt.substring(0, i);
	}
}
