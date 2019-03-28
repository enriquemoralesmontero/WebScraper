import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Prueba {

	public static void main(String[] args) {
		
		//Document document = getHtmlDocument("http://cnmv.es/Portal/AlDia/DetalleIFIAlDia.aspx?nreg=2019034054"); 	// The HTML of the web is obtained in a Document object.
		Document document = getHtmlDocument("http://cnmv.es/Portal/AlDia/DetalleIFIAlDia.aspx?nreg=2019026879");
		Element element = document.getElementById("ctl00_ContentPrincipal_ctl11_hlDescargaInforme");
		
		String url_ixbrl = element.attr("href");

		System.out.println(url_ixbrl);
		
		/*Element link = entradas.getElementById("ctl00_ContentPrincipal_ctl11_hlDescargaInforme");
		String url_ixbrl = link.attr("href");

		System.out.println(url_ixbrl);*/
		
		//System.out.println(link.toString());
		
			
	}
	
	
	
	
	
	/*
	 * Method that returns a Document with the HTML content of the web.
	 * It allows the application of JSoup methods on it.
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Document HTML.
	 */
	public static Document getHtmlDocument(final String webURL) {

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