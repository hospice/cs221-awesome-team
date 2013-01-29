package ir.assignments.three;

import java.util.HashMap;

public class MemoryDocumentStorage implements IDocumentStorage {
	private HashMap<String, HtmlDocument> urlPageDictionary = new HashMap<String, HtmlDocument>();	
	
	public void storeDocument(String url, String text) {
		this.urlPageDictionary.put(url, new HtmlDocument(text));
	}
	
	public HtmlDocument getDocument(String url) {
		return this.urlPageDictionary.get(url);
	}
}
