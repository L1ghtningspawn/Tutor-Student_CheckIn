<?
session_start();
include ("include.php");
$time=time();
$t_ur_id=$_SESSION['Tutor_id'];
$query="Insert into $server_database.PAYROLL (t_ur_id,punch_in,punch_out) values ($t_ur_id,FROM_UNIXTIME($time),0)";
mysqli_query($con,$query);
?>