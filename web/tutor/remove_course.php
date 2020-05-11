<?php
session_start();
include ('../include.php');

$urc_id=$_POST['urc_id'];
$query="Delete from $server_database.USER_ROLES_COURSE where urc_id=$urc_id";
mysqli_query($con,$query);
?>