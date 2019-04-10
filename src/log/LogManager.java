package log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import webscraping.RegistryCNMV;

/**
 * The class that captures exceptions and errors.
 * It saves them in a log file.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	5/4/2019
 * @version	9/4/2019
 */
public class LogManager {
	
	private static final String FILE_PATH = "ScrapingHistory.log";
	private static final String EXCEPTIONS_FILE_PATH = "ErrorHistory.log";
	
	/**
	 * The procedure that creates the file where the log is stored if it does not exist.
	 * 
	 * @return File
	 * @throws IOException
	 * @see LogManager#writeAttentionInLog() writeLog() - Method that uses this function.
	 */
	private static File getFileLog() throws IOException {
		
		File file = new File(FILE_PATH);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		return file;
	}
	
	/**
	 * The procedure that creates the file where the log is stored if it does not exist.
	 * 
	 * @return File
	 * @throws IOException
	 * @see LogManager#writeAttentionInLog() writeLog() - Method that uses this function.
	 */
	private static File getFileExceptionsLog() throws IOException {
		
		File file = new File(EXCEPTIONS_FILE_PATH);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		return file;
	}
	
	/**
	 * A public procedure that stores the following data in the file:
	 * <ul>
	 * <li>		Day, month and year.							</li>
	 * <li>		Hour, minutes and seconds.						</li>
	 * <li>		Type of exception.								</li>
	 * <li>		Location of the code that causes the exception.	</li>
	 * </ul>
	 */
	public static void writeExceptionInLog(Exception exception, String message, String classAndMethod) {

		writeAttentionInLog();
		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileExceptionsLog(), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println("EXCEPTION!");
			printWriter.println("Date: " + thisDate.getDayMonthYear());
			printWriter.println("Hour: " + thisDate.getClockTime());
			printWriter.println("Loc.: " + classAndMethod);
			printWriter.println("Type: " + exception.getClass().getSimpleName() + ".");
			printWriter.println("Info: " + message);
			try {
				printWriter.println(exception.getCause().getLocalizedMessage());
			} catch (NullPointerException e) {}
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * A public procedure that stores the following data in the file:
	 * <ul>
	 * <li>		Day, month and year.							</li>
	 * <li>		Hour, minutes and seconds.						</li>
	 * <li>		Number of new registries.						</li>	
	 * <li>		List of new entityNames inserted.				</li>
	 * </ul>
	 */
	public static void writeListInLog(ArrayList<RegistryCNMV> list) {
		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileLog(), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println("NEW SCRAPING ACTION!");
			printWriter.println("Date: " + thisDate.getDayMonthYear());
			printWriter.println("Hour: " + thisDate.getClockTime());
			printWriter.println("New registries: " + list.size() + ".");
			
			if (list.size() > 0) {
				
				for (RegistryCNMV registryCNMV : list) {
					printWriter.println("\t- " + registryCNMV.getEntity_name());
				}
				
			}
			
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	/**
	 * A public procedure that stores the following data in the file:
	 * <ul>
	 * <li>		Error message.									</li>
	 * <li>		Day, month and year.							</li>
	 * <li>		Hour, minutes and seconds.						</li>
	 * </ul>
	 */
	public static void writeAttentionInLog() {
		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileLog(), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println("ATTENTION!");
			printWriter.println("NEW SCRAPING ACTION WITH ERRORS!");
			printWriter.println("Date: " + thisDate.getDayMonthYear());
			printWriter.println("Hour: " + thisDate.getClockTime());			
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
}