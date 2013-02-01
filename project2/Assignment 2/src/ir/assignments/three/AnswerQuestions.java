package ir.assignments.three;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AnswerQuestions {
	public static void Answer(double secondsElapsed, Collection<String> crawledUrls, IDocumentStorage documentStorage) {
		File answers = new File("answers.txt");
		File subdomains = new File("Subdomains.txt");
		File commonWords = new File("CommonWords.txt");
		File common2Grams = new File("Common2Grams.txt");

		// Start
		displayAndStore(answers, "Crawled " + crawledUrls.size() + " pages(s)");

		// Question 1
		displayAndStore(answers, "Seconds elapsed: " + secondsElapsed);

		// Question 2
		int uniquePages = UrlStatistics.countUniquePages(crawledUrls);
		displayAndStore(answers, "Unique pages: " + uniquePages);

		// Question 3
		List<Frequency> countedSubdomains = UrlStatistics.countSubdomains(crawledUrls);
		ArrayList<String> subdomainsDisplay = new ArrayList<String>();
		for (Frequency freq : countedSubdomains) {
			subdomainsDisplay.add(freq.getText() + ", " + freq.getFrequency());
		}
		Collections.sort(subdomainsDisplay); // sort alphabetically
		store(subdomains, subdomainsDisplay);

		Result result = UrlStatistics.calculations(crawledUrls, documentStorage); // Calling calculations method for test processing 
		
		// Question 4
		//String longestPageUrlString = UrlStatistics.getLongestPage(crawledUrls, documentStorage);
		String longestPageUrlString = result.getLongestPageUrlString();
		displayAndStore(answers, "Longest page: " + longestPageUrlString);

		// Question 5
		//List<String> mostCommonWords = UrlStatistics.getMostCommonWords(crawledUrls, documentStorage);
		List<String> mostCommonWords = result.getMostCommonWords();
		store(commonWords, mostCommonWords);

		// Question 6
		//List<String> mostCommon2Grams = UrlStatistics.getMostCommon2Grams(crawledUrls, documentStorage);
		List<String> mostCommon2Grams = result.getMostCommon2Grams();
		store(common2Grams, mostCommon2Grams);
	
	}

	private static void displayAndStore(File file, String line) {
		try {
			FileHelper.appendToFile(file, line);
		}
		catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(line);
	}

	private static void store(File file, List<String> lines) {
		try {
			FileHelper.writeFile(file, lines.toArray(new String[lines.size()]));
		}
		catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
