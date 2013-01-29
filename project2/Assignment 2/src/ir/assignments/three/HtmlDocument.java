package ir.assignments.three;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlDocument {
	private final static String lineSeparator = System.getProperty("line.separator");
	private Document parsedDoc;

	public HtmlDocument(String html) {
		// Parse document using jsoup (http://jsoup.org/)
		this.parsedDoc = Jsoup.parse(html);
	}

	public String getTitle() {
		return this.parsedDoc.title();
	}

	public String getDescription() {
		String description = "";

		// Get description from meta tag(s) (e.g. <meta name="description" content="This is the description" />)
		Elements metaTags = this.parsedDoc.select("meta[name=description]");
		if (metaTags != null && metaTags.size() > 0) {
			for (Element metaTag : metaTags) {
				description += metaTag.attr("content");
			}
		}

		return description;
	}

	public String getBody() {
		return this.parsedDoc.body().text();
	}

	public String getAllText() {
		return getTitle() + lineSeparator + getDescription() + lineSeparator + getBody();
	}
}
