var lastQuery = "";

$(document).ready(function() {
	lastQuery = $("#txtQuery").val(); // don't show autocomplete when page loads
	
	setInterval(function() {
		var currentQuery = $("#txtQuery").val();
		if (lastQuery !== currentQuery) {
			lastQuery = currentQuery;
			if (currentQuery === "") {
				hideSuggest();
				return;
			}
			
			$.ajax({
				url : "services/autocomplete-service.jsp",
				data : {
					query : currentQuery
				},
				method : "GET",
				success : function(response) {
					response = $.trim(response);
					if (response !== "") {
						updateAndShowSuggest(response);
					}
					else {
						hideSuggest();
					}
				},
				error : function() {
					console.log("There was an error on the server");
				}
			});
		}
	}, 500);
});

function updateAndShowSuggest(itemsHtml) {
	var txtQuery = $("#txtQuery");
	
	$("#autocomplete").html(itemsHtml)
	                  .width(txtQuery.outerWidth()) // match the length of the search box
	                  .css("top", txtQuery.offset().top + txtQuery.outerHeight()) // position right below
	                  .css("left", txtQuery.offset().left) // align to the left side
	                  .show();
	
	// Attach events to items
	$("#autocomplete .item").click(function() {
		$("#autocomplete").hide();
		
		// Enter the selected query in the query-textbox
		$("#txtQuery").val($(this).text());
		
		// Submit the form (initiate search)
		$("#frmSearch").submit();
	});
}

function hideSuggest() {
	$("#autocomplete").hide();
}