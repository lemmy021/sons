package rs.sons.dao;

import java.io.IOException;

import rs.sons.weather.WeatherObject;

public interface WeatherDao {

	public WeatherObject getNsWeather() throws IOException;
	
	public void cacheEvict();
	
}
