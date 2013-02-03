package ir.assignments.three;

import java.util.Iterator;

import com.db4o.ObjectSet;

public class UrlIterable implements Iterable<String> {
	private ObjectSet<CrawledDocument> results;

	public UrlIterable(ObjectSet<CrawledDocument> results) {
		this.results = results;
	}
	
	@Override
	public Iterator<String> iterator() {
		return new HtmlDocumentIterator(results);
	}
	
	public class HtmlDocumentIterator implements Iterator<String> {
		private ObjectSet<CrawledDocument> results;

		public HtmlDocumentIterator(ObjectSet<CrawledDocument> results) {
			this.results = results;
		}

		@Override
		public boolean hasNext() {
			return results.hasNext();
		}

		@Override
		public String next() {
			CrawledDocument doc = results.next();
			return doc.url;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
		}
	}
}
