<?php
session_start();
include ("../include.php");
$ur_id=$_SESSION['Tutor_id'];
$department="SELECT d_id FROM $server_database.USER_ROLES_DEPARTMENT where ur_id=$ur_id";
$result=mysqli_query($con,$department);
$row=mysqli_fetch_assoc($result);
$d_id=$row['d_id'];

$not_assigned="Select course_name,c_id from $server_database.COURSE where d_id=$d_id and c_id not in (Select c_id from USER_ROLES_COURSE where ur_id=$ur_id)";
$result=mysqli_query($con,$not_assigned);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $course=[];
    array_push($course,$row['course_name']);
    array_push($course,$row['c_id']);
    array_push($to_web[],$course);
}

echo json_encode($to_web);