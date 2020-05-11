<?php
session_start();
include ('../include.php');

$u_id=$_SESSION['u_id'];
include ('../include.php');
$departments="Select dept_name,d_id from $server_database.DEPARTMENT where d_id in(Select d_id from $server_database.USER_ROLES_DEPARTMENT where ur_id in (Select ur_id from $server_database.USER_ROLES where u_id=$u_id and r_id=$supervisor))";
$result=mysqli_query($con,$departments);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $temp=[$row['dept_name'],$row['d_id']];
    array_push($to_web,$temp);
}


echo json_encode($to_web);








/*$ur_id_array=$_SESSION['Supervisor_id'];
$ur_id_index=$_SESSION['index'];
$ur_id=$ur_id_array[$ur_id_index];*/
