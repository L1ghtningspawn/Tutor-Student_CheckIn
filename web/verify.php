<?php
session_start();


function checkSupervisor($login_id)
{
    include ('roles.php');
    $query="Select ur_id from $server_database.USER_ROLES where u_id=$login_id";
    
}

/*function checkTutor()

function checkStudent()

function checkPassword()*/










define("IN_CODE", 1);
include ("../php/android_php/dbconfig.php");
$login = mysqli_real_escape_string($con, $_POST["email"]);
$password = mysqli_real_escape_string($con, $_POST['password']);

$query = "Select login_id,password from $server_database.LOGIN where email='$login'";
$result=mysqli_query($con,$query);
if(mysqli_num_rows($result)==0)
{
    echo "No user found";
}
else
{
    $row=mysqli_fetch_array($result);
    $login_id=$row['login_id'];
    $hash=$row['password'];
    #echo $login_id;
    if(password_verify($password, $hash))
    {
        $_SESSION['login_id']=$login_id;
        $_SESSION['email']=$login;
        if(checkSupervisor($login_id))
        {

        }
    }
    else
    {
        echo "No match";
    }
}











?>