<?php

session_start();

session_destroy();

?>

<html>

<style>

	.login{

		position:relative;

		width:300px;

		height:400px;

		margin:auto;

		text-align:center;

		border:1px solid black;

		top:60px;

	}



	.button{

		color:white;

		padding: 10px 100px;

		background-color:black;

		border:none;

		text-align: center;

		text-decoration: none;

		display:inline-block;

		font-size:16px;

		margin:4px 2px;

		cursor:pointer;

	}



	p.text{

		font-size:25px;

	}

	.input{

		padding:10px 34px;

		text-align:center;

		text-decoration: none;

		font-size:16px;

		margin:4px 2px;

	}

</style>

<head>

	<meta charset="UTF-8">

	<link rel="stylesheet" href="style/style.css">

	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

</head>

<body>

	<div class="header">

		<img class="logo" src="images/logo.png">

		<button class="head-btn" id="Login" onclick="window.location.href='login.html'">Login</button>

		<button class="head-btn" id="sign-up" onclick="window.location.href='signup.html'">Sign-up</button>

	</div>



	<div class="webpage1">

		<div class="login">

			<p class="text">Login</p>

			<form action="verify.php" method="post">

				<input class="input" type="text" name="email" placeholder="Email Address">

				<br><br>

				<input class="input" type="password" name="password" placeholder="Password">

				<br><br>

				<input type="hidden" name="login" value="set">

				<input class="button" type="submit" value="Login">

			</form>



			<a href="signup.html">Create Account</a><br>

			<a href="">Forgot Password?</a>

		</div>

	</div>

</body>

</html>
