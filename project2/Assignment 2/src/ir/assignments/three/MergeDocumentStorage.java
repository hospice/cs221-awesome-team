package ir.assignments.three;

import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.HtmlDocument;

public class MergeDocumentStorage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DocumentStorage documentStorage1 = new DocumentStorage("docStorage\\docStorage1");
		DocumentStorage documentStorage2 = new DocumentStorage("docStorage\\docStorage2");
		
		try {
			boolean firstIsSmaller = documentStorage1.getSize() < documentStorage2.getSize();
			DocumentStorage mergeFrom = firstIsSmaller ? documentStorage1 : documentStorage2;
			DocumentStorage mergeTo = firstIsSmaller ? documentStorage2 : documentStorage1;
			
			int i = 0;
			for (HtmlDocument doc : mergeFrom.getAll()) {
				if (i % 500 == 0)
					System.out.println(i + "");
				
				i++;				
				mergeTo.storeDocument(doc.getUrl(), doc.getHtml());				
			}
		}
		finally {
			documentStorage1.close();
			documentStorage2.close();
		}
	}

}
