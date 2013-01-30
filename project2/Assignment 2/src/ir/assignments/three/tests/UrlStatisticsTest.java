package ir.assignments.three.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import ir.assignments.three.MemoryDocumentStorage;
import ir.assignments.three.UrlStatistics;
import ir.assignments.two.a.*;
import ir.assignments.two.tests.TestUtils;

import org.junit.Test;

public class UrlStatisticsTest {

	@Test
	public void testCountUniquePages() {
		List<String> urls = Arrays.asList(new String[] 
										  { 
											"http://www.example.com", 
											"http://www.example.com/", 
											"http://www.otherexample.com" });
		int expectedCount = 3;
		int actualCount = UrlStatistics.countUniquePages(urls);

		assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testCountUniquePages_QueriesAndTags() {
		List<String> urls = Arrays.asList(new String[] 
				                          {
											"http://www.example.com/page.php", 
											"http://www.example.com/page.php?query=1", 
											"http://www.example.com/page.php?query=1&query=2&query=3",
											"http://www.example.com/page.php#tag",
											"http://www.example.com/page.php?query#tag" });
		int expectedCount = 1;
		int actualCount = UrlStatistics.countUniquePages(urls);

		assertEquals(expectedCount, actualCount);
	}
	
	@Test
	public void testCountUniquePages_Null() {
		int expectedCount = 0;
		int actualCount = UrlStatistics.countUniquePages(null);

		assertEquals(expectedCount, actualCount);
	}
	
	@Test
	public void testCountUniquePages_Empty() {
		int expectedCount = 0;
		int actualCount = UrlStatistics.countUniquePages(Arrays.asList(new String[0]));

		assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testCountSubdomains() {
		List<String> urls = Arrays.asList(new String[] 
							              {
											"http://vision.ics.uci.edu/",
											"http://vision.ics.uci.edu/page.php",
											"http://vision.ics.uci.edu/page.php?query",
											"http://other.ics.uci.edu/",
											"http://other.ics.uci.edu/page.php" });
							    
		
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("http://other.ics.uci.edu", 2));
		expected.add(new Frequency("http://vision.ics.uci.edu", 2));
		
		List<Frequency> actual = UrlStatistics.countSubdomains(urls);
		TestUtils.compareFrequencyLists(expected, actual);
	}
	
	@Test
	public void testCountSubdomains_Tricky() {
		List<String> urls = Arrays.asList(new String[] 
							              {
											"http://vision.ics.uci.edu", // <--- no slash
											"http://vision.ics.uci.edu/", // <--- yes slash (two separate pages)
											"http://vision.ics.uci.edu/page.php",
											"http://vision.ics.uci.edu/page.php?query" });
		
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("http://vision.ics.uci.edu", 3));
		
		List<Frequency> actual = UrlStatistics.countSubdomains(urls);
		TestUtils.compareFrequencyLists(expected, actual);
	}
	
	@Test
	public void testCountSubdomains_Null() {
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		List<Frequency> actual = UrlStatistics.countSubdomains(null);
		TestUtils.compareFrequencyLists(expected, actual);
	}
	
	@Test
	public void testCountSubdomains_EmptyList() {
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		List<Frequency> actual = UrlStatistics.countSubdomains(new ArrayList<String>());
		TestUtils.compareFrequencyLists(expected, actual);
	}

	@Test
	public void testGetLongestPage() {
		// Setup the (fake) URLs to check
		List<String> urls = getTestDocumentUrls();

		// Setup up the test in-memory document storage
		MemoryDocumentStorage docStorage = getTestDocumentStorage();

		// Get the longest page
		String actualLongestPageUrl = UrlStatistics.getLongestPage(urls, docStorage);
		String expectedLongestPageUrl = "http://www.fake.com/page3.php";
		
		assertEquals(expectedLongestPageUrl, actualLongestPageUrl);
	}

	@Test
	public void testGetMostCommonWords() {
		// Setup the (fake) URLs to check
		List<String> urls = getTestDocumentUrls();

		// Setup up the test in-memory document storage
		MemoryDocumentStorage docStorage = getTestDocumentStorage();
		
		// Get the most common words
		String[] actualMostCommonWords = toStringArray(UrlStatistics.getMostCommonWords(urls, docStorage));
		String[] expectedMostCommonWords = new String[] { "word", "page", "test", "one", "another", "two", "three", "yet" }; // Order matters (sorted by freq)
		
		assertArrayEquals(expectedMostCommonWords, actualMostCommonWords);
	}

	@Test
	public void testGetMostCommon2Grams() {
		// Setup the (fake) URLs to check
		List<String> urls = getTestDocumentUrls();

		// Setup up the test in-memory document storage
		MemoryDocumentStorage docStorage = getTestDocumentStorage();
		
		// Get the most common words
		String[] actualMostCommon2Grams = toStringArray(UrlStatistics.getMostCommon2Grams(urls, docStorage));
		String[] expectedMostCommon2Grams = new String[] { "word word", "one two", "test page", "two one", "another test", "one three", "page one", "page word", "test page", "yet another"  }; // Order matters (sorted by freq)
		
		assertArrayEquals(expectedMostCommon2Grams, actualMostCommon2Grams);
	}
	
	private List<String> getTestDocumentUrls() {
		return Arrays.asList(new String[] { "http://www.fake.com/page1.php", "http://www.fake.com/page2.php", "http://www.fake.com/page3.php" });
	}

	private MemoryDocumentStorage getTestDocumentStorage() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		docStorage.storeDocument("http://www.fake.com/page1.php", toHtml("Test Page", "This is a test page")); // Stop words
		docStorage.storeDocument("http://www.fake.com/page2.php", toHtml("Another Test Page", "One two one two One three")); // 2-grams 
		docStorage.storeDocument("http://www.fake.com/page3.php", toHtml("Yet Another Test Page", "Word word word word word word word")); // high frequency word 
		
		return docStorage;
	}
	
	private String toHtml(String title, String body) {
		return "<html><title>" + title + "</title><body>" + body + "</body></html>";
	}
	
	private String[] toStringArray(List<String> elements) {
		return elements.toArray(new String[elements.size()]);
	}
}
