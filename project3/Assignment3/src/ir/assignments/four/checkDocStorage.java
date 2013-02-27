package ir.assignments.four;

import ir.assignments.three.storage.DocumentStorage;

public class checkDocStorage {

	public static void main(String[] args) {
		
		System.out.println(check("http://www.ics.uci.edu/~ziv/ooad/intro_to_se/tsld023.htm").toString());
	}
	
	public static Boolean check(String targetUrl){
		DocumentStorage docStorage = new DocumentStorage("docStorage\\docStorage");
		return docStorage.getDocument(targetUrl) != null;
	}
}
