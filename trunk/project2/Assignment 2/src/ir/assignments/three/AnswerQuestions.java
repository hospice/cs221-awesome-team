package ir.assignments.three;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.util.Collection;
import java.util.List;

public class AnswerQuestions {
	public static void Answer(double secondsElapsed, Collection<String> crawledUrls, IDocumentStorage documentStorage) {
		System.out.println("Crawled " + crawledUrls.size() + " pages(s)");
		
		// Question 1
		System.out.println("Seconds elapsed: " + secondsElapsed);

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
