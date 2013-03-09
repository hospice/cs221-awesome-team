<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="" import="ir.assignments.four.web.*"%>
<%
	// This is the service that performs the search on the server and then returns the results

	// Input Parameters
	String query = request.getParameter("query");
	String pageStr = request.getParameter("page");

	int currentPage = 0;
	try {
		if (pageStr != null)
			currentPage = Integer.parseInt(pageStr);
	}
	catch (Exception e) {
	}

	// Do search
	WebSearch searcher = null;
	SearchResult[] results = null;
	try {
		searcher = new WebSearch();
		results = searcher.search(query, currentPage);
	}
	finally {
		if (searcher != null)
			searcher.close();
	}

	// Render the results
	if (results != null && results.length > 0) {
		for (SearchResult result : results) {
			out.println("<div class=\"search-result\">");

			// Title
			out.println("<a href=\"" + result.getUrl() + "\" class=\"title\">");
			out.println(result.getTitle());
			out.println("</a>");

			// Url
			out.println("<div class=\"url\">");
			out.println(result.getDisplayUrl());
			out.println("</div>");

			// Description
			out.println("<div class=\"description\">");
			out.println(result.getDescription());
			out.println("</div>");

			out.println("</div>");
		}
	} else {
		out.println("<div class=\"no-results\">No search results found</div>");
	}
%>