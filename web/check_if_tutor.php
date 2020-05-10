<?php

session_start();

if($_SESSION['Tutor'])
	echo 'True';
else
	echo 'False';
?>