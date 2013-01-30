package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class DocumentStorage implements IDocumentStorage {
	private String storagePath;
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
				String[] lines = new String[] { url, text };
				FileHelper.writeFile(urlFile, lines);
			}
			catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public HtmlDocument getDocument(String url) {
		synchronized (getUrlLock(url)) {
			// Make sure storage path exists
			File storageDir = new File(this.storagePath);
			if (!storageDir.exists())
				return null;

			// Find the URL file by 1) hash in the filename and 2) URL in the contents
			try {
				File urlDoc = getDocumentFromUrl(storageDir, url);
				if (urlDoc != null) {
					String docContent = FileHelper.readFile(urlDoc, 1); // skip first line (URL)
					return new HtmlDocument(docContent);
				}
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

	private String getUrlFromDocument(File file) throws IOException {
		return FileHelper.readFirstLine(file);
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
