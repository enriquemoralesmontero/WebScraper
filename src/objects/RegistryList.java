package objects;

import java.util.ArrayList;

public class RegistryList {
	
	private ArrayList<InfoCNMV> list;
	
	/**
	 * Constructor.
	 * 
	 * @param list - ArrayList of InfoCNMV.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RegistryList() {
		list = new ArrayList();
	}
	
	public void add(InfoCNMV infoCNMV) {
		list.add(infoCNMV);
	}
	
	public InfoCNMV get(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
}