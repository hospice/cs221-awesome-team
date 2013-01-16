package ir.assignments.two.a;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of utility methods for text processing.
 */

public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		// TODO Write body!

		String strLine;
		String[] tokens;
		ArrayList<String> tokenize=new ArrayList<String>();

		try{
			FileInputStream fstream = new FileInputStream(input);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			while((strLine=br.readLine()) != null)
			{
				strLine = strLine.toLowerCase();
				String delims = "[^a-zA-Z0-9']+";
				tokens = strLine.split(delims);
				int tokenLength = tokens.length;

				for (int j = 1; j <= tokenLength; j++)
				{
					System.out.println(tokens[j-1]);
					tokenize.add(tokens[j-1]);
				}

			}

			br.close();
			in.close();
		}
		catch (Exception e)	{
			System.err.println("Error: " + e.getMessage());
		}

		return tokenize;
	}

	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		// TODO Write body!
		String s;
		int n;
		int total=0;
		int count=0;

		for(int i=0; i<=frequencies.size()-1;i++){
			n = frequencies.get(i).getFrequency();
			total = total + n;
			count = count + 1;
		}

		s = frequencies.get(0).getText();
		
		if(s.contains(" ")){		
		System.out.println("Total 2-gram count: " + total);
		System.out.println("Unique 2-gram count: " + count + "\n");
		}
		else{
			System.out.println("Total item count: " + total);
			System.out.println("Unique item count: " + count + "\n");
		}
		
		for(int i=0; i<=frequencies.size()-1;i++){
			s = frequencies.get(i).getText();
			n = frequencies.get(i).getFrequency();
			System.out.println(s +" \t"+ n);
		}

	}
}
