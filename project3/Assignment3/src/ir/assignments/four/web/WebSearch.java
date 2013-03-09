package ir.assignments.four.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.assignments.four.SearchFiles;
import ir.assignments.four.storage.DocumentStorage;
import ir.assignments.four.storage.HtmlDocument;

public class WebSearch {
	private SearchFiles indexSearch;
	private DocumentStorage docStorage;
	
	public WebSearch() {
		try {
			this.indexSearch = new SearchFiles("docIndexEnhanced");
			this.docStorage = new DocumentStorage("docStorage\\docStorage");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SearchResult[] search(String query, int page) {
		List<String> urlResults = this.indexSearch.search(query, 10);
		
		// Create the results 
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		for (String url : urlResults) {
			String html = docStorage.getDocument(url);
			HtmlDocument doc = new HtmlDocument(url, html);
			
			String title = doc.getTitle();
			String description = doc.getBody();
			description = description.substring(0, Math.min(100, description.length())).trim() + "...";
			
			SearchResult result = new SearchResult(title, url, description);
			results.add(result);
		}
		
		return results.toArray(new SearchResult[results.size()]);
	}

	public void close() {
		if (this.indexSearch != null) {
			this.indexSearch.close();
		}
		
		if (this.docStorage != null) {
			this.docStorage.close();
		}
	}
}
