package model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Crawler {
	
	private Document docHTML;
	
	public Crawler() {}
	
	public void setDocHMTL(Document doc) {this.docHTML = doc;}
	
	/**
	 * Function that is entered in another link to collect more information.
	 * 
	 * @param webURL - New URL (String)
	 * @param ID - Identifier of the searched tag (String).
	 * @param ELEMENT - Element from which the value will be collected (String).
	 * @return The value of the parameter called "ELEMENT" (String).
	 */
	public String getAttrValue(String ID, String ELEMENT) {
					
		Element element = docHTML.getElementById(ID);
		
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
	public String getPlainText(String ID) {
						
		Element element = docHTML.getElementById(ID);
		
		if (element == null) {return "NULL";}
		
		String value = element.getElementsByAttributeValue("id", ID).text();
		return value;
	}
}