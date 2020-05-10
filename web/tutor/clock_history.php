<?php
session_start();
include ('../include.php');
$t_ur_id=$_SESSION['Tutor_id'];
$query="Select punch_in, punch_out from $server_database.PAYROLL where t_ur_id=$t_ur_id order by transaction_id desc LIMIT 10";
$result=mysqli_query($con,$query);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $temp=[$row['punch_in'],$row['punch_out']];
    array_push($to_web,$temp);
}

echo json_encode($to_web);

?>