<!DOCTYPE html>
<html>

<head>

  <meta charset="UTF-8">

  <title>Login</title>

  <link rel="stylesheet" type="text/css" href="\css/bootstrap.css">
  <link rel="stylesheet" type="text/css" href="\css/calendar.css">
  <link rel="stylesheet" type="text/css" href="\css/login.css">
  <script src="\js/jquery-2.1.1.js"></script>
  <script src="\js/login.js"></script>

</head>

<body>
  <div class="login-card">
  	<div id="logreg">
  		<img src="\img/logo.png" id ="logo"><br>
		${form}
	    <input type="text" name="user" id="loginUser" placeholder="Username" required>
	    <input type="password" name="pass" id="loginPass" placeholder="Password" required>
<!-- 	    changed from type submit to type button -->
	    <input type="button" name="login" id="loginbutton" class="btn btn-primary" value="Login">
	  </form>
	    <button type="button" class="btn btn-primary" id ="registerbutton" name="registerbutton">Register</button>
	   <div class="Result">${message}</div>
  	</div>
    
    <!register page>
    <div id="registerpage">
    <h1>Register</h1><br>
<!--     	direct to login page -->
		<form id="registerForm">
		    <input type="text" name="user" id="regUser" placeholder="Username" required>
		    <input type="password" name="pass" id="pass" placeholder="Password" required>
		    <input type="password" name="pass" id="pass2" placeholder="Confirm password" required>
		    <input type="text" name="name" id="regName" placeholder="Full name" required>
<!-- 		    change from type submit to type button -->
		    <input id = "register" type="button" name="register" class="btn btn-primary" value="Register">
		</form>
	  <input type="image" src="\img/leftarrow.png" class ="backbutton" name="backbutton">
    </div>
  </div>
  
  <!-- change register button to make connection to google api -->

<!-- <div id="error"><img src="https://dl.dropboxusercontent.com/u/23299152/Delete-icon.png" /> Your caps-lock is on.</div> -->
  <script src='http://codepen.io/assets/libs/fullpage/jquery_and_jqueryui.js'></script>

</body>

</html>