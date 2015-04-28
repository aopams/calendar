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
	
	$("#register").bind('click', function(event) {
		console.log("noice");
		var username = document.getElementById('regUser').value;
		var password = document.getElementById('pass').value;
		var fullname = document.getElementById('regName').value;
		var postParameters = {username : username, password : password, fullname : fullname};
		register(postParameters);
		

	})
	
});

/* CHANGE */

function register(postParameters, loginParam) {
	$.post('/register', postParameters, function(responseJSON) {
		var responseObject = JSON.parse(responseJSON);
		console.log(responseObject.success);
		if (responseObject.success == 1) {
			var postParameters = {user : responseObject.user, pass : responseObject.pass};
			console.log(postParameters);			
			login(postParameters);
		}
	});
}

function login(postParameters) {
	console.log(postParameters);
	var url = $("#login-form").attr('action');
	console.log('url is ' + url);
	/*$.post(url, postParameters, function(responseJSON) {
			
	}); */
}
