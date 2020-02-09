package util;

import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TempDebuggerCode {
	public static String urlString = "https://nseindia.com/api/historical/cm/equity?series=[%22EQ%22]&from=30-07-2019&to=09-02-2020&symbol=";
	
	
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
				String json = Helper.fetchJsonResultFromRemoteURL(url);
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
			    String mTimeStamp = jsonArrayLevel2.getJSONObject(j).getString("mTIMESTAMP");
			    
			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy"); 
			    LocalDate date = LocalDate.parse(mTimeStamp, formatter);
				
				avgClose += jsonArrayLevel2.getJSONObject(j).getFloat("CH_CLOSING_PRICE"); 
				count++;
			}
			System.out.println(symbol +": " + avgClose/count);

		} catch (Exception e) {

			System.out.println("position: " + e.getStackTrace());
			System.out.println(e);
		}
	}

}
