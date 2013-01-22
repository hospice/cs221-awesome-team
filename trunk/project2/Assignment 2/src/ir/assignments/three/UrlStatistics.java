package ir.assignments.three;

import java.util.Collection;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class UrlStatistics {
	public static int countUniquePages(Collection<String> urls) {
		// Count the number of unique pages in the URL list (e.g. http://www.example.com/test.php and http://www.example.com/test.php?param are 2 URLs but 1 page
	}
	
	public static List<Frequency> countSubdomains(Collection<String> urls) {
		// http://vision.ics.uci.edu, 10
        // etc.
	}
	
	public static String getLongestPage(Collection<String> urls) {
		// Get the page with the most terms (including or excluding stop words?)
	}
	
	public static List<String> getMostCommonWords(Collection<String> urls) {
		// Get top 500 most common words across all documents (excluding stop words)
	}
	
	public static List<String> getMostCommon2Grams(Collection<String> urls) {
		// Get top 20 2-grams excluding stop words
	}
}
