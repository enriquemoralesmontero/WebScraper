package launcher;

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

		System.out.println("Starting...");
		
		RegistryList list = Scraper.getListOfInfoCNMV(webURL);
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		System.out.println("Finish!");
	}
}