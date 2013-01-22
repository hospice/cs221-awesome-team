package ir.assignments.three;

import java.util.HashSet;

public class ICSCrawlerStatistics {
	private HashSet<String> urlsCrawled = new HashSet<String>();
	
	public HashSet<String> getUrlsCrawled() { 
		return this.urlsCrawled;
	}
	
	public void addCrawledPageUrl(String url) {
		this.urlsCrawled.add(url);
	}
}