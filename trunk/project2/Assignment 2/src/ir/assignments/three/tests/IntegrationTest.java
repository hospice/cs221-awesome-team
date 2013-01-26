package ir.assignments.three.tests;

import static org.junit.Assert.*;
import ir.assignments.three.Crawler;
import ir.assignments.three.DocumentStorage;
import ir.assignments.three.MemoryDocumentStorage;
import ir.assignments.three.StopWatch;
import ir.assignments.three.UrlStatistics;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class IntegrationTest {
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void testCrawl() {
		// Do an actual crawl of www.vcskicks.com (Armando's website)
		StopWatch watch = new StopWatch();
		watch.start();

		Collection<String> crawledUrls = new ArrayList<String>();
		DocumentStorage documentStorage = null;
		
		try {
			// Parameters (use temporary folders to storage)
			String seedURL = "http://www.vcskicks.com";
			String intermediateStoragePath = tmpFolder.newFolder().getAbsolutePath();
			String documentStoragePath = tmpFolder.newFolder().getAbsolutePath();
			int maxDepth = 1;
			int maxPages = 10; //unlimited

			documentStorage = new DocumentStorage(documentStoragePath);
			crawledUrls = Crawler.crawl(seedURL, intermediateStoragePath, documentStorage, maxDepth, maxPages);
		}
		catch (IOException ex) {
			fail("IOException: " + ex.getMessage());
		}
		
		//TODO: check crawled URLs

		// Question 1
		watch.stop();
		double secondsElapsed = watch.getTotalElapsedSeconds();
		//TODO: check that it was a reasonable time

		// Question 2
		int uniquePages = UrlStatistics.countUniquePages(crawledUrls);
		//TODO: check that number within some range

		// Question 3
		List<Frequency> subdomains = UrlStatistics.countSubdomains(crawledUrls);
		//TODO: check expected frequencies

		// Question 4
		String longestPageUrlString = UrlStatistics.getLongestPage(crawledUrls, documentStorage);
		//TODO: check expected

		// Question 5
		List<String> mostCommonWord = UrlStatistics.getMostCommonWords(crawledUrls, documentStorage);
		//TODO: check expected (maybe only top 10 or something)

		// Question 6
		List<String> mostCommon2Gram = UrlStatistics.getMostCommon2Grams(crawledUrls, documentStorage);
		//TODO: check expected (maybe only top 10 or something)
	}
}
