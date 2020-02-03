package com;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Sample URL from where to pull the data
 * https://nseindia.com/api/historical/cm/equity?symbol=ICICIBANK&series=[%22EQ%22]&from=30-07-2019&to=30-01-2020
 */
public class Quote {

	public static void main(String args[]) {
		String baseNSEUrl = "https://nseindia.com";
		String subUrlHD = "/api/historical/cm/equity?";
		String urlParamsHD = "series=[%22EQ%22]&from=30-07-2019&to=02-02-2020&symbol=";
		String urlString = baseNSEUrl + subUrlHD + urlParamsHD;
		List <String> symbolList = getEquitySymbolList();
		String symbol;
		try {
			
			// Creating a File object that represents the disk file. 
	        PrintStream o = new PrintStream(new File("A1.html")); 
	  
	        // Store current System.out before assigning a new value 
	        PrintStream console = System.out; 
	  
	        // Assign o to output stream 
	        System.setOut(o); 
	        System.out.println("<html><body><table>"); 
	  
	        // Use stored value for output stream 
	        //System.setOut(console); 
	        //System.out.println("This will be written on the console!"); 
	        System.out.println("<tr><td><b>Symbol</b></td><td><b> T90 avg</b></td><tr>");
	        
			for(int i = 0; i < symbolList.size(); i++) {
				symbol = symbolList.get(i);
				String url = urlString + symbol;
				String json = jsonGetRequest(url);
				parseJson(json, symbol);
				
			}
			System.out.println("</table></body></html>");
			//System.out.println(json);
			File htmlFile = new File("A1.html");
			FormatResult.openInBrowser(htmlFile.toURL().toString());
			
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}

	}
	
	public static List<String> getEquitySymbolList(){
		List <String> symbolList = new ArrayList<String>();
		symbolList.add("ICICIBANK");
		symbolList.add("ITC");
		symbolList.add("HINDUNILVR");
		symbolList.add("NESTLEIND");
		symbolList.add("TECHM");
		symbolList.add("HDFC");
		symbolList.add("LT");
		symbolList.add("TCS");
		symbolList.add("HDFCBANK");
		symbolList.add("MARUTI");
		
		symbolList.add("BAJFINANCE");
		symbolList.add("KOTAKBANK");
		symbolList.add("WIPRO");
		symbolList.add("RELIANCE");
		symbolList.add("SBIN");
		symbolList.add("BHARTIARTL");
		symbolList.add("TITAN");
		symbolList.add("EICHERMOT");
		return symbolList;
	}

	public static void parseJson(String json,String symbol) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.names();
			List<Float> list = new ArrayList<Float>();

			String key = jsonArray.getString(0);
			
			JSONArray jsonArrayLevel2 = jsonObject.getJSONArray(key);
			float avgClose = 0;
			int count = 0;
			for (int j = 0; j < jsonArrayLevel2.length(); j++) {
				list.add(jsonArrayLevel2.getJSONObject(0).getFloat("CH_OPENING_PRICE"));
				avgClose += jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE"); 
				count++;
			}
			System.out.println(" <tr><td>" + symbol +": </td><td> " + avgClose/count + "</td></tr>");

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
	

	public static void printPriceData(String json,String symbol) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.names();
			List<Float> list = new ArrayList<Float>();

			String key = jsonArray.getString(0);

			
			System.out.println("|----------------------------------------------|");
			System.out.println("|Date        |Open | Close   |High    |Low    |");
			System.out.println("|----------------------------------------------");
			

			JSONArray jsonArrayLevel2 = jsonObject.getJSONArray(key);
			float avgClose = 0;
			int count = 0;
			for (int j = 0; j < jsonArrayLevel2.length(); j++) {
				list.add(jsonArrayLevel2.getJSONObject(0).getFloat("CH_OPENING_PRICE"));
				
				System.out.println("|" + jsonArrayLevel2.getJSONObject(j).getString("mTIMESTAMP") + " | "
						+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_OPENING_PRICE") + " | "
						+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE") + " | "
						+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_TRADE_HIGH_PRICE") + " | "
						+ jsonArrayLevel2.getJSONObject(j).getFloat("CH_TRADE_LOW_PRICE") + " | ");
				
				avgClose += jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE"); 
				count++;
			}
			System.out.println("T30-avg, " + symbol +": " + avgClose/count);

		} catch (Exception e) {

			System.out.println("position: " + e.getStackTrace());
			System.out.println(e);
		}
	}

	private static String streamToString(InputStream inputStream) {
		String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
		return text;
	}

}
