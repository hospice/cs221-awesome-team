package ir.assignments.three;

import java.util.HashMap;

public class MemoryDocumentStorage implements IDocumentStorage {
	private HashMap<String, String> urlPageDictionary = new HashMap<String, String>();	
	
	public void storeDocument(String url, String text) {
		this.urlPageDictionary.put(url, text);
	}
	
	public String getDocument(String url) {
		return this.urlPageDictionary.get(url);
	}
}
