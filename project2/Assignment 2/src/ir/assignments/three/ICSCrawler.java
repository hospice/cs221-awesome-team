package ir.assignments.three;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/*
 * General code structure borrowed from: http://code.google.com/p/crawler4j/source/browse/src/test/java/edu/uci/ics/crawler4j/examples/basic/BasicCrawler.java
*/
public class ICSCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|ico))$");

	private ICSCrawlerStatistics stats = new ICSCrawlerStatistics();
	private ICSCrawlerParameters params;

    @Override
    public void onStart() {
    	// Get the parameters
    	this.params = (ICSCrawlerParameters)myController.getCustomData();
    }
	
	@Override
	public boolean shouldVisit(WebURL url) {
		// Don't crawl non-HTML pages
		String href = url.getURL().toLowerCase(); // Turns http://www.ics.uci.edu/SomePage.PHP ->  http://www.ics.uci.edu/somepage.php
		if (FILTERS.matcher(href).matches()) // filter using file extension
			return false;
		
		// Only crawl within the domain of the seed URL
		try {
			URI currentUri = new URI(url.getURL());
			String currentUrlHost = currentUri.getHost();
			
			URI seedUri = new URI(this.params.getSeedUrl());
			String seedUrlHost = seedUri.getHost();
			
			if (!currentUrlHost.endsWith(seedUrlHost))
				return false;
		}
		catch (URISyntaxException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public void visit(Page page) {
		// Keep track of visited URLs
		String url = page.getWebURL().getURL();
		stats.addCrawledPageUrl(url);
		System.out.println("Crawled: " + url);

		// Get the page terms and store them locally
		if (page.getParseData() instanceof HtmlParseData) { // make sure document has HTML data
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText(); // get rid of HTML tags

			this.params.getDocumentStorage().storeDocument(url, text);
		}
	}

	@Override
	public Object getMyLocalData() {
		return this.stats;
	}
}
