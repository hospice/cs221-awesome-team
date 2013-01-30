package ir.assignments.three.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import ir.assignments.three.DocumentStorage;
import ir.assignments.three.HtmlDocument;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DocumentStorageTest {
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();
	
	@Test
	public void testWriteAndRead() {
		try {
			DocumentStorage docStorage = new DocumentStorage(tmpFolder.newFolder().getAbsolutePath());
			docStorage.storeDocument("http://www.google.com", "<html><title>Title</title><body>Body</body></html>");
			
			HtmlDocument doc = docStorage.getDocument("http://www.google.com");
			assertEquals("Title", doc.getTitle());
			assertEquals("Body", doc.getBody());			
		}
		catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testWriteSameLocation() {
		try {
			File folder = tmpFolder.newFolder();
			DocumentStorage docStorage = new DocumentStorage(folder.getAbsolutePath());
			docStorage.storeDocument("http://www.google.com", "<html><title>Title</title><body>Body</body></html>");
			docStorage.storeDocument("http://www.google.com", "<html><title>Title</title><body>Body 2</body></html>");
			
			int folderFileCount = folder.listFiles().length;
			assertEquals(1, folderFileCount);			
		}
		catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
