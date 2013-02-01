package ir.assignments.three;

import java.util.List;
/*
 * A class that collects the final results which can be accessed though the getters.
 */
public class Result {
	static String longestPageUrlString;
	static List<String> mostCommonWords;
	static List<String> mostCommon2Grams;
	
	public String getLongestPageUrlString() {
		return longestPageUrlString;
	}
	public void setLongestPageUrlString(String pageURL) {
		longestPageUrlString = pageURL;
	}
	public List<String> getMostCommonWords() {
		return mostCommonWords;
	}
	public void setMostCommonWords(List<String> commonWords) {
		mostCommonWords = commonWords;
	}
	public  List<String> getMostCommon2Grams() {
		return mostCommon2Grams;
	}
	public void setMostCommon2Grams(List<String> common2Grams) {
		mostCommon2Grams = common2Grams;
	}
	
	

}
