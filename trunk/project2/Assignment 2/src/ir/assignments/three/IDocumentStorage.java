package ir.assignments.three;

public interface IDocumentStorage {
	
	void storeDocument(String url, String text);	
	String getDocument(String url);
}
