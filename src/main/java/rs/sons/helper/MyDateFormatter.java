package rs.sons.helper;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateFormatter {

	private static final String ZERO_DATE = "1900-01-01 00:00:00";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	private static SimpleDateFormat deadlineDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
	
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
	
	/**
	 * add 7 days on current date
	 * @return
	 */
	public static Date paymentDeadline() {
		Date dateNow = new Date();
		Date sevenDays = new Date(dateNow.getTime() + (1000 * 60 * 60 * 24 * 7));
		
		try {
			return deadlineDateFormat.parse(deadlineDateFormat.format(sevenDays));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String formatDate(Date date) {
		Format formatter = new SimpleDateFormat("dd.MM.yyyy.");
		
		return  formatter.format(date);
	}
}
