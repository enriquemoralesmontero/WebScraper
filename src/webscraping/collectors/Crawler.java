package webscraping.collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A web crawler, sometimes called a web-spider or spiderbot, is a program that
 * accedes to any individual link and extracts all the necessary data 
 * for each cnmv record we need to store and secure.
 * 
 * @author Enrique Morales Montero
 * @since 28/3/2019
 * @version 3/4/2019
 */
public class Crawler {
	
	// Fields.
	
	/**
	 * JSoup object with the HTML of the web page.
	 * The web spider will extract data from this web page.
	 */
	private Document docHTML;
	
	// Constructor.
	
	/**
	 * Simple constructor.
	 * It is only used to instantiate the object.
	 */
	public Crawler() {}
	
	// Setters and getters.
	
	/**
	 * Procedure that fixes the targeted HTML document.
	 * The web spider  extracts data from the configured program.
	 * 
	 * @param doc - JSoup object with the HTML of the web page.
	 */
	public void setDocHMTL(final Document doc) {this.docHTML = doc;}
	
	/*
	 * <p> This	function collects the information we pass as a parameter from the document.										</p>
	 * <p>	It extracts the plain text that is placed between the tags whose attribute "id" is "ID".</p>
	 * <p>	Example:																				</p>
	 * <p>		HTML 									= <tag id="Parameter">31/01/2019</tag>		</p>
	 * <p>		Results of getPlainText("Parameter") 	= "31/01/2019"								</p>
	 * 
	 * @param ID -  HTML tag that we target (String).
	 * @return String. The tag inner text (INNERHTML)  (String).
	 */
	public String getPlainText(final String ID) {
						
		Element element = docHTML.getElementById(ID);						// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.getElementsByAttributeValue("id", ID).text();// Getting the plain text between the searched tags.
		return value;
	}
	
	/**
	 * <p>	This method collects information from the document.													</p>
	 * <p>	It is similar to getPlainText(), but it has its own features:										</p>
	 * <p>	It extracts the value from the attribute "ATTRIBUTE" located in the HTML tag with id equals to "ID".				</p>
	 * <p>	Example:																							</p>
	 * <p>		HTML 											= <tag id="Parameter" href="page.com"></tag>	</p>
	 * <p>		Results of getAttrValue("Parameter", "href") 	= "page.com"									</p>
	 * 
	 * @param ID - Tag Identifier  (String).
	 * @param ATTRIBUTE - Targeted attribute inside the HTML tag (String).
	 * @return String  HTML tag "ATTRIBUTE" value (String).
	 */
	public String getAttrValue(final String ID, final String ATTRIBUTE) {
					
		Element element = docHTML.getElementById(ID);	// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.attr(ATTRIBUTE);			// Getting the value of the attribute called "ATTRIBUTE" (ATTRIBUTE="value")...
		return value;
	}
}