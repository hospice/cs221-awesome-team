package ir.assignments.three;

import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/*
 * General code structure borrowed from: http://code.google.com/p/crawler4j/source/browse/src/test/java/edu/uci/ics/crawler4j/examples/basic/BasicCrawler.java
*/
public class ICSCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

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
		String href = url.getURL().toLowerCase(); // Turns http://www.ics.uci.edu/somepage.php -> http://www.ics.uci.edu/
		if (FILTERS.matcher(href).matches())
			return false;
		
		// Only crawl within the domain of the seed URL
		if (!url.getDomain().endsWith(this.params.getSeedUrl())) //TODO: this doesn't work
			return false;
		
		return true;
	}

	@Override
	public void visit(Page page) {
		// Keep track of visited URLs
		String url = page.getWebURL().getURL();
		stats.addCrawledPageUrl(url);

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
