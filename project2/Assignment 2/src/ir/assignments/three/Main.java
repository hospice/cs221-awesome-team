package ir.assignments.three;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.util.Collection;
import java.util.List;


public class Main {
	public static void main(String[] args) {
		
		// Keep track of crawl time
		StopWatch watch = new StopWatch();
		watch.start();
		
		DocumentStorage documentStorage = new DocumentStorage("/docStorage"); 
		Collection<String> crawledUrls = Crawler.crawl("http://www.ics.uci.edu", documentStorage);
		
		// Question 1
		watch.stop();
		double secondsElapsed = watch.getTotalElapsedSeconds();
		
		// Question 2
		int uniquePages = UrlStatistics.countUniquePages(crawledUrls);
		
		// Question 3
		List<Frequency> subdomains = UrlStatistics.countSubdomains(crawledUrls);
		Utilities.printFrequencies(subdomains);
		
		// Question 4
		String longestPageUrlString = UrlStatistics.getLongestPage(crawledUrls, documentStorage);
		
		// Question 5
		List<String> mostCommonWord = UrlStatistics.getMostCommonWords(crawledUrls, documentStorage);
		
		// Question 6
		List<String> mostCommon2Gram = UrlStatistics.getMostCommon2Grams(crawledUrls, documentStorage);
	}
}
