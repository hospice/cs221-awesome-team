package ir.assignments.four;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.HtmlDocument;
import ir.assignments.three.storage.IDocumentStorage;

public class Indexer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DocumentStorage docStorage = new DocumentStorage("docStorage\\docStorage");
		try {
			Indexer indexer = new Indexer();
			indexer.indexDocuments(docStorage, "docIndex");
		}
		finally {
			docStorage.close();
		}
	}

	public void indexDocuments(IDocumentStorage docStorage, String indexPath) {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);

		try {
			Directory indexDir = FSDirectory.open(new File(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
			IndexWriter indexWriter = new IndexWriter(indexDir, config);

			try {
				int i = 0;
				for (HtmlDocument crawledDoc : docStorage.getAll()) {
					
					if (i % 500 == 0)
						System.out.println(i + "");
					
					i++;
//					if (i >= 1000)
//						break;

					Document indexDoc = new Document();
					
					indexDoc.add(new StringField("url", crawledDoc.getUrl(), Field.Store.YES));
					indexDoc.add(new TextField("title", crawledDoc.getTitle(), Field.Store.YES));
					indexDoc.add(new TextField("content", crawledDoc.getBody(), Field.Store.NO));

					// Update document based on URL
					indexWriter.updateDocument(new Term("url",  crawledDoc.getUrl()), indexDoc);
				}
			}
			finally {
				indexWriter.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}
}
