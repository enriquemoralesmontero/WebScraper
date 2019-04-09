package log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The class that captures exceptions and errors.
 * It saves them in a log file.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	5/4/2019
 * @version	8/4/2019
 */
public class LogManager {
	
	private static final String FILE_PATH = "errors.log";
	
	/**
	 * The procedure that creates the file where the log is stored if it does not exist.
	 * 
	 * @return File
	 * @throws IOException
	 */
	private static File getFileLog() throws IOException {
		
		File file = new File(FILE_PATH);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		return file;
	}
	
	/**
	 * The public procedure that stores the following data in the file:
	 * <ul>
	 * <li>		Day, month and year.							</li>
	 * <li>		Hour, minutes and seconds.						</li>
	 * <li>		Type of exception.								</li>
	 * <li>		Location of the code that causes the exception.	</li>
	 * </ul>
	 */
	public static void writeLog(Exception exception, String message) {

		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileLog(), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println();
			printWriter.println("EXCEPTION!");
			printWriter.println("Date: " + thisDate.getDayMonthYear() + ".");
			printWriter.println("Hour: " + thisDate.getClockTime() + ".");
			printWriter.println("Type: " + exception.getClass().getSimpleName() + ".");
			printWriter.println("Desc: " + message);
			printWriter.println(exception.getCause().getLocalizedMessage());
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
}