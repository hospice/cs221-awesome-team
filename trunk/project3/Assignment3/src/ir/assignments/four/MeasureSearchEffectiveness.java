package ir.assignments.four;

import java.util.List;

public class MeasureSearchEffectiveness {

	public static void main(String[] args) {
		System.out.println("mondego: " + getScore("mondego", false));
		System.out.println("machine learning: " + getScore("machine learning", false));
		System.out.println("software engineering: " + getScore("software engineering", false));
		System.out.println("security: " + getScore("security", false));
		System.out.println("student affairs: " + getScore("student affairs", false));
		System.out.println("graduate courses: " + getScore("graduate courses", false));
		System.out.println("Crista Lopes: " + getScore("Crista Lopes", false));
		System.out.println("REST: " + getScore("REST", false));
		System.out.println("computer games: " + getScore("computer games", false));
		System.out.println("information retrieval: " + getScore("information retrieval", false));
	}
	
	private static double getScore(String query, boolean printResults) {
		try {
			GoogleSearcher google = new GoogleSearcher();
			SearchFiles localSearch = new SearchFiles("docIndexEnhanced");
			
			List<String> googleResults = google.getSearchResults(query, 5);
			List<String> localResults = localSearch.search(query, 5);
			
			if (printResults) {
				printResults("Google", googleResults);
				printResults("Local", localResults);
			}
			
			return NDCG.getNDCG(localResults, googleResults, 5);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private static void printResults(String header, List<String> results) {
		System.out.println(header + ":");
		int rank = 1;
		for (String result : results) {
			System.out.println(" " + rank + ": " + result);
			rank++;
		}
	}

}
