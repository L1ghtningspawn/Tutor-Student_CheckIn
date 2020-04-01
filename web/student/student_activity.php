<?php
session_start();
include ('../dbconfig.php');
#$var=$_POST['var_from_ajax'];
$var=$_SESSION["Student_id"];
$query="Select ts_id,t_ur_id,s_ur_id,time_in,time_out,c_id from $server_database.TUTOR_SESSION where s_ur_id=$var order by ts_id desc";
echo $query;
$result=mysqli_query($con,$query);
$array=[];
while($row=mysqli_fetch_array($result))
{
    $temp=[];
    array_push($temp,$row['ts_id']);
    array_push($temp,$row['t_ur_id']);
    array_push($temp,$row['s_ur_id']);
    array_push($temp,$row['time_in']);
    array_push($temp,$row['time_out']);
    array_push($temp,$row['c_id']);
    array_push($array,$temp);
}

echo json_encode($array);

?>