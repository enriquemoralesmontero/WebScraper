package objects;

/**
 * Class with the main data collected from the website.
 * 
 * @author Enrique Morales Montero
 * @version 28/3/2019
 */
public class RegistryCNMV {
	
	private String url_ixbrl;
	
	public RegistryCNMV(String url_ixbrl) {
		this.url_ixbrl = url_ixbrl;
	}

	public String getUrl_ixbrl() {return url_ixbrl;}

	public void setUrl_ixbrl(String url_ixbrl) {this.url_ixbrl = url_ixbrl;}
		
}