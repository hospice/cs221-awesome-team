package ir.assignments.four.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import ir.assignments.four.HighlighterUtility;
import ir.assignments.four.ResultSet;
import ir.assignments.four.SearchFiles;
import ir.assignments.four.storage.DocumentStorage;
import ir.assignments.four.storage.HtmlDocument;

public class WebSearch {
	private SearchFiles indexSearch;
	private DocumentStorage docStorage;

	public WebSearch() {
		try {
			this.indexSearch = new SearchFiles("C:\\Users\\Vibhor Mathur\\Documents\\IR\\Project\\project3\\Assignment3\\docIndexstoring");
			this.docStorage = new DocumentStorage("C:\\Users\\Vibhor Mathur\\Documents\\IR\\Project\\project3\\Assignment3\\docStorage\\docStorage");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WebResultSet search(String query, int page, int maxPerPage) {
		ResultSet results = this.indexSearch.search(query, page, maxPerPage);

		// Create the results 
		ArrayList<SearchResult> displayResults = new ArrayList<SearchResult>();
		List<String> urls = results.getUrls();

		for (int i = 0; i < urls.size(); i++) {
			String url = urls.get(i);
			String html = docStorage.getDocument(url);
			HtmlDocument doc = new HtmlDocument(url, html);

			String title = doc.getTitle();
			String description="";

			try {
				description = this.indexSearch.getHighlights(url, query, results.getIds().get(i), doc.getBody());
			}
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvalidTokenOffsetsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			SearchResult result = new SearchResult(title, url, description);
			displayResults.add(result);
		}

		SearchResult[] displayResultsArray = displayResults.toArray(new SearchResult[displayResults.size()]);
		return new WebResultSet(displayResultsArray, results.getTotalHits(), page); 
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
