<?php
session_start();
include ('dbconfig.php');
$var=$_POST['from_ajax'];
#$var=$_SESSION["Student_id"];
$query="Select ui.fname as fname, ui.lname as lname from USER_INFO ui, USER_ROLES ur where ur.u_id=ui.u_id and ur.ur_id=$var";
#echo $query;
$result=mysqli_query($con,$query);
$fname=$var;
$lname="";
while($row=mysqli_fetch_array($result))
{
    $fname=
}

?>