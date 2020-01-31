package com;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Sample URL from where to pull the data
 * https://nseindia.com/api/historical/cm/equity?symbol=ICICIBANK&series=[%22EQ%22]&from=30-07-2019&to=30-01-2020
 */
public class Quote {

	public static void main(String args[]) {
		String baseNSEUrl = "https://nseindia.com";
		String subUrlHD = "/api/historical/cm/equity?";
		String urlParamsHD = "symbol=ICICIBANK&series=[%22EQ%22]&from=30-07-2019&to=30-01-2020";
		String urlString = baseNSEUrl + subUrlHD + urlParamsHD;
		System.out.println(urlString);
		System.out.println("------------------------");
		try {
			String json = jsonGetRequest(urlString);
			System.out.println(json);
			parseJson(json);
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}

	}

	public static void parseJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.names();
			List<Float> list = new ArrayList<Float>();

			for (int i = 0; i < 1; ++i) {
				String key = jsonArray.getString(i);

				System.out.println("|----------------------------------------------");
				System.out.println("|Date        |Open | Close   |High    |Low    |");
				System.out.println("|----------------------------------------------");

				JSONArray jsonArrayLevel2 = jsonObject.getJSONArray(key);
				for (int j = 0; j < jsonArrayLevel2.length(); j++) {
					list.add(jsonArrayLevel2.getJSONObject(i).getFloat("CH_OPENING_PRICE"));
					System.out.println("|"+ jsonArrayLevel2.getJSONObject(j).getString("mTIMESTAMP") + " | "
							+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_OPENING_PRICE") + " | "
							+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE") + " | "
							+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_TRADE_HIGH_PRICE") + " | "
							+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_TRADE_LOW_PRICE")
							);
				}
			}

		} catch (Exception e) {

			System.out.println("position: " + e.getStackTrace());
			System.out.println(e);
		}
	}

	public static String jsonGetRequest(String urlQueryString) {
		String json = null;
		try {
			URL url = new URL(urlQueryString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			// todo: moved to a small method this map setters
			Map<String, String> headers = new HashMap<>();
			headers.put("charset", "utf-8");
			headers.put("X-CSRF-Token", "fetch");
			headers.put("content-type", "application/json");
			headers.put("Accept", "*/*");
			headers.put("Accept-Language", "en-US,en;q=0.5");
			headers.put("Host", "nseindia.com");
			headers.put("Referer",
					"\"https://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/GetQuote.jsp?symbol=INFY&illiquid=0&smeFlag=0&itpFlag=0");
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
			headers.put("X-Requested-With", "XMLHttpRequest");
			for (String headerKey : headers.keySet()) {
				connection.setRequestProperty(headerKey, headers.get(headerKey));
			}
			connection.connect();
			InputStream inStream = connection.getInputStream();
			json = streamToString(inStream); // input stream to string
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return json;
	}

	private static String streamToString(InputStream inputStream) {
		String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
		return text;
	}

}
