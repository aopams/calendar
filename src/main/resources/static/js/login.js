jQuery(document).ready(function($) {
	$(".registerpage").hide(0);	
	$("#registerbutton").bind('click', function(event) {
		console.log("register");
		$(".registerpage").show(0);
		$(".logreg").hide(0);
	});
	$(".backbutton").bind('click', function(event) {
		console.log("asdf");
		$(".logreg").show(0);
		$(".registerpage").hide(0);
	});
});
