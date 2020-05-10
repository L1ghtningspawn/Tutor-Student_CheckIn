<?php

session_start();

if($_SESSION['Supervisor'])
	echo 'True';
else
	echo 'False';
?>