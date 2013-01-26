package ir.assignments.three;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class DocumentStorage implements IDocumentStorage {
	private String storagePath;
	private String lineSeparator = System.getProperty("line.separator");
	private static final String UNIQUE_ID = "e6ff0549-d27b-4ada-adaf-71e671b0a93f";

	public DocumentStorage(String storagePath) {
		this.storagePath = storagePath;
	}

	public void storeDocument(String url, String text) {
		synchronized (getUrlLock(url)) {
			// Make sure storage path exists
			File storageDir = new File(this.storagePath);
			storageDir.mkdir();

			try {
				// Try to find an existing local copy of the URL
				File urlFile = getDocumentFromUrl(storageDir, url);

				// If no match found, create a new file (URL hash value + random string)
				if (urlFile == null) {
					urlFile = new File(storageDir, getUrlHash(url) + UUID.randomUUID().toString() + ".txt");
				}

				// Write to the file
				writeDocument(urlFile, url, text);
			}
			catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public String getDocument(String url) {
		synchronized (getUrlLock(url)) {
			// Make sure storage path exists
			File storageDir = new File(this.storagePath);
			if (!storageDir.exists())
				return null;

			// Find the URL file by 1) hash in the filename and 2) URL in the contents
			try {
				File urlDoc = getDocumentFromUrl(storageDir, url);
				if (urlDoc != null)
					return readDocument(urlDoc);
			}
			catch (FileNotFoundException e) {
				System.out.println("Error: " + e.getMessage());
			}
			catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}

			// Document not found
			return null;
		}
	}

	private Object getUrlLock(String url) {
		// Lock based on the global instance of a string (unique_id + URL hash value)
		// The unique id is to coincidences that would lock on unintended strings
		return (UNIQUE_ID + getUrlHash(url)).intern();
	}

	private String getUrlHash(String url) {
		return Math.abs(url.hashCode()) + "";
	}

	private void writeDocument(File file, String url, String text) throws IOException {
		FileWriter stream = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(stream);
		writer.write(url + lineSeparator); // first line is URL
		writer.write(text); // rest is URL content
		writer.flush();
		writer.close();
	}

	private String readDocument(File file) throws IOException, FileNotFoundException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			// Skip the first line (the URL)
			reader.readLine();

			// Read the rest
			String content = "";
			String currentLine = "";
			while ((currentLine = reader.readLine()) != null) {
				content += currentLine;
				if (reader.ready())
					content += lineSeparator;
			}

			return content;
		}
		finally {
			if (reader != null)
				reader.close();
		}
	}

	private String getUrlFromDocument(File file) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			return reader.readLine();
		}
		finally {
			if (reader != null)
				reader.close();
		}
	}

	private File getDocumentFromUrl(File directory, String url) throws IOException {
		File urlDoc = null;
		String urlHash = getUrlHash(url);

		for (File possibleFile : directory.listFiles()) {
			if (!possibleFile.getName().startsWith(urlHash))
				continue;

			// Check the first line of the doc (the URL)
			if (url.equals(getUrlFromDocument(possibleFile))) {
				urlDoc = possibleFile; // match found
				break;
			}
		}

		return urlDoc;
	}
}
