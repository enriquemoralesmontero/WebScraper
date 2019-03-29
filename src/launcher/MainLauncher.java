package launcher;

import model.DataBase;
import model.Scraper;
import objects.RegistryList;

/**
 * The main class that runs the entire project.
 * 
 * @author Enrique Morales Montero
 * @version 28/3/2019
 */
public class MainLauncher {
	
	/**
	 * The website that is going to be scraped.
	 */
	public static final String webURL = "http://cnmv.es/Portal/Consultas/IFI/ListaIFI.aspx?XBRL=S";
	
	/**
	 * Main method.
	 * 
	 * @param args - They are not necessary.
	 */
	public static void main(String[] args) {

		System.out.println("Starting...\n");
		
		DataBase mysql = new DataBase();
		String lastUrlInfoContext = "NULL";
		
		if (mysql.hasRegistries()) {
			lastUrlInfoContext = mysql.lastUrlInfoContext();
			System.out.println("Last registry:\n\n\t" + lastUrlInfoContext);
		}
		
		// Web scraping.
		// The data will be stored in a list of registries.
		
		System.out.println("\nScraping...");
		
		RegistryList list = Scraper.getListOfInfoCNMV(webURL, lastUrlInfoContext);
		
		// The data extracted by the web scraper will be displayed.
		// They will be displayed by console.
		
		System.out.println("\n\nNew registries in CNMV: " + list.size() + "\n");
		
		if (list.size() > 0) {
			
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
			
			// Storage in the MySQL database.
			// The data is stored in order if it is not already in the database.
			
			System.out.println("\nInserting in the database...\n");
			mysql.store(list);
		}	
		
		System.out.println("\nFinish!");
	}
}