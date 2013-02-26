package ir.assignments.four;

import ir.assignments.three.helpers.URLHelper;
import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.IDocumentStorage;

public class checkDocStorage {

	public static Boolean check(String Oracleurl){
		DocumentStorage docStorage = new DocumentStorage("docStorage\\docStorage");

		for (String url : docStorage.getCrawledUrls()) {
			// Display number of processed documents to keep track of progress				
			if(url.equals(Oracleurl)){
				return true;
			}
		}
		return false;
	}
}
