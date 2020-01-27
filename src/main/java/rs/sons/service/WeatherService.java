package rs.sons.service;

import java.io.IOException;

import rs.sons.weather.WeatherObject;

public interface WeatherService {

	public WeatherObject getNsWeather() throws IOException;
}
