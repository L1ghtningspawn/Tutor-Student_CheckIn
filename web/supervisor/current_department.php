<?php

$ur_id=$_SESSION['Supervisor_id'];
$departments="Select dept_name,d_id from $server_database.DEPARTMENT where d_id in(Select d_id from $server_database.USER_ROLES_DEPARTMENT where ur_id=$ur_id)";
$result=mysqli_query($con,$departments);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $temp=[$row['dept_name'],$row['d_id']];
    array_push($to_web,$temp);
}


echo json_encode($to_web);