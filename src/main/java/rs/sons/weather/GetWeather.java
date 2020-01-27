package rs.sons.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//https://www.baeldung.com/java-http-request

public class GetWeather {

		//http://api.openweathermap.org/data/2.5/weather?id=3194360&APPID=0d04354b8dbb90fa9e02f92b8fcacdbf&units=metric
		private static final String URL = "http://api.openweathermap.org/data/2.5/weather?";
		private static final String APIID = "0d04354b8dbb90fa9e02f92b8fcacdbf";
		private static final String UNITS = "metric";
		private static final String CITY_ID = "3194360";
		
		public static void main(String[] args) throws IOException {
			
			Map<String, String> parameters = new HashMap<>();
			parameters.put("APPID", APIID);
			parameters.put("units", UNITS);
			parameters.put("id", CITY_ID);
			
			URL url = new URL(URL + ParameterStringBuilder.getParamsString(parameters));
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			//int status = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
			    response.append(inputLine);
			}
			
			in.close();
			
			System.out.println(response.toString());
			
			//https://stackoverflow.com/questions/8233542/parse-a-nested-json-using-gson
			JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
			
			System.out.println((jsonObject.getAsJsonObject().get("main")).getAsJsonObject().get("temp"));
			System.out.println((jsonObject.getAsJsonObject().get("main")).getAsJsonObject().get("temp"));
			System.out.println((jsonObject.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon")));
			
			con.disconnect();
		    

		}
		
		public static class ParameterStringBuilder {
		    public static String getParamsString(Map<String, String> params) 
		      throws UnsupportedEncodingException{
		        StringBuilder result = new StringBuilder();
		 
		        for (Map.Entry<String, String> entry : params.entrySet()) {
		          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
		          result.append("=");
		          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		          result.append("&");
		        }
		 
		        String resultString = result.toString();
		        return resultString.length() > 0
		          ? resultString.substring(0, resultString.length() - 1)
		          : resultString;
		    }
		}


}
