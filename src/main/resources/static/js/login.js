jQuery(document).ready(function($) {
	$("#registerpage").hide(0);	
	$("#registerbutton").bind('click', function(event) {
		console.log("register");
		$("#registerpage").show(0);
		$("#logreg").hide(0);
	});
	$(".backbutton").bind('click', function(event) {
		console.log("asdf");
		$("#logreg").show(0);
		$("#registerpage").hide(0);
	});
	

	$('form').submit(function() {
		var username = $('#loginUser').val();
		var password = $('#loginPass').val();
		var postParameters = {username : username, password : password};
		var boolean = false;
		var response = true;
		$.post('/validate', postParameters, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			status = responseObject.status;
			message = responseObject.message;
			if (status == "error") {
				console.log("error");
				$('.Result p').css("text-align", "center");
				$('.Result p').text(message);
			} else if (status == "failure") {
				console.log("failureeee");
				$('.Result p').css("text-align", "center");
				$('.Result p').text(message);
			} else {
				console.log("set to true");
				console.log(boolean);
				boolean = true;
				console.log(boolean);
				//call function, that does what you want
			}
		});
		return boolean;
	});
	
	$("#register").bind('click', function(event) {
		console.log("noice");
		$("p").remove();
		var password = document.getElementById('pass').value;
		var confpass = document.getElementById('pass2').value;
		var username = document.getElementById('regUser').value;
		var fullname = document.getElementById('regName').value;
		
		//check for valid fields
		if (!password || !confpass || !username || !fullname) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Please fill out all fields."));
			$("#registerForm").append(message);
		} else if (password != confpass) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Passwords do not match, please try again."));
			$("#registerForm").append(message);
		} else if (!/^[a-zA-Z0-9- ]*$/.test(username) || !/^[a-zA-Z0-9- ]*$/.test(fullname)) {
			var message = document.createElement('p');
			message.style.textAlign = "center";
			message.style.color = "red";
			message.appendChild(document.createTextNode("Passwords do not match, please try again."));
			$("#registerForm").append(message);
		} else {
			console.log("shouldn't be here");
			var postParameters = {username : username, password : password, fullname : fullname};
			register(postParameters);	
		}
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