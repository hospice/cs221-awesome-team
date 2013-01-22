package ir.assignments.three;

import java.util.Collection;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Main {
	public static void main(String[] args) {
		
		long startTime = 0; //TODO: get current system time
		
		Collection<String> crawledUrls = Crawler.crawl("http://www.ics.uci.edu");
		
		// Question 1
		long endTime = 0; //TODO: get current system time
		long timeElapsed = endTime - startTime;
		
		// Question 2
		int uniquePages = UrlStatistics.countUniquePages(crawledUrls);
		
		// Question 3
		List<Frequency> subdomains = UrlStatistics.countSubdomains(crawledUrls);
		Utilities.printFrequencies(subdomains);
		
		// Question 4
		String longestPageUrlString = UrlStatistics.getLongestPage(crawledUrls);
		
		// Question 5
		List<String> mostCommonWord = UrlStatistics.getMostCommonWords(crawledUrls);
		
		// Question 6
		List<String> mostCommon2Gram = UrlStatistics.getMostCommonWords(crawledUrls);
	}
}
