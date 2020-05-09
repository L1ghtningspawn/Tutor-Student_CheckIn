<?php
session_start();

include("../include.php");
$u_id=$_SESSION['u_id'];
$department="SELECT d_id FROM $server_database.USER_ROLES_DEPARTMENT where ur_id in (Select ur_id from $server_database.USER_ROLES where r_id=$supervisor and u_id=$u_id)";
$result=mysqli_query($con,$department);
$row=mysqli_fetch_assoc($result);
$d_id=$row['d_id'];

#$tutor="Select ui.fname as fname,ui.lname as lname,ui.year_at_organization as year,ui.u_id as user_id from $server_database.USER_INFO ui where ui.u_id in (Select u.u_id from $server_database.USER_ROLES u where u.r_id=$tutor)";
$tutor="Select fname,lname,year_at_organization as year,u_id as user_id from USER_INFO where u_id in (Select u_id from USER_ROLES where ur_id in (SELECT ur_id FROM seesselm_Capstone.USER_ROLES_DEPARTMENT where d_id=$d_id) and r_id=$tutor)";
$result=mysqli_query($con,$tutor);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $user_info=[];
    array_push($user_info,$row['fname']);
    array_push($user_info,$row['lname']);
    array_push($user_info,$row['year']);
    array_push($user_info,$row['user_id']);
    array_push($to_web,$user_info);
}

#echo json_encode($to_web);

?>