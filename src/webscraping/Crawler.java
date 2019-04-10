package webscraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A web crawler, sometimes called a web-spider or spiderbot.
 * It is a program that accedes to any individual link and extracts all the necessary data for each CNMV record we need to store and secure.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	28/3/2019
 * @version 9/4/2019
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
	 * The procedure that fixes the targeted HTML document.
	 * The web spider extracts data from the configured program.
	 * 
	 * @param doc - JSoup object with the web page HTML code.
	 */
	public void setDocHMTL(final Document doc) {this.docHTML = doc;}
	
	/**
	 * <p>	This function collects the information we pass as a parameter from the document.		</p>
	 * <p>	It extracts the plain text that is placed between the tags whose attribute "id" is "ID".</p>
	 * <p>	Example:																				</p>
	 * <p>		HTML 									= <tag id="Parameter">31/01/2019</tag>		</p>
	 * <p>		Results of getPlainText("Parameter") 	= "31/01/2019"								</p>
	 * 
	 * @param ID - Identifier of the searched tag (String).
	 * @return The text that is between the searched tags (String).
	 * @see Crawler#docHTML			docHTML			- HTML document.
	 * @see Crawler#getAttrValue()	getAttrValue	- Analogous method.
	 */
	public String getPlainText(final String ID) {
						
		Element element = docHTML.getElementById(ID);						// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.getElementsByAttributeValue("id", ID).text();// Getting the plain text between the searched tags.
		return value;
	}
	
	/**
	 * <p>	This function collects information from the document.												</p>
	 * <p>	It is similar to getPlainText(), but it has its own features:										</p>
	 * <p>	It extracts the value from the attribute "ATTRIBUTE" located in the HTML tag with id equals to "ID".</p>
	 * <p>	Example:																							</p>
	 * <p>		HTML 											= <tag id="Parameter" href="page.com"></tag>	</p>
	 * <p>		Results of getAttrValue("Parameter", "href") 	= "page.com"									</p>
	 * 
	 * @param ID - Searched tag identifier (String).
	 * @param ATTRIBUTE - Element the value will be collected from (String).
	 * @return The value of the attribute called "ATTRIBUTE" (String).
	 * @see Crawler#docHTML			docHTML			- HTML document.
	 * @see Crawler#getPlainText()	getPlainText	- Analogous method.
	 */
	public String getAttrValue(final String ID, final String ATTRIBUTE) {
					
		Element element = docHTML.getElementById(ID);	// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.attr(ATTRIBUTE);			// Getting the value of the attribute called "ATTRIBUTE" (ATTRIBUTE="value")...
		return value;
	}
}