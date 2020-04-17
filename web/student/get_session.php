<?php

session_start();
$array=[];
array_push($array,$_SESSION['u_id']);
array_push($array,$_SESSION['fname']);
array_push($array,$_SESSION['lname']);
array_push($array,$_SESSION['login_id']);
array_push($array,$_SESSION['email']);
array_push($array,$_SESSION['year_at_organization']);
if($_SESSION['Student'])
{
    array_push($array,"Student");
    array_push($array,$_SESSION['Student_id']);
}
else
{
    array_push($array,"Not-Student");
    array_push($array,"0");
}
#tutor array info
#supervisor array info

echo json_encode($array);
?>