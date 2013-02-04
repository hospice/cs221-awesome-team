package ir.assignments.three.helpers;

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

	public static String getDomain(String url) {
		try {
			URI uri = new URI(url);
			String host = uri.getHost();
			if (host != null) {
				host = host.toLowerCase();
				if (host.startsWith("www."))
					host = host.substring("www.".length());

				return host;
			}
		}
		catch (Exception e) {
			System.out.println("Error on url: " + url);
			e.printStackTrace();
		}

		return null;
	}
}
