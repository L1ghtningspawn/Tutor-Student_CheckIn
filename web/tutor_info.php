<?php
session_start();
include ("include.php");
$u_id=$_SESSION['u_id'];
$ur_id=$_SESSION['Tutor_id'];
$query="Select u_id,fname,lname,login_id,year_at_organization from $server_database.USER_INFO where u_id=$u_id";
echo $query;
echo "<br>";
$result=mysqli_query($con,$query);
while($row=mysqli_fetch_assoc($result))
{
$fname=$row['fname'];
$lname=$row['lname'];
$year_at_organization=$row['year_at_organization'];
$_SESSION['fname']=$fname;
$_SESSION['lname']=$lname;
$_SESSION['year_at_organization']=$year_at_organization;
}


$query="Select urc_id,ur_id,c_id from $server_database.USER_ROLES_COURSE where ur_id=$ur_id";
echo $query;
echo "<br>";
$urc_c_id=[];
$result=mysqli_query($con,$query);
while($row=mysqli_fetch_assoc($result))
{
    $input=[$row['urc_id'],$row['c_id']];
    array_push($urc_c_id,$input);
}
$_SESSION['tutor_urc_c_id']=$urc_c_id;

$query="Select urd_id,ur_id,d_id from $server_database.USER_ROLES_DEPARTMENT where ur_id=$ur_id";
$urd_d_id=[];
echo $query;
echo "<br>";
$result=mysqli_query($con,$query);
while($row=mysqli_fetch_assoc($result))
{
    $input=[$row['urd_id'],$row['d_id']];
    array_push($urc_c_id,$input);
}
$_SESSION['tutor_urd_d_id']=$urd_d_id;


header("Location: ./tutor/tutor.html");

?>