package rs.sons.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.WeatherDao;
import rs.sons.weather.WeatherObject;

@Service("weatherService")
public class WeatherServiceImpl implements WeatherService {
	
	@Autowired
	WeatherDao weatherDao;

	@Override
	public WeatherObject getNsWeather() throws IOException {
		return weatherDao.getNsWeather();
	}

}
