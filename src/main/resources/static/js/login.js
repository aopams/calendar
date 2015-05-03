jQuery(document).ready(function($) {
	$("#registerpage").hide(0);	
	$("#registerbutton").bind('click', function(event) {
		$("#registerpage").show(0);
		$("#logreg").hide(0);
	});
	$(".backbutton").bind('click', function(event) {
		$("#logreg").show(0);
		$("#registerpage").hide(0);
	});
	

	
	//add enter funcitonality
	$('#loginbutton').bind('click', function(event) {
		$("p").remove();
		var username = $('#loginUser').val();
		var password = $('#loginPass').val();
		var postParameters = {username : username, password : password};
		$.post('/validate', postParameters, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			status = responseObject.status;
			message = responseObject.message;
			if (status == "error") {
				var message = document.createElement('p');
				message.style.textAlign = "center";
				message.style.color = "red";
				message.appendChild(document.createTextNode("Error with database, please try again."));
				$('#loginForm').submit(function(e) {
					return false;
				});
			} else if (status == "failure") {
				var message = document.createElement('p');
				message.style.textAlign = "center";
				message.style.color = "red";
				message.appendChild(document.createTextNode("Username or password not found in database, please try again."));
				$('#loginForm').submit(function(e) {
					return false;
				});
			} else {
				$('#loginForm').submit();
			}
		});
	});
	
	$("#register").bind('click', function(event) {
		console.log("noice");
		$("p").remove();
		var password = document.getElementById('pass').value;
		var confpass = document.getElementById('pass2').value;
		var username = document.getElementById('regUser').value;
		var fullname = document.getElementById('regName').value;
		//check for valid fields
		//username or full name contains only whitespace, not allowed
		if (/^\s+$/.test(username)) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Username and full name cannot contain only whitespaces, please try again."));
			$("#registerForm").append(message);
		//any of the fields are empty, not allowed
		} else if (!password || !confpass || !username || !fullname) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Please fill out all fields."));
			$("#registerForm").append(message);
		//password and confirm pass do not match, not allowed
		} else if (password != confpass) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Passwords do not match, please try again."));
			$("#registerForm").append(message);
		//username or fullname contain non-alphanumeric characters, not allowed
		} else if (!/^[a-zA-Z0-9- ]*$/.test(username) || !/^[a-zA-Z0-9- ]*$/.test(fullname)) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Username and full name fields cannot contain special symbols, please try again."));
			$("#registerForm").append(message);
		//valid inputs, now check with backend if 
		} else {
			var postParameters = {username : username, password : password, fullname : fullname};
			register(postParameters);	
		}
	})
});

/* CHANGE */

function register(postParameters, loginParam) {
	$("p").remove();
	console.log("post should happen");
	$.post('/register', postParameters, function(responseJSON) {
		console.log("response received");
		var responseObject = JSON.parse(responseJSON);
		console.log(responseObject.success);
		if (responseObject.success == 1) {
			console.log("succesful registration");
			var postParameters = {user : responseObject.user, pass : responseObject.pass};
			console.log(postParameters);			
			login(postParameters);
		//user already exists, try again
		} else if (responseObject.success == 0) {
			console.log("unsuccessful login");
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Username already exists, please try another one."));
			$("#registerForm").append(message);
		}
	});
}

function login(postParameters) {
	console.log("HEREEE");
	console.log(postParameters);
	var url = $("#loginForm").attr('action');
	console.log('url is ' + url);
	my_form=document.createElement('FORM');
	my_form.name='myForm';
	my_form.method='POST';
	my_form.action= url;

	my_tb=document.createElement('INPUT');
	my_tb.type='TEXT';
	my_tb.name='user';
	my_tb.value=postParameters.user;
	my_form.appendChild(my_tb);

	my_tb=document.createElement('INPUT');
	my_tb.type='Text';
	my_tb.name='pass';
	my_tb.value= postParameters.pass;
	my_form.appendChild(my_tb);

	document.body.appendChild(my_form);
	console.log("submitting");
	my_form.submit();
	/*$.post(url, postParameters, function(responseJSON) {
			
	}); */
}