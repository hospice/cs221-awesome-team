package ir.assignments.three;

import java.util.Iterator;

import com.db4o.ObjectSet;

public class HtmlDocumentIterable implements Iterable<HtmlDocument> {
	private ObjectSet<CrawledDocument> results;

	public HtmlDocumentIterable(ObjectSet<CrawledDocument> results) {
		this.results = results;
	}
	
	@Override
	public Iterator<HtmlDocument> iterator() {
		return new HtmlDocumentIterator(results);
	}
	
	public class HtmlDocumentIterator implements Iterator<HtmlDocument> {
		private ObjectSet<CrawledDocument> results;

		public HtmlDocumentIterator(ObjectSet<CrawledDocument> results) {
			this.results = results;
		}

		@Override
		public boolean hasNext() {
			return results.hasNext();
		}

		@Override
		public HtmlDocument next() {
			CrawledDocument doc = results.next();
			return new HtmlDocument(doc.url, doc.content);
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}
	}
}
