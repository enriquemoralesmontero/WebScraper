package webscraping;

import static log.LogManager.writeExceptionInLog;
import static cryptography.Algorithm.generateSHA3_256;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <p>The class that collects the information from the requested web page.
 * This program has to accede to two levels of a HyperText document.</p>
 * 
 * <p>In the main website, we have the summarized information for each company quarterly report.
 * Anytime a report is given to the CNMV, the CNMV website manager adds this new report to his website.
 * This is an individual record we partially store in the RegistryCNMV object.</p>
 * 
 * <p>Each record in the HTML information has four fields and a link to the full report.
 * This new page contains all the information needed about this quarterly report.
 * Using the full report, we complete the data in the RegistryCNMV.
 * Therefore, the object is entirely filled in, and it is ready to be stored.</p>
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * 
 * @since 	29/3/2019
 * @version 9/4/2019
 * 
 * @see RegistryCNMV RegistryCNMV object
 */
public class Scraper {
	
	/**
	 * This is the main method in the scraper.   
	 * It follows these steps:
	 * 
	 * <ol>
	 * <li>	Instance a list.													</li>
	 * <li>	Check that the connection code to the web page is correct. (200).	</li>
	 * <li>	Get the HTML document.												</li>
	 * <li>	Treats the rows of the HTML table.									</li>
	 * <li>	It goes on getting and instance of a web crawler to enter the hyperlinks.</li>
	 * <li>	It goes through the CNMV list to extract data from the rows and store them in the list.</li>
	 * </ol>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @param lastUrlInfoContext 
	 * 
	 * @return An ArrayList<RegistryCNMV> (a list of all the extracted data).
	 * 
	 * @see Scraper#getStatusConnectionCode() getStatusConnectionCode() - Method to check the connection code.
	 * @see <a href="http://cnmv.es/Portal/Consultas/IFI/ListaIFI.aspx?XBRL=S">CNMV website</a>
	 */
	public static ArrayList<RegistryCNMV> getListOfInfoCNMV(final String webURL, final String lastUrlInfoContext) {
		
		// 1 - Instanced list.
		
		ArrayList<RegistryCNMV> list = new ArrayList<RegistryCNMV>();		// List of scraped data.
			
		
		// 2 - Checking the connection code.
		//
		// It checks if the code is 200 when making the request.
		// If there is a bad connection to the website, it will give a code other than 200 (400, 404...)
		
		System.out.println("\n\tThis task may take a while.");
		
		if (getStatusConnectionCode(webURL) == 200) {		

			
			// 3 - Getting the HTML document.
			//
			// The HTML of the web is obtained in a Document object.
			
			Document document = getHtmlDocument(webURL); 	

			
			// 4 - Treats the rows of the HTML table.
			//
			// Searching for data...
            		
            Elements rows = document.select("tr"); 	// Rows obtained.
            System.out.println("\n\tElements: " + rows.size() + " registries.");
            
            
            // 5 - Instantiate the web crawler.
            //
            // The web spider gets into each individual hypertext links with the full information associated with this CNMV company quarter economic information.
            // This step completes the data for each registry recorded in the collection.
            
            Crawler spiderBot = new Crawler();
            
            
            // 6 - Loop to extract data from the rows.
            //
            // Data is retrieved from the web within the JSoup's sentences.
            // It is not necessary to control all the rows.
            // The loop finishes as soon as the record presently extracted matches the most recent record in the database.
            // Finally, are program stores each new gathered record in an ArrayList.
            
            for (int i = 1; i < rows.size(); i++) {
            	
            	System.out.print("\n\t\t- Scanning... " + i + "/" + rows.size());
    			
            	Element element = rows.get(i);						// Each element contains the row of the HTML table with its HTML information.
    			
    			Element hyperlink = element.select("a").first();	// It contains the url_info_context. The spider will use it later.
    			
    			// JSoup data extraction:
    			//	- url_info_context
    			//	- entityName
    			
    			String url_info_context = hyperlink.attr("href").replace("../..", "http://cnmv.es/Portal");
    			String entityName = element.getElementsByAttributeValue("data-th", "Nombre del emisor").text();
    			
    			System.out.print("\t" + entityName);
    			
    			// Improved performance control:
    			//		If the last gathered information matches with the most recent record in the database, it gets out of the loop.
    			//		As it does so, it is not mandatory to have full awareness of changing and new records of the CNMV's web.
    			
    			if (url_info_context.equals(lastUrlInfoContext)) {
    				System.out.println("\n\t\t\t\t\t- This registry (" + i + "/" + rows.size() + ") matches the last one in the database.");
    				System.out.println("\t\t\t\t\t- Finishing the scraping...");
    				break;
    			}
    			
    			// It requests the spiderbot to deal with a singular web link and retrieve the full information we need for each CNMV entry.
    			
    			spiderBot.setDocHMTL(getHtmlDocument(url_info_context));
    			
    			// The spider extracts the following data fields::
    			//	- url_ixbrl
    			//	- entityCode
    			//	- period_end
    			//	- form
    			
    			String url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl11_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    			String entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblNIFCont");  			
    			String period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblFinPeriodoCont");
    			String form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			
    			// Other data:
    			//	- format
    			//	- oam
    			//	- country
    			
    			String format = "XBRL";
    			String oam = "CNMV";
    			String country = "ES";
    			
    			// Some entities that are already stored in the database could be modified lately due to some changes in quarterly company reports.
                // Consequently, our program needs to update these changes in our database.
    			// It rarely happens, but it could occur occasionally.
                // We need to control these changes.
    			
    			for (int j = 11; j < 20 && url_ixbrl == "NULL"; j++) {

    				System.err.print(" (with modifications)");	// A text is shown to indicate that this registry was modified by the entity.
					url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl" + String.valueOf(j + 1) + "_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
					entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl" + String.valueOf(j) + "_lblNIFCont");
					period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl" + String.valueOf(j) + "_lblFinPeriodoCont");
					form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl" + String.valueOf(j + 1) + "_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
				}
    			
    			// The last data:
    			//	- hash_code
    			
    			String hash_code = generateSHA3_256(url_ixbrl);
    			
    		    // Storing data in a list.
    		    
    		    RegistryCNMV infoCNMV = new RegistryCNMV(url_ixbrl, url_info_context, entityName, entityCode, period_end, form, format, hash_code, oam, country);
    		    list.add(infoCNMV);
    		}
            
        } else {
        	int errorCode = getStatusConnectionCode(webURL);        	
        	System.err.println("Critical status code: " + errorCode + " - " + getMessageFromCode(errorCode));
        	System.err.println("The webpage (" + webURL + ") cannot be accessed...");
        	writeExceptionInLog(new Exception(), "The webpage (" + webURL + ") cannot be accessed...", "Scraper.getListOfInfoCNMV()");
        	System.exit(1);
        }
		
		return list;
	}
		
	/**
	 * We need to check the website availability when we connect to it.
	 * This function gets the status code of the response.
	 * 
	 * <ul>
	 * 		<li>	200 = OK.						</li>
	 * 		<li>	300 = Multiple choices.			</li>
	 * 		<li>	301 = Moved permanently.		</li>
	 * 		<li>	305 = Use proxy.				</li>
	 * 		<li>	400 = Bad request.				</li>
	 * 		<li>	403 = Forbidden.				</li>
	 * 		<li>	404 = Not found.				</li>
	 * 		<li>	500 = Internal server error.	</li>
	 * 		<li>	502 = Bad gateway.				</li>
	 * 		<li>	503 = Service unavailable.		</li>
	 * </ul>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Status Code (int).
	 * 
	 * @throws IOException
	 */
	private static int getStatusConnectionCode(final String webURL) {
		
	    Response response = null;
	    
	    try {
	    	
	    	response = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    	
	    } catch (IOException ex) {
	    	System.err.println("Exception when getting the status code: " + ex.getMessage());
	    	writeExceptionInLog(ex, "IO exception when getting the status code...", "Scraper.getStatusConnectionCode()");
	    }
	    
	    return response.statusCode();
	}
	
	/**
	 * Returns the meaning of the connection code.
	 * 
	 * @param connectionCode (int)
	 * 
	 * @return The meaning of the connection code (String).
	 * 
	 * @throws IOException
	 */
	private static String getMessageFromCode(int connectionCode) {
		
		switch (connectionCode) {
		case 300:
			return "Multiple choices";
		case 301:
			return "Moved permanently";
		case 305:
			return "Use proxy";
		case 400:
			return "Bad request";
		case 403:
			return "Forbidden";
		case 404:
			return "Not found";
		case 500:
			return "Internal server error";
		case 502:
			return "Bad gateway";
		case 503:
			return "Service unavailable";
		default:
			return "Unknown error code";
		}
	}
	
	/**
	 * The method that returns a Document with the HTML content of the web.
	 * It allows the application of JSoup methods on it.
	 * Attention! If in a hundred seconds HTML is not detected, the connection must be checked!
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Document HTML.
	 * 
	 * @throws IOException
	 */
	private static Document getHtmlDocument(final String webURL) {

	    Document doc = null;
	    
		try {
		    doc = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.err.println("IO exception when getting the HTML of the page: " + ex.getMessage());
			writeExceptionInLog(ex, "IO exception when getting the HTML of the page...", "Scraper.getHtmlDocument()");
			System.exit(1);
		}
		
	    return doc;
	}
	
}