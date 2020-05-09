<?php
session_start();

include("../include.php");

$tutor="Select ui.fname as fname,ui.lname as lname,ui.year_at_organization as year,ui.u_id as user_id from $server_database.USER_INFO ui where ui.u_id in (Select u.u_id from $server_database.USER_ROLES u where u.r_id=$tutor)";
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