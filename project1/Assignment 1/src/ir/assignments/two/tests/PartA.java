package ir.assignments.two.tests;

import static org.junit.Assert.*;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PartA {
	private String lineSeparator = System.getProperty("line.separator");

	@Test
	public void testTokenizeFile() {
		String[] words = tokenize("An input string, this is! (or is it?)");
		assertArrayEquals(new String[] { "an", "input", "string", "this", "is",
				"or", "is", "it" }, words);
	}

	@Test
	public void testPrintFrequenciesWords() {
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		frequencies.add(new Frequency("sentence", 2));
		frequencies.add(new Frequency("the", 1));
		frequencies.add(new Frequency("this", 1));
		frequencies.add(new Frequency("word", 1));

		String output = getPrintedFrequencies(frequencies);
		String expected = getExpectedOutput(new String[] 
   		                                    { 
   												"Total item count: 6",
   												"Unique item count: 5",
   												"",
   												"sentence   2",
   												"the        1",
   												"this       1",
   												"repeats    1",
   												"word       1"
   		                                    });

		assertEquals(expected, output);
	}

	@Test
	public void testPrintFrequencies2Gram() {
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		frequencies.add(new Frequency("you think", 2));
		frequencies.add(new Frequency("how you", 1));
		frequencies.add(new Frequency("know how", 1));
		frequencies.add(new Frequency("think you", 1));
		frequencies.add(new Frequency("you know", 1));

		String output = getPrintedFrequencies(frequencies);
		String expected = getExpectedOutput(new String[] 
		                                    { 
												"Total 2-gram count: 6",
												"Unique 2-gram count: 5",
												"",
												"you think      2",
												"how you        1",
												"know how       1",
												"think you      1",
												"you know       1"
		                                    });
		assertEquals(expected, output);
	}

	private String[] tokenize(String content) {
		File file = getTempFile(content);
		ArrayList<String> words = Utilities.tokenizeFile(file);
		deleteTempFile(file);
		return words.toArray(new String[words.size()]);
	}

	private String getExpectedOutput(String[] lines) {
		String output = "";
		for (String line : lines)
		{
			output += line + lineSeparator;
		}
		return output;
	}

	private String getPrintedFrequencies(ArrayList<Frequency> frequencies) {
		// Capture the output stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));

		Utilities.printFrequencies(frequencies);

		// Restore output stream to console window
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

		return baos.toString();
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
