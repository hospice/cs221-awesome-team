package ir.assignments.three;

import ir.assignments.two.a.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class UrlStatistics {
	public static int countUniquePages(Collection<String> urls) {
		// Count the number of unique pages in the URL list (e.g. http://www.example.com/test.php and http://www.example.com/test.php?param are 2 URLs but 1 page
		HashSet<String> hm = new HashSet<String>();
		Iterator<String> iterator;
		int numbersOfUniquePages ;

		iterator = urls.iterator();     
		while (iterator.hasNext()){
			String a = (String) iterator.next();
			if(a.contains("?")){
				int i=a.indexOf("?");
				a = a.substring(0, i);
			}
			System.out.println(a);
			hm.add(a);
		}
		numbersOfUniquePages = hm.size();
		
		return numbersOfUniquePages ;  

	}

	public static List<Frequency> countSubdomains(Collection<String> urls) {
		// http://vision.ics.uci.edu, 10
		// etc.
		return null;
	}

	public static String getLongestPage(Collection<String> urls, IDocumentStorage docStorage) {
		// Get the page with the most terms (including or excluding stop words?)
		return null;
	}

	public static List<String> getMostCommonWords(Collection<String> urls, IDocumentStorage docStorage) {
		// Get top 500 most common words across all documents (excluding stop words)
		return null;
	}

	public static List<String> getMostCommon2Grams(Collection<String> urls, IDocumentStorage docStorage) {
		// Get top 20 2-grams excluding stop words
		return null;
	}
}
