package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
https://www1.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?symbol=MARUTI&series=EQ&fromDate=undefined&toDate=undefined&datePeriod=3months
https://nseindia.com/api/historical/cm/equity?symbol=ICICIBANK&series=[%22EQ%22]&from=30-07-2019&to=30-01-2020
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
			URL url = new URL(urlString);
			URLConnection yc = url.openConnection();
			
			//Set Request Headers
			Map<String, String> headers = new HashMap<>();
			headers.put("X-CSRF-Token", "fetch");
			headers.put("content-type", "application/json");
			headers.put("Accept", "*/*");
			headers.put("Accept-Language","en-US,en;q=0.5");
			headers.put("Host","nseindia.com");
			headers.put("Referer","\"https://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/GetQuote.jsp?symbol=INFY&illiquid=0&smeFlag=0&itpFlag=0");
			headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
			headers.put("X-Requested-With","XMLHttpRequest");
			for (String headerKey : headers.keySet()) {
			    yc.setRequestProperty(headerKey, headers.get(headerKey));
			}
			
			
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while((inputLine = in.readLine()) != null) 
				System.out.println(inputLine);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
