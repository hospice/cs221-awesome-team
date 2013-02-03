package ir.assignments.three;

import ir.assignments.three.storage.DocumentStorage;

import java.util.Collection;

public class MainCrawler {
	public static void main(String[] args) {
		// Do crawl
		DocumentStorage documentStorage = new DocumentStorage("docStorage.yap");
		try {
			Collection<String> crawledUrls = Crawler.crawl("http://www.ics.uci.edu", documentStorage);
			System.out.println("Finished crawling " + crawledUrls.size() + " page(s)");
		}
		finally {
			documentStorage.close();
		}		
	}
}
