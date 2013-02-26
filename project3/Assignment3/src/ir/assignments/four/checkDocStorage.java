package ir.assignments.four;

import ir.assignments.three.helpers.URLHelper;
import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.IDocumentStorage;

public class checkDocStorage {

	public static void main(String[] args) {
		System.out.println(check("http://www.ics.uci.edu/prospective/en/degrees/software-engineering/").toString());
	}
	
	public static Boolean check(String Oracleurl){
		
		DocumentStorage docStorage = new DocumentStorage("docStorage\\docStorage");

		int i = 0;
		for (String url : docStorage.getCrawledUrls()) {
			// Display number of processed documents to keep track of progress
			if (i % 500 == 0) {
				System.out.println(i + "");
			}
			i++;
			
			if(url.equals(Oracleurl)){
				return true;
			}
		}
		return false;
	}
}
