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

	@Override
	public boolean shouldVisit(WebURL url) {
		// Only crawl ics.uci.edu and its subdomains

		// TODO: use getDomain maybe?
		String href = url.getURL().toLowerCase(); // Turns http://www.ics.uci.edu/somepage.php -> http://www.ics.uci.edu/
		return !FILTERS.matcher(href).matches() && href.endsWith("ics.uci.edu"); //TODO: / needed?
	}

	@Override
	public void visit(Page page) {
		// Get url of crawled page
		String url = page.getWebURL().getURL();
		stats.addCrawledPageUrl(url);

		// Get the page terms and store them locally
		if (page.getParseData() instanceof HtmlParseData) { // make sure document has HTML data
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText(); // get rid of HTML tags

			//TODO: store the text somewhere
		}
	}

	@Override
	public Object getMyLocalData() {
		return this.stats;
	}
}
