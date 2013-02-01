package ir.assignments.three;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Class maintains an in-memory list of available stop words
 * used in removing unnecessary words form the parsing indexer.
 * Words we don't care to utilize for frequency weights in
 * determining probability.
 *
 * @author CJ_Barker
 *
 * Copyright (c) 2008 C. J. Barker. All rights reserved.
 */
public class StopWord {

	private static final String stopListFile = System.getProperty("user.dir") + File.separator + "StopWordList.txt";
	//	private static List<String> stopList = new ArrayList<String>();
	private static HashMap<String,String> stopMap = new HashMap<String,String>(); 

	/*
	 * Static class load - one-time file IO for reading in the
	 * stop words list and storing it in-memory when the application
	 * is launched.
	 */
	static {
		// read file and load list              
		File f = new File(stopListFile);
		try {
			BufferedReader input =  new BufferedReader(new FileReader(f));

			try {
				String line = null;

				while ((line = input.readLine()) != null){
					//System.out.println(line);
					if (line != null)
						stopMap.put(line.trim().toLowerCase(), "true");
					//stopList.add(line.trim().toLowerCase());
				}
			}
			finally {
				input.close();
			}
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Unable to locate stop word list file for processing: " + f.getAbsolutePath());
			System.exit(0);
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Denotes whether or not a given word is part of the stop word list.
	 *
	 * @param word  Word to check if it is a valid stop word or not
	 * @return              Boolean denoting if the word is a stop word
	 */
	public static boolean isStopWord(String word) {
		//return stopList.contains(word.trim().toLowerCase());
		if(stopMap.containsKey(word))
			return true;
		return false;
	}

	/**
	 * Access method of the stopList
	 * @return      The stop word list
	 */
/*	public static List<String> getList() {
		return stopList;
	}
*/
}

