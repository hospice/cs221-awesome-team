package ir.assignments.three;

public interface IDocumentStorage {
	
	void storeDocument(String url, String text);	
	HtmlDocument getDocument(String url);
}
