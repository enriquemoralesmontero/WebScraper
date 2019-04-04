package exceptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ScraperException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String FILE_PATH = "errors.log";
	private File file;
	private Exception exception;

	/**
	 * Constructor.
	 * 
	 * @param exceptionParam
	 */
	public ScraperException (final Exception exceptionParam) {
		
		super();
		exception = exceptionParam;
		
		createLog();
		writeLog();
	}

	/**
	 * Procedure that creates the file where the log is stored if it does not exist.
	 */
	public void createLog() {
		
		try {
			this.file = new File(FILE_PATH);
			
			if (!file.exists()) {file.createNewFile();}
			
		} catch (IOException e) {e.printStackTrace();}

	}

	/**
	 * Procedure that stores the following data in the file:
	 * <ul>
	 * <li>		Day, month and year.							</li>
	 * <li>		Hour, minutes and seconds.						</li>
	 * <li>		Type of exception.								</li>
	 * <li>		Location of the code that causes the exception.	</li>
	 * </ul>
	 */
	public void writeLog() {

		final LogDate thisDate = new LogDate();
		
		try {
			
			FileWriter fileWriter = new FileWriter(this.file, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println();
			printWriter.println("EXCEPTION!");
			printWriter.println("Date: " + thisDate.getDayMonthYear() + ".");
			printWriter.println("Hour: " + thisDate.getClockTime() + ".");
			printWriter.println("Type: " + this.exception.getClass().getSimpleName() + ".");
			printWriter.println();
			printWriter.println(this.exception.getCause().getLocalizedMessage());
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}

}