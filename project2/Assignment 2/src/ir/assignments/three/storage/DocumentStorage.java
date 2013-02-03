package ir.assignments.three.storage;


import ir.assignments.three.CrawledDocument;

import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;

public class DocumentStorage implements IDocumentStorage {
	private ObjectContainer database;

	public DocumentStorage(String storagePath) {
		// Use db4o (http://www.db4o.com/) as an embedded database		
		// Configure database
		EmbeddedConfiguration dbConfig = Db4oEmbedded.newConfiguration();
		dbConfig.common().objectClass(CrawledDocument.class).objectField("url").indexed(true);
		dbConfig.file().blockSize(8);

		// Create/Open database
		database = Db4oEmbedded.openFile(dbConfig, storagePath);
	}

	public void storeDocument(String url, String html) {
		// Check if URL already stored
		final String targetUrl = url;
		List<CrawledDocument> existingEntries = database.query(new Predicate<CrawledDocument>() {
			public boolean match(CrawledDocument storedDoc) {
				return storedDoc.url.equals(targetUrl);
			}
		});
		CrawledDocument entryToAddOrUpdate = existingEntries.size() > 0 ? existingEntries.get(0) : null;

		// Either add a new entry or update the existing entry
		if (entryToAddOrUpdate == null)
			entryToAddOrUpdate = new CrawledDocument(url, html); // new
		else
			entryToAddOrUpdate.content = html; // update

		// Write to database
		database.store(entryToAddOrUpdate);
	}

	public Iterable<String> getCrawledUrls() {
		return new UrlIterable(database.query(CrawledDocument.class));
	}

	public Iterable<HtmlDocument> getAll() {
		return new HtmlDocumentIterable(database.query(CrawledDocument.class));
	}

	public void close() {
		this.database.close();
	}
}
