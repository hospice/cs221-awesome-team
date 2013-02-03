package ir.assignments.three;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.b.FrequencyComparator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class DocumentStatistics {
	private String longestDocumentUrl;
	private List<String> mostCommonWords;
	private List<String> mostCommonTwoGrams;

	public String getLongestDocumentUrl() {
		return this.longestDocumentUrl;
	}

	public List<String> getMostCommonWords() {
		return this.mostCommonWords;
	}

	public List<String> getMostCommonTwoGrams() {
		return this.mostCommonTwoGrams;
	}

	public void runStats(IDocumentStorage docStorage) {
		RecordManager tokenManager = null;
		RecordManager twoGramManager = null;
		
		try {
			// Initialize a file-based tree map for tokens and 2-grams. 
			// Using a tree map gives faster insert performance since the tokens (keys) are pretty small (hashmap has too many collisions).
			File statsDir = new File("stats");
			statsDir.mkdir();			
			
			tokenManager = RecordManagerFactory.createRecordManager("stats\\tokenFrequencies.jdb");
			twoGramManager = RecordManagerFactory.createRecordManager("stats\\twoGramFrequencies.jdb");

			PrimaryTreeMap<String, Integer> tokenMap = tokenManager.treeMap("tokenMap");
			PrimaryTreeMap<String, Integer> twoGramMap = twoGramManager.treeMap("twoGramMap");

			// Scan all the crawled documents
			long longestDocumentCount = 0;
			String longestDocumentUrl = "";
			long count = 0;
			
			for (HtmlDocument doc : docStorage.getAll()) {
				// Display number of processed documents to keep track of progress				
				if (count % 100 == 0)
					System.out.println(count);

				// Commit every 10,000 documents to release memory (prevent out of memory errors)
				if (count % 10000 == 0) {					
					tokenManager.commit();
					twoGramManager.commit();
					System.out.println("Commit");
				}
				
				count++;

				// Get the tokens in the document
				ArrayList<String> tokens = tokenize(doc.getAllText());

				int tokenCount = 0;
				for (int i = 0; i < tokens.size(); i++) {
					String currentToken = tokens.get(i);
					if (!isInterestingWord(currentToken))
						continue;

					// Keep track of token count (to find longest document)
					tokenCount++;

					// Keep track of token frequency
					increaseFrequency(tokenMap, currentToken);

					// Keep track of 2-gram frequency
					if (i + 1 < tokens.size()) {
						String nextToken = tokens.get(i + 1);
						if (!isInterestingWord(nextToken))
							continue;

						String twoGram = currentToken + " " + nextToken;
						increaseFrequency(twoGramMap, twoGram);
					}
				}

				// Keep track of longest document
				if (tokenCount > longestDocumentCount) {
					longestDocumentCount = tokenCount;
					longestDocumentUrl = doc.getUrl();
				}
			}

			// Commit any leftovers
			tokenManager.commit();
			twoGramManager.commit();

			// Results
			this.longestDocumentUrl = longestDocumentUrl;
			this.mostCommonWords = getTopMostCommon(tokenMap, 500); // top 500 most common words
			this.mostCommonTwoGrams = getTopMostCommon(twoGramMap, 20); // top 20 most common words

			tokenManager.close();
			twoGramManager.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			// Close the file-backed tree maps
			try {
				if (tokenManager != null)
					tokenManager.close();
			}
			catch (IOException ex2) {
				ex2.printStackTrace();
			}
			
			try {
				if (twoGramManager != null)
					twoGramManager.close();
			}
			catch (IOException ex2) {
				ex2.printStackTrace();
			}		
		}
	}
	
	private List<String> getTopMostCommon(PrimaryTreeMap<String, Integer> map, int n) {
		// Get the top n most common tokens
		ArrayList<String> mostCommon = new ArrayList<String>();
		ArrayList<Frequency> freqs = new ArrayList<Frequency>();

		// Convert the dictionary to a frequency list
		for (String token : map.keySet()) {
			Frequency freq = new Frequency(token, map.get(token));
			freqs.add(freq);
		}

		// Order by frequency (desc) and break ties with alphabetical order (asc)
		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(freqs, comparator);

		// Keep only the top n (or the entire list if it's <= n)
		for (int i = 0; i < Math.min(n, freqs.size()); i++) {
			mostCommon.add(freqs.get(i).getText());
		}

		return mostCommon;
	}

	private void increaseFrequency(PrimaryTreeMap<String, Integer> map, String token) {
		Integer count = map.get(token);
		if (count == null)
			count = 0;
		map.put(token, count + 1);
	}

	private ArrayList<String> tokenize(String input) {
		ArrayList<String> tokenize = new ArrayList<String>();
		if (input == null)
			return tokenize;

		input = input.toLowerCase();
		String[] parts = input.split("\\W+"); // alphanumeric words 
		return new ArrayList<String>(Arrays.asList(parts));
	}

	private static Pattern REGEX_PATTERN = Pattern.compile(".*\\d.*"); // compile only use
	private static Matcher REGEX_MATCHER = REGEX_PATTERN.matcher("");

	private boolean isInterestingWord(String token) {
		return token.length() >= 4 && // filter out words shorter than 4 letters
				!REGEX_MATCHER.reset(token).matches() && // filter out words with numbers
				!StopWord.isStopWord(token); // filter out stop words
	}
}
