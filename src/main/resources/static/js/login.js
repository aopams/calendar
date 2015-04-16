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
	
	
	$("#login").bind('click', function(event) {
		var username = $("#user").val();
		username = JSON.stringify(username);
		var password = $("#password").val();
		password = JSON.stringify(password);
		var randNum = 0;
		$.get("/randnum", function(responseJSON){
			responseObject = JSON.parse(responseJSON);
			randNum = responseObject.num;
		});
		var postParameters = {
			username : username,
			password : password,
			randNum : randNum
		}
		
		var string = "/calendar/" +randNum;
		
		console.log(string);
		
		$.post("/calendar/" + randNum, postParameters, function(response) {
			
		});

	})
});