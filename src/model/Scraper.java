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
		
	private static Document doc;
	
	/**
	 * 
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 */
	public static RegistryList getListOfInfoCNMV(final String webURL) {
		
		RegistryList list = new RegistryList();				// List of scraped data.
		
		if (getStatusConnectionCode(webURL) == 200) {		// It checks if the code is 200 when making the request.
			
            Document document = getHtmlDocument(webURL); 	// The HTML of the web is obtained in a Document object.
			
            // Searching for data...
            		
            Elements entradas = document.select("tr"); 
            System.out.println("Elements: " + entradas.size() + " registries.\n");
            
            // Loop of elements.            
            
            for (int i = 1; i < entradas.size(); i++) {
            	
            	System.out.print("\n\t" + i + "/" + entradas.size());
    			Element element = entradas.get(i);

    			// Getting data.
    			
    			Element link = element.select("a").first();
    			
    			String url_info_context = link.attr("href").replace("../..", "http://cnmv.es/Portal");
    			String entityName = element.getElementsByAttributeValue("data-th", "Nombre del emisor").text();
    			
    			doc = getHtmlDocument(url_info_context);
    			
    			String url_ixbrl = crawlerGetAttrValue("ctl00_ContentPrincipal_ctl11_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    			String entityCode = crawlerGetPlainText("ctl00_ContentPrincipal_ctl10_lblNIFCont");  			
    			String period_end = crawlerGetPlainText("ctl00_ContentPrincipal_ctl10_lblFinPeriodoCont");
    			String form = crawlerGetPlainText("ctl00_ContentPrincipal_ctl11_txtInfoXBRL");
    			String format = "XBRL";
    			String hash_code = "";
    			String oam = "CNMV";
    			String country = "ES";
    			
    			// Some entities have made modifications to the model previously registered with the CNMV.
    			// It must be controlled.
    			
    			if (url_ixbrl == "NULL") {
    				System.out.print(" .");
    				url_ixbrl = crawlerGetAttrValue("ctl00_ContentPrincipal_ctl12_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    				entityCode = crawlerGetPlainText("ctl00_ContentPrincipal_ctl11_lblNIFCont");
    				period_end = crawlerGetPlainText("ctl00_ContentPrincipal_ctl11_lblFinPeriodoCont");
    				form = crawlerGetPlainText("ctl00_ContentPrincipal_ctl12_txtInfoXBRL");
    			}
    			
    			if (url_ixbrl == "NULL") {
    				System.out.print(".");
    				url_ixbrl = crawlerGetAttrValue("ctl00_ContentPrincipal_ctl13_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    				entityCode = crawlerGetPlainText("ctl00_ContentPrincipal_ctl12_lblNIFCont");
    				period_end = crawlerGetPlainText("ctl00_ContentPrincipal_ctl12_lblFinPeriodoCont");
    				form = crawlerGetPlainText("ctl00_ContentPrincipal_ctl13_txtInfoXBRL");
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
			
	    Response response = null;
		
	    try {
	    	response = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    } catch (IOException ex) {
	    	System.err.println("Exception when getting the Status Code: " + ex.getMessage());
	    }
	    
	    return response.statusCode();
	}
	
	/**
	 * Method that returns a Document with the HTML content of the web.
	 * It allows the application of JSoup methods on it.
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
	
	/**
	 * Function that is entered in another link to collect more information.
	 * 
	 * @param webURL - New URL (String)
	 * @param ID - Identifier of the searched tag (String).
	 * @param ELEMENT - Element from which the value will be collected (String).
	 * @return The value of the parameter called "ELEMENT" (String).
	 */
	private static String crawlerGetAttrValue(String ID, String ELEMENT) {
					
		Element element = doc.getElementById(ID);
		
		if (element == null) {return "NULL";}
		
		String value = element.attr(ELEMENT);
		return value;
	}
	
	/*
	 * Function that is entered in another link to collect more information.
	 * 
	 * @param webURL - New URL (String)
	 * @param ID - Identifier of the searched tag (String).
	 * @return The value of the parameter called "ID" (String).
	 */
	private static String crawlerGetPlainText(String ID) {
						
		Element element = doc.getElementById(ID);
		
		if (element == null) {return "NULL";}
		
		String value = element.getElementsByAttributeValue("id", ID).text();
		return value;
	}
	
	
}

