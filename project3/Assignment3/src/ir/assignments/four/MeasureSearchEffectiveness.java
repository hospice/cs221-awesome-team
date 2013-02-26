package ir.assignments.four;

import java.util.List;

public class MeasureSearchEffectiveness {

	public static void main(String[] args) {
		String[] queries = new String[] { "mondego", "machine learning", "software engineering", "security", "student affairs",
				                          "graduate courses", "Crista Lopes", "REST", "computer games", "information retrieval" };
		
		double totalScore = 0;
		for (String query : queries) {
			double score = getScore(query, false);
			System.out.println(query + ": " + score);
			
			totalScore += score;
		}
		
		System.out.println("");
		System.out.println("Average Score: " + (totalScore / queries.length));
	}
	
	private static double getScore(String query, boolean printResults) {
		try {
			GoogleSearcher google = new GoogleSearcher();
			SearchFiles localSearch = new SearchFiles("docIndexEnhanced");
			
			List<String> googleResults = google.getSearchResults(query, 5);
			List<String> localResults = localSearch.search(query, 5);
			
			if (printResults) {
				printResults("Google", query, googleResults);
				printResults("Local", query, localResults);
			}
			
			return NDCG.getNDCG(localResults, googleResults, 5);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private static void printResults(String header, String query, List<String> results) {
		System.out.println(header + "(" + query + "):");
		int rank = 1;
		for (String result : results) {
			System.out.println(" " + rank + ": " + result);
			rank++;
		}
	}

}
