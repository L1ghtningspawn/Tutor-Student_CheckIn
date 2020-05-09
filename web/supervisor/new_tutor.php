<?php

include ('../include.php');
$from_web=$_POST['u_id'];
$query="Insert into $server_database.USER_ROLES (u_id,r_id) values ($from_web,5)";
mysqli_query($con,$query);

$ur_id = mysqli_insert_id($con);
$query2="Insert into $server_database.USER_ROLES_DEPARTMENT (ur_id,d_id) values ($ur_id,7)";
mysqli_query($con,$query2);
?>