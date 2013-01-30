package ir.assignments.three;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MainStatisticsOnly {
	public static void main(String[] args) {
		DocumentStorage documentStorage = new DocumentStorage("docStorage");
		Collection<String> crawledUrls = getCraweledUrls("docStorage");

		AnswerQuestions.Answer(0, crawledUrls, documentStorage);
	}

	private static Collection<String> getCraweledUrls(String docStorageFolder) {
		ArrayList<String> urls = new ArrayList<String>();

		if (docStorageFolder != null && !docStorageFolder.equals("")) {
			for (File possibleFile : new File(docStorageFolder).listFiles()) {
				try {
					String firstLine = FileHelper.readFirstLine(possibleFile);
					if (firstLine.startsWith("http"))
						urls.add(firstLine);
				}
				catch (IOException ex) {
				}
			}
		}

		return urls;
	}
}
