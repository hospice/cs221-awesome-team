$(document).ready(function() {
	// Once the page loads, request the search results from the server
	var query = $("#txtQuery").val();
	if (query === "") {
		hideSpinner();
		return; // don't do anything if no query entered
	}

	// Do actual request
	$.ajax({
		url : "services/search-service.jsp",
		data : {
			query : query,
			page: $("#search-results-container").data("currentpage")
		},
		method : "GET",
		success : function(response) {
			$("#loading-image-container").hide();
			$("#search-results-container").html(response).fadeIn(400);
		},
		error : function() {
			$("#loading-image-container").hide();
			$("#search-results-container").html("");
			alert("There was an error on the server");
		}
	});
});

function hideSpinner() {
	$("#loading-image-container").hide();
}