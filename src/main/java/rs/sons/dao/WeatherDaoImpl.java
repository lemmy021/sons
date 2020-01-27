package rs.sons.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import rs.sons.helper.MyDateFormatter;
import rs.sons.weather.WeatherObject;

@Repository(value = "weatherDao")
public class WeatherDaoImpl implements WeatherDao {
	
	//http://api.openweathermap.org/data/2.5/weather?id=3194360&APPID=0d04354b8dbb90fa9e02f92b8fcacdbf&units=metric
	private static final String URL = "http://api.openweathermap.org/data/2.5/weather?";
	private static final String APIID = "0d04354b8dbb90fa9e02f92b8fcacdbf";
	private static final String UNITS = "metric";
	private static final String CITY_ID = "3194360";

	
	@Cacheable("weather")
	public WeatherObject getNsWeather() throws IOException {
		return getWeatherHttpRequest();
	}
	
	@CacheEvict(allEntries = true, cacheNames = { "weather" })
	@Scheduled(fixedDelay = 10 * 60 * 1000)
	public void cacheEvict() {
		System.out.println("odradio vremenski skedjuler");
	}
	
	private WeatherObject getWeatherHttpRequest() throws IOException {
		System.out.println("2222222222222222222222222222");
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
		
		WeatherObject weatherObject;
		
		if(con.getResponseCode() == 200) {
		
			//https://stackoverflow.com/questions/8233542/parse-a-nested-json-using-gson
			JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
			
			//result.put("temp", jsonObject.getAsJsonObject().get("main").getAsJsonObject().get("temp").toString());
			//result.put("icon", jsonObject.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").toString());
			//result.put("last_update", MyDateFormatter.unixTimestampToDate(jsonObject.getAsJsonObject().get("dt").getAsLong()));
			
			weatherObject = new WeatherObject(MyDateFormatter.unixTimestampToDate(jsonObject.getAsJsonObject().get("dt").getAsLong()), jsonObject.getAsJsonObject().get("wind").getAsJsonObject().get("speed").toString(), MyDateFormatter.unixTimestampToDate(jsonObject.getAsJsonObject().get("sys").getAsJsonObject().get("sunrise").getAsLong()), MyDateFormatter.unixTimestampToDate(jsonObject.getAsJsonObject().get("sys").getAsJsonObject().get("sunset").getAsLong()), jsonObject.getAsJsonObject().get("main").getAsJsonObject().get("temp").toString(), jsonObject.getAsJsonObject().get("main").getAsJsonObject().get("humidity").toString(), false);
			
		} else {
			weatherObject = new WeatherObject(true);
		}
		
		con.disconnect();
		
		return weatherObject;
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
