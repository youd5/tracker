package coma;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import db.HistoricalPricesDaily;
import util.FormatResult;
import util.SymbolList;

/**
 * Sample URL from where to pull the data
 * https://nseindia.com/api/historical/cm/equity?symbol=ICICIBANK&series=[%22EQ%22]&from=30-07-2019&to=30-01-2020
 */
public class Quote {
	public static String urlString = "https://nseindia.com/api/historical/cm/equity?series=[%22EQ%22]&from=30-07-2019&to=09-02-2020&symbol=";
	
	public static void main(String args[]) {
		List <String> symbolList = SymbolList.getEquitySymbolList();
		Map <String,String> marketData = new HashMap<String,String>();
		String symbol;
		try {
			for(int i = 0; i < symbolList.size(); i++) {
				symbol = symbolList.get(i);
				String json = fetchHistoricalQuotesJson(symbol);
				parseJson(json, symbol);
				marketData.put(symbol, json);
			}
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		System.out.println("END execution");
	}
	
	public static String fetchHistoricalQuotesJson(String symbol) {
		String url = urlString + symbol;
		String json = jsonGetRequest(url);
		return json;
	}
	
	

	public static void parseJson(String json,String symbol) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.names();

			String key = jsonArray.getString(0);
			
			JSONArray jsonArrayLevel2 = jsonObject.getJSONArray(key);
			float avgClose = 0;
			int count = 0;
			for (int j = 0; j < jsonArrayLevel2.length(); j++) {
				float open_price = jsonArrayLevel2.getJSONObject(j).getFloat("CH_OPENING_PRICE");
			    float close_price = jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE");
			    float prev_close_price = jsonArrayLevel2.getJSONObject(j).getFloat("CH_PREVIOUS_CLS_PRICE");
			    float high_price = jsonArrayLevel2.getJSONObject(j).getFloat("CH_TRADE_HIGH_PRICE");
			    float low_price = jsonArrayLevel2.getJSONObject(j).getFloat("CH_TRADE_LOW_PRICE");
			    float ltp_price = jsonArrayLevel2.getJSONObject(j).getFloat("CH_LAST_TRADED_PRICE");
			    float high52 = jsonArrayLevel2.getJSONObject(j).getFloat("CH_52WEEK_HIGH_PRICE");
			    float low52 = jsonArrayLevel2.getJSONObject(j).getFloat("CH_52WEEK_LOW_PRICE");
			    int vol = jsonArrayLevel2.getJSONObject(j).getInt("CH_TOT_TRADED_QTY");
			    String mTimeStamp = jsonArrayLevel2.getJSONObject(j).getString("mTIMESTAMP");
			    
			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy"); 
			    LocalDate date = LocalDate.parse(mTimeStamp, formatter);
				
			    HistoricalPricesDaily.insert(symbol, open_price, close_price, prev_close_price, high_price, low_price, ltp_price, high52, low52, vol, java.sql.Date.valueOf(date));
				avgClose += jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE"); 
				count++;
			}
			System.out.println(symbol +": " + avgClose/count);

		} catch (Exception e) {

			System.out.println("position: " + e.getStackTrace());
			System.out.println(e);
		}
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
	
	
	public static void printResultToHTML() {
		List <String> symbolList = SymbolList.getEquitySymbolList();
		String symbol;
		try {

			// Creating a File object that represents the disk file.
			PrintStream o = new PrintStream(new File("A1.html"));

			// Store current System.out before assigning a new value
			PrintStream console = System.out;

			// Assign o to output stream
			System.setOut(o);
			System.out.println("<html><body><table>");
			System.out.println("<tr><td><b>Symbol</b></td><td><b> T90 avg</b></td><tr>");

			for (int i = 0; i < symbolList.size(); i++) {
				symbol = symbolList.get(i);
				String url = urlString + symbol;
				String json = jsonGetRequest(url);
				parseJson(json, symbol);

			}
			System.out.println("</table></body></html>");
			// System.out.println(json);
			File htmlFile = new File("A1.html");
			FormatResult.openInBrowser(htmlFile.toURL().toString());

		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
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

	private static String streamToString(InputStream inputStream) {
		String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
		return text;
	}

}
