<?php
session_start();

include ("include.php");
$u_id=$_SESSION['u_id'];
$query="Select u_id,fname,lname,login_id,year_at_organization from $server_database.USER_INFO where u_id=$u_id";
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
header("Location: ./student/student.html");

?>