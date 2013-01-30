package ir.assignments.three;

import java.io.*;
import java.util.ArrayList;

public class Utilities {
	public static ArrayList<String> tokenizeFile(String input) {
		String[] tokens;
		ArrayList<String> tokenize = new ArrayList<String>();

		if (input == null)
			return tokenize;

		if ( input.trim().length() != 0) {
			// Convert the input data string to lower case and then tokenize it
			input = input.toLowerCase();
			String delims = "[^a-zA-Z0-9']+";
			tokens = input.split(delims);
			int tokenLength = tokens.length;

			for (int j = 1; j <= tokenLength; j++) {
				tokenize.add(tokens[j - 1]);
			}
		}
		return tokenize;
	}
}
