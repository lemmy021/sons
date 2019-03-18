package rs.sons.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateFormatter {

	private static final String ZERO_DATE = "1900-01-01 00:00:00";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	
	public static Date setZeroDate() {
		
		//dateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		
		try {
			return dateFormat.parse(ZERO_DATE);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date setCurrentDatetime() {
		Date dateNow = new Date();
		
		try {
			return dateFormat.parse(dateFormat.format(dateNow));
		} catch (ParseException e) {
			return null;
		}
	}
}
