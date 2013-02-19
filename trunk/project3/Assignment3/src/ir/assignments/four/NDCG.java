package ir.assignments.four;

import java.util.Arrays;
import java.util.List;

public class NDCG {
	
	public static void main(String[] args) {
		List<String> urls = Arrays.asList(new String[] { "1", "2", "3", "5", "4" });
		List<String> oracleUrls = Arrays.asList(new String[] { "1", "2", "3", "4", "5" });
		
		System.out.println(NDCG.calculate(urls, oracleUrls, 5));
	}

	public static double calculate(List<String> urls, List<String> oracleUrls, int r) {
		// get DCG of urls
		double[] urlDCG = getDCG(urls, oracleUrls, r);
		
		// get DCG of perfect ranking
		double[] perfectDCG = getDCG(oracleUrls, oracleUrls, r);
		
		// normalize by dividing
		double[] normalized = new double[urlDCG.length];
		for (int i = 0; i < urlDCG.length; i++) {
			normalized[i] = urlDCG[i] / perfectDCG[i];
		}
		
		// The final score is just the sum of the normalized scores
		double score = 0;
		for (int i = 0; i < normalized.length; i++) {
			score += normalized[i];
		}
		
		return score;
	}
	
	private static double[] getDCG(List<String> urls, List<String> oracleUrls, int p) {
		double[] scores = new double[p];
		
		for (int i = 0; i < p; i++) {
			scores[i] = getRelevance(urls.get(i), oracleUrls);
			
			if (i > 0) {
				// for all positions after the first one, reduce the "gain"
				scores[i] /= (Math.log(i + 1) / Math.log(2)); // log base 2
			}
		}
		
		return scores;
	}
	
	private static double getRelevance(String url, List<String> oracleUrls) {
		// Use the position in the oracle ranking as the relevance
		return oracleUrls.size() - oracleUrls.indexOf(url);
	}
}
