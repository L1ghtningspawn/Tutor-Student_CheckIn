<?php

include ('../include.php');
$from_web=$_POST['u_id'];
$query="Insert into $server_database.USER_ROLES (u_id,r_id) values ($from_web,5)";
mysqli_query($con,$query);

$u_id=$_SESSION['u_id'];
$department="SELECT d_id FROM $server_database.USER_ROLES_DEPARTMENT where ur_id in (Select ur_id from $server_database.USER_ROLES where r_id=$supervisor and u_id=$u_id)";
$result=mysqli_query($con,$department);
$row=mysqli_fetch_assoc($result);
$d_id=$row['d_id'];

$ur_id = mysqli_insert_id($con);
$query2="Insert into $server_database.USER_ROLES_DEPARTMENT (ur_id,d_id) values ($ur_id,$d_id)";
mysqli_query($con,$query2);
?>