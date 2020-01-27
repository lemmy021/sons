package rs.sons.weather;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherObject {

	private String last_update;
	private String wind;
	private String sunrise;
	private String sunset;
	private String temp;
	private String humidity;
	private boolean error;
	
	public WeatherObject(String last_update, String wind, String sunrise, String sunset, String temp, String humidity, boolean error) {
		super();
		this.last_update = last_update;
		this.wind = wind;
		this.sunrise = sunrise;
		this.sunset = sunset;
		this.temp = temp;
		this.humidity = humidity;
		this.error = error;
	}

	public WeatherObject(boolean error) {
		super();
		this.error = error;
	}
}
