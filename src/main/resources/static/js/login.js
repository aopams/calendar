jQuery(document).ready(function($) {
	$(".loginpage").hide(0);
	$(".registerpage").hide(0);	
	$("#loginbutton").bind('click', function(event) {
		$(".loginpage").show(0);
		$(".registerpage").hide(0);	
		$(".logreg").hide(0);
	});
	$("#registerbutton").bind('click', function(event) {
		$(".registerpage").show(0);
		$(".loginpage").hide(0);
		$(".logreg").hide(0);
	});
	$(".backbutton").bind('click', function(event) {
		console.log("asdf");
		$(".logreg").show(0);
		$(".loginpage").hide(0);
		$(".registerpage").hide(0);
	});
});