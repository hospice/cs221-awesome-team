package ir.assignments.two.tests;

import static org.junit.Assert.*;
import ir.assignments.two.a.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PartA {

	@Test
	public void testTokenizeFile() {
		String[] words = tokenize("An input string, this is! (or is it?)");
		assertArrayEquals(new String[] { "an", "input", "string", "this", "is", "or", "is", "it" }, words);
	}

	@Test
	public void testPrintFrequencies() {
		fail("Not yet implemented");
	}

	private String[] tokenize(String content) {
		File file = getTempFile("asdf");
		ArrayList<String> words = Utilities.tokenizeFile(file);
		deleteTempFile(file);
		return words.toArray(new String[words.size()]);
	}

	private File getTempFile(String content) {
		File tmpFile = null;

		try {
			tmpFile = File.createTempFile("test", ".tmp");
			FileWriter fstream = new FileWriter(tmpFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			// Close the output stream
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return tmpFile;
	}

	private void deleteTempFile(File file) {
		file.delete();
	}
}
