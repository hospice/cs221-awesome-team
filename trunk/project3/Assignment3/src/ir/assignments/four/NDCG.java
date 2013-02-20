package ir.assignments.four;

import java.util.Arrays;
import java.util.List;

public class NDCG {
	
	public static void main(String[] args) {
		List<String> urls = Arrays.asList(new String[] { "1", "2", "3", "4", "5" });
		List<String> oracleUrls = Arrays.asList(new String[] { "1", "2", "3", "4", "5" });
		
		System.out.println(NDCG.getNDCG(urls, oracleUrls, 5));
	}

	public static double getNDCG(List<String> urls, List<String> oracleUrls, int r) {
		// get DCG of urls
		double urlDCG = getDCG(urls, oracleUrls, r);
		
		// get DCG of perfect ranking
		double perfectDCG = getDCG(oracleUrls, oracleUrls, r);
		
		// normalize by dividing
		double normalized = urlDCG / perfectDCG;
		return normalized;
	}
	
	private static double getDCG(List<String> urls, List<String> oracleUrls, int p) {
		double[] scores = new double[p];
		
		for (int i = 0; i < p; i++) {
			scores[i] = getRelevance(urls.get(i), oracleUrls);
			
			if (i > 0) {
				// for all positions after the first one, reduce the "gain"
				double logValue = (Math.log(i + 1) / Math.log(2));
				scores[i] /= (Math.log(i + 1) / Math.log(2)); // log base 2
			}
		}
		
		// Score is the sum
		double score = 0;
		for (int i = 0; i < scores.length; i++) {
			score += scores[i];
		}
		
		return score;
	}
	
	private static double getRelevance(String url, List<String> oracleUrls) {
		// Use the position in the oracle ranking as the relevance
		return oracleUrls.size() - oracleUrls.indexOf(url);
	}
}
