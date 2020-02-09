package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Helper {
	
	
	public static String fetchJsonResultFromRemoteURL(String urlQueryString) {
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
