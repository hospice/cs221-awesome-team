package ir.assignments.three;

import java.net.URI;
import java.net.URISyntaxException;

public class URLHelper {
	public static String removeQuery(String url) {
		try {
			URI uri = new URI(url);
			return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
		}
		catch (URISyntaxException e) {
			System.out.println("Error removing query from: " + url);
			e.printStackTrace();
		}

		return null;
	}

	public static String removePath(String url) {
		try {
			URI uri = new URI(url);
			return uri.getScheme() + "://" + uri.getAuthority();
		}
		catch (URISyntaxException e) {
			System.out.println("Error removing path from: " + url);
			e.printStackTrace();
		}

		return null;
	}
}
