package ir.assignments.three;

import java.util.HashMap;

public class MemoryDocumentStorage implements IDocumentStorage {
	private HashMap<String, String> urlPageDictionary;
	
	public MemoryDocumentStorage(HashMap<String, String> urlPageDictionary) {
		this.urlPageDictionary = urlPageDictionary;
	}
	
	public void storeDocument(String url, String text) {
		//TODO
	}
	
	public String getDocument(String url) {
		//TODO
		return null;
	}
}
