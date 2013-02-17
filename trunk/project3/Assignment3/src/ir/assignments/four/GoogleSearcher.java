package ir.assignments.four;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearcher {

	public static void main(String[] args) {
		// For testing purposes
		GoogleSearcher searcher = new GoogleSearcher();
		List<String> results = searcher.getSearchResults("machine learning", 10);
		for (String url : results) {
			System.out.println(url);
		}
	}

	private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?q=[QUERY]&hl=en&start=[START]&btnG=Google+Search&gbv=1";

	public List<String> getSearchResults(String query, int topN) {

		// Get the search url
		String encodedQuery = getQueryString(query);
		String searchUrl = GOOGLE_SEARCH_URL.replace("[QUERY]", encodedQuery);

		// Do search until we have enough results
		ArrayList<String> urlResults = new ArrayList<String>();
		int start = 0;
		while (urlResults.size() < topN) {
			String pageSearchUrl = searchUrl.replace("[START]", start + "");

			try {
				// Download the page using jsoup
				// (user Firefox's user agent string so google doesn't block request)
				Document pageDoc = Jsoup.connect(pageSearchUrl).userAgent("Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1").get();

				// Get the URLs of the search results
				Elements searchResults = pageDoc.select("li[class=g] a");
				for (Element result : searchResults) {
					if (result.text().equals("Cached") || result.text().equals("Similar")) // don't include cached or related links
						continue;
					
					String href = result.attr("href");
					String extractedUrl = extractUrl(href);
					urlResults.add(extractedUrl);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				break;
			}

			// Wait a second before requesting more results to be polite to google
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Get 10 next results if necessary
			start += 10;
		}

		return urlResults;
	}

	private String extractUrl(String googleUrl) {
		// Google search result links are not the direct links, have to get it from the "q" parameter
		int start = googleUrl.indexOf("q=");
		if (start > -1) {
			start += "q=".length();

			int end = googleUrl.indexOf("&", start);
			if (end > start) {
				try {
					return URLDecoder.decode(googleUrl.substring(start, end), "UTF-8");
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	private String getQueryString(String query) {
		try {
			// Limit search to ics.uci.edu
			return URLEncoder.encode("site:ics.uci.edu " + query, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
