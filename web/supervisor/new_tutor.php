<?php
session_start();
include ('../include.php');
$from_web=$_POST['u_id'];
$query="Insert into $server_database.USER_ROLES (u_id,r_id) values ($from_web,5)";
mysqli_query($con,$query);

$ur_id=$_SESSION['Supervisor_id'];
#echo $ur_id;
$department="SELECT d_id FROM $server_database.USER_ROLES_DEPARTMENT where ur_id=$ur_id";
$result1=mysqli_query($con,$department);
$row1=mysqli_fetch_assoc($result1);
$d_id=$row1['d_id'];

$ur_id = mysqli_insert_id($con);
$query2="Insert into $server_database.USER_ROLES_DEPARTMENT (ur_id,d_id) values ($ur_id,$d_id)";
mysqli_query($con,$query2);

$query3="Select c_id from $server_database.COURSE where d_id=$d_id and course_name like '%QUESTION%'";
$result2=mysqli_query($con,$query3);
$row2=mysqli_fetch_assoc($result2);
$c_id=$row2['c_id'];

$query4="Insert into $server_database.USER_ROLES_COURSE (ur_id,c_id) values ($ur_id,$c_id)";
mysqli_query($con,$query4);

header("Location: account_management.html");
?>