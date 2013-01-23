package ir.assignments.three.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import ir.assignments.three.UrlStatistics;
import ir.assignments.two.a.Frequency;
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
		expected.add(new Frequency("http://vision.ics.uci.edu", 2));
		expected.add(new Frequency("http://other.ics.uci.edu", 2));		
		
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
		fail("Not yet implemented");
	}

	@Test
	public void testGetMostCommonWords() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMostCommon2Grams() {
		fail("Not yet implemented");
	}

}
