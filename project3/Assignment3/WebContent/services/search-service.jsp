<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="" import="ir.assignments.four.web.*"%>
<%
	// This is the service that performs the search on the server and then returns the results

	// Input Parameters
	String query = request.getParameter("query");
	String pageStr = request.getParameter("page");	
	int maxPerPage = 10;

	int currentPage = 0;
	try {
		if (pageStr != null)
			currentPage = Integer.parseInt(pageStr) - 1;
		
		if (currentPage < 0)
			currentPage = 0;
		else if (currentPage > 20) // limit to top 20 pages
			currentPage = 20;
	}
	catch (Exception e) {
	}

	// Do search
	WebSearch searcher = null;
	WebResultSet results = null;
	try {
		searcher = new WebSearch();
		results = searcher.search(query, currentPage, maxPerPage);
	}
	finally {
		if (searcher != null)
			searcher.close();
	}

	// Render the results
	if (results != null && results.getResults().length > 0) {
		for (SearchResult result : results.getResults()) {
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
		out.println("<strong>Page: " + results.getCurrentPage() + "</strong>");
		out.println("<strong>Pages: " + results.getTotalPages(maxPerPage) + "</strong>");
	} else {
		out.println("<div class=\"no-results\">No search results found</div>");
	}
%>