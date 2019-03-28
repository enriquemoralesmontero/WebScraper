package model;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class ThreadSpider extends Thread {
	
	private boolean running;
	private String value;
	private String webURL;
	private String ID;
	private String ELEMENT;
	private Document document;
	private Element element; 
	
	public ThreadSpider() {}
	
	public void values(String webURL, String ID, String ELEMENT) {
		this.webURL = webURL;
		this.ID = ID;
		this.ELEMENT = ELEMENT;
	}
	
	@Override
	public void run() {
		
        running = true;
        
        while (running) {
        	try {
        		document = getHtmlDocument(webURL);
            	element = document.getElementById(ID);
            	value = element.attr(ELEMENT);
			} catch (Exception e) {
            	value = "ERROR";
			}
        }
    }
	
	public void kill() {this.running = false;}
	public String getValue() {return value;}
	
	/*
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
	
}
