package exceptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogDate extends Date {

	private static final long serialVersionUID = 1L;

	public LogDate() {super();}
	
	/**
	 * @return String with the day of the month, the month and the year.
	 */
	public String getDayMonthYear() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		return dateFormat.format(this);
	}
	
	/**
	 * @return String with the hours, minutes and seconds.
	 */
	public String getClockTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(this);
	}
	
}