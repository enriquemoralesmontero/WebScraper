package model;

import java.io.IOException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import objects.InfoCNMV;
import objects.RegistryList;

/**
 * The class that collects the information from the requested web page.
 * 
 * @author Enrique Morales Montero
 * @version 28/3/2019
 */
public class Scraper {
	
	/**
	 * This is the most important function of the scraper.
	 * It is responsible for the following features:
	 * 
	 * <p>	1 - Instance a list.													</p>
	 * <p>	2 - Check that the connection code to the web page is correct (200).	</p>
	 * <p>	3 - Get the HTML document.												</p>
	 * <p>	4 - Treats the rows of the HTML table.									</p>
	 * <p>	5 - Instance a web crawler to enter the hyperlinks.						</p>
	 * <p>	6 - Loop to extract data from the rows.									</p>
	 * <p>		</p>
	 * <p>		</p>
	 * <p>		</p>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @param lastUrlInfoContext 
	 */
	public static RegistryList getListOfInfoCNMV(final String webURL, final String lastUrlInfoContext) {
		
		// 1 - Instanced list.
		
		RegistryList list = new RegistryList();		// List of scraped data.
		
		// 2 - Checking the connection code.
		// It checks if the code is 200 when making the request.
		// If there is a bad connection to the website, it will give a code other than 200 (400, 404...)
		
		System.out.println("\n\tThis task may take a while.");
		
		if (getStatusConnectionCode(webURL) == 200) {		

			// 3 - Getting the HTML document.
			// The HTML of the web is obtained in a Document object.
			
			Document document = getHtmlDocument(webURL); 	

			// 4 - Treats the rows of the HTML table.
			// First essential work of web scraping!
			// The first elements to be treated are the rows of the HTML table of the web page.
			// Searching for data...
            		
            Elements rows = document.select("tr"); 	// Rows obtained.
            System.out.println("\n\tElements: " + rows.size() + " registries.");
            
            // 5 - Instantiate the web crawler.
            // The web spider will be used to enter the hypertext links.
            // This will continue the data collection within them.
            
            Crawler spiderBot = new Crawler();
            
            // 6 - Loop to extract data from the rows.
            // For each column taken, data is collected.
            // It is not necessary to control all the rows.
            // The loop will be exited if the records found have already been previously inserted in the database.
            
            for (int i = 1; i < rows.size(); i++) {
            	
            	System.out.print("\n\t\t- Scanning... " + i + "/" + rows.size());
    			Element element = rows.get(i);

    			// Getting data.
    			
    			Element link = element.select("a").first();
    			
    			String url_info_context = link.attr("href").replace("../..", "http://cnmv.es/Portal");
    			String entityName = element.getElementsByAttributeValue("data-th", "Nombre del emisor").text();
    			
    			if (url_info_context.equals(lastUrlInfoContext)) {
    				System.out.println(" [This record matches the last one in the database] - Finishing the scraping...");
    				break;
    			}
    			
    			spiderBot.setDocHMTL(getHtmlDocument(url_info_context));
    			
    			String url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl11_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    			String entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblNIFCont");  			
    			String period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblFinPeriodoCont");
    			String form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			String format = "XBRL";
    			String hash_code = "";
    			String oam = "CNMV";
    			String country = "ES";
    			
    			// Some entities have made modifications to the model previously registered with the CNMV.
    			// It must be controlled.
    			
    			if (url_ixbrl == "NULL") {
    				System.out.print(" .");
    				url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl12_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    				entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_lblNIFCont");
    				period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_lblFinPeriodoCont");
    				form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl12_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			}
    			
    			if (url_ixbrl == "NULL") {
    				System.out.print(".");
    				url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl13_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    				entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl12_lblNIFCont");
    				period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl12_lblFinPeriodoCont");
    				form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl13_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			}
    			
    		    // Storing data in a list.
    		    
    		    InfoCNMV infoCNMV = new InfoCNMV(url_ixbrl, url_info_context, entityName, entityCode, period_end, form, format, hash_code, oam, country);
    		    list.add(infoCNMV);
    		}
            
        } else {
        	System.err.println("Critical status code: " + getStatusConnectionCode(webURL));
        	System.exit(1);
        }
		
		return list;
	}
		
	/**
	 * Get the Status code of the response.
	 * 
	 * 		<p>	200 = OK.				</p><p>	300 = Multiple Choices.			</p>
	 * 		<p>	301 = Moved Permanently.</p><p>	305 = Use Proxy.				</p>
	 * 		<p>	400 = Bad Request.		</p><p>	403 = Forbidden.				</p>
	 * 		<p>	404 = Not Found.		</p><p>	500 = Internal Server Error.	</p>
	 * 		<p>	502 = Bad Gateway.		</p><p>	503 = Service Unavailable.		</p>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Status Code (int).
	 */
	private static int getStatusConnectionCode(final String webURL) {
		//System.out.println("01");
	    Response response = null;
	    //System.out.println("02");
	    try {
	    	//System.out.println("03");
	    	response = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    	//System.out.println("04");
	    } catch (IOException ex) {
	    	System.err.println("Exception when getting the Status Code: " + ex.getMessage());
	    }
	    //System.out.println("1 sale del getCode");
	    return response.statusCode();
	}
	
	/**
	 * Method that returns a Document with the HTML content of the web.
	 * It allows the application of JSoup methods on it.
	 * Attention! If in a hundred seconds HTML is not detected, the connection must be checked!
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Document HTML.
	 */
	private static Document getHtmlDocument(final String webURL) {

	    Document doc = null;
	    
		try {
		    doc = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.err.println("Exception when getting the HTML of the page: " + ex.getMessage());
			System.exit(1);
		}
		
	    return doc;
	}
	
}

