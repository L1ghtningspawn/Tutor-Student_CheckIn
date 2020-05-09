<?php
session_start();


function checkRoles($login_id)
{
    include ('include.php');
    $_SESSION['Supervisor']=false;
    $_SESSION['Tutor']=false;
    $_SESSION['Student']=false;
    $query="Select ur_id,r_id from $server_database.USER_ROLES where u_id=$login_id";
    #echo $query;
    $results=mysqli_query($con,$query);
    $tutor_id=[];
    while($row=mysqli_fetch_assoc($results))
    {
        if($row['r_id']==$supervisor)
        {    
            $_SESSION['Supervisor']=true;
            $_SESSION['Supervisor_id']=$row['ur_id'];
        }
        if($row['r_id']==$tutor)
        {
            $_SESSION['Tutor']=true;
            #$_SESSION['Tutor_id']=$row['ur_id'];
            array_push($tutor_id,$row['ur_id']);
        }
        if($row['r_id']==$student)
        {
            $_SESSION['Student']=true;
            $_SESSION['Student_id']=$row['ur_id'];
        }
    }
    if(sizeof($tutor_id)>0)
    {
        $_SESSION['Tutor_id']=$tutor_id;
        $_SESSION['Tutor_index']=0;
    }
}









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
    $query = "Select u_id from $server_database.USER_INFO where login_id=$login_id";
    $result=mysqli_query($con,$query);
    $row=mysqli_fetch_array($result);
    $u_id=$row['u_id'];
    #echo $login_id;
    if(password_verify($password, $hash))
    {
        #echo "In password";
        $_SESSION['login_id']=$login_id;
        $_SESSION['u_id']=$u_id;
        $_SESSION['email']=$login;
        checkRoles($u_id);
        if($_SESSION['Supervisor'])
            header("Location: supervisor_info.php");
        elseif($_SESSION['Tutor'])
            header("Location: tutor_info.php");
        elseif($_SESSION['Student'])
            header("Location: student_info.php");
    }
    else
    {
        echo "Email/password mismatch";
    }
}











?>