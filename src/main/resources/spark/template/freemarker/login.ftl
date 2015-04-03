<!DOCTYPE html>
<html>

<head>

  <meta charset="UTF-8">

  <title>Login</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
  <link rel="stylesheet" type="text/css" href="css/calendar.css">
  <link rel="stylesheet" href="css/login.css">
  <script src="js/jquery-2.1.1.js"></script>
  <script src="js/login.js"></script>

</head>

<body>
  <div class="login-card">
  	<div class="logreg">
  		<img src="img/logo.png" id ="logo"><br>
  		<button type="button" class="btn btn-primary" id ="loginbutton" name="loginbutton">Login</button>
	    <button type="button" class="btn btn-primary" id ="registerbutton" name="registerbutton">Register</button>
  	</div>
  	
  	<!login page>
    <div class="loginpage">
	<h1>Log-in</h1><br>
	  <form method = "POST" action="/loginattempt">
	    <input type="text" name="user" placeholder="Username">
	    <input type="password" name="pass" placeholder="Password">-
	    <input type="submit" name="login" class="btn btn-primary" value="Login">
	  </form>
	  
	  <input type="image" src="img/leftarrow.png" class ="backbutton" name="backbutton">
	   <div class="Result">${message}<br></p>
    </div>
    
    <!register page>
    <div class="registerpage">
    <h1>Register</h1><br>
	  <form>
	    <input type="text" name="user" placeholder="Username">
	    <input type="password" name="pass" placeholder="Password">
	    <input type="password" name="pass" placeholder="Confirm password">
	    <input type="text" name="name" placeholder="Full name">
	    <input type="text" name="email" placeholder="Google E-mail">	    
	    <input type="submit" name="register" class="btn btn-primary" value="Register">
	  </form>
	  
	  <input type="image" src="img/leftarrow.png" class ="backbutton" name="backbutton">
	  
    </div>
  </div>

<!-- <div id="error"><img src="https://dl.dropboxusercontent.com/u/23299152/Delete-icon.png" /> Your caps-lock is on.</div> -->
  <script src='http://codepen.io/assets/libs/fullpage/jquery_and_jqueryui.js'></script>

</body>

</html>