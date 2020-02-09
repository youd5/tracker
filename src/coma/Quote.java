package coma;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import db.HistoricalPricesDaily;
import util.Helper;
import util.SymbolList;

/**
 * Sample URL from where to pull the data
 * https://nseindia.com/api/historical/cm/equity?symbol=ICICIBANK&series=[%22EQ%22]&from=30-07-2019&to=30-01-2020
 */
public class Quote {
	public static String urlString = "https://nseindia.com/api/historical/cm/equity?series=[%22EQ%22]&from=30-07-2019&to=09-02-2020&symbol=";
	
	public static void main(String args[]) {
		List <String> symbolList = SymbolList.getEquitySymbolList();
		String symbol;
		try {
			for(int i = 0; i < symbolList.size(); i++) {
				symbol = symbolList.get(i);
				String json = fetchHistoricalQuotesJson(symbol);
				parseJsonAndInsert(json, symbol);
			}
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		System.out.println("END execution");
	}
	
	public static String fetchHistoricalQuotesJson(String symbol) {
		String url = urlString + symbol;
		String json = Helper.fetchJsonResultFromRemoteURL(url);
		return json;
	}
	
	

	public static void parseJsonAndInsert(String json,String symbol) {
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
	
	
	
}
