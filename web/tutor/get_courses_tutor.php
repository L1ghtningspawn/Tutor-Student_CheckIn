<?php
session_start();
include ("../include.php");
$ur_id=$_SESSION['Tutor_id'];
$department="SELECT d_id FROM $server_database.USER_ROLES_DEPARTMENT where ur_id=$ur_id";
$result=mysqli_query($con,$department);
$row=mysqli_fetch_assoc($result);
$d_id=$row['d_id'];

$assigned="select c.course_name as course_name,c.c_id as c_id,urc.urc_id as urc from COURSE c, USER_ROLES_COURSE urc where c.d_id=7 and c.c_id=urc.c_id and c.c_id in (Select c_id from USER_ROLES_COURSE where ur_id=$ur_id)";
$result=mysqli_query($con,$assigned);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $course=[];
    array_push($course,$row['course_name']);
    array_push($course,$row['c_id']);
    array_push($course,$row['urc']);
    array_push($to_web[],$course);
}

echo json_encode($to_web);