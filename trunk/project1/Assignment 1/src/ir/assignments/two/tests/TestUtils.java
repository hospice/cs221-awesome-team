package ir.assignments.two.tests;

import static org.junit.Assert.assertEquals;
import ir.assignments.two.a.Frequency;

import java.util.List;

public class TestUtils {
	public static void compareFrequencyLists(List<Frequency> expected, List<Frequency> actual) {
		assertEquals(expected.size(), actual.size());
		
		for (int i = 0; i < expected.size(); i++)
		{
			Frequency expectedFreq = expected.get(i);
			Frequency actualFreq = actual.get(i);
			
			assertEquals(expectedFreq.getText(), actualFreq.getText());
			assertEquals(expectedFreq.getFrequency(), actualFreq.getFrequency());
		}
	}
}
