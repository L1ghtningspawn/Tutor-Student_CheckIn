<?php
session_start();
include ('../include.php');
$from_web=$_POST['ur_id'];
#echo $from_web;
#$query="Insert into $server_database.USER_ROLES (u_id,r_id) values ($from_web,5)";
#$query="Select ur_id from $server_database.USER_ROLES where u_id=$from_web and r_id=$tutor";
#echo $query;
#echo $query;
#$result=mysqli_query($con,$query);
#$row=mysqli_fetch_assoc($result);
#$ur_id=$row['ur_id'];
$query="Update $server_database.USER_ROLES set r_id=$remove_tutor where ur_id=$from_web";
#echo $query;
mysqli_query($con,$query);



?>