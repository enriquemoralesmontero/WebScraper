package errorcontrol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	5/4/2019
 * @version	5/4/2019
 */
public class LogDate extends Date {

	private static final long serialVersionUID = 1L;

	public LogDate() {super();}
	
	/**
	 * @return A string with the day of the month, the month and the year.
	 */
	public String getDayMonthYear() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		return dateFormat.format(this);
	}
	
	/**
	 * @return A string with the hours, minutes and seconds.
	 */
	public String getClockTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(this);
	}
	
}