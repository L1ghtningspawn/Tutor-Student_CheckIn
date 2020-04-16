<?php

/*

Todo: Check for existing logins.


*/


define("IN_CODE", 1);
include ("include.php");
$login = mysqli_real_escape_string($con, $_POST["email"]);
$fname = mysqli_real_escape_string($con, $_POST['fname']);
$lname = mysqli_real_escape_string($con, $_POST['lname']);
$password = mysqli_real_escape_string($con, $_POST['password']);
$password2 = mysqli_real_escape_string($con, $_POST['password2']);

$query = "select email from $server_database.LOGIN where email='$login'";
$result = mysqli_query($con,$query);
if (mysqli_num_rows($result) == 0)
{ 
    if(strcmp($password,$password2)==0)
    {
        $options = [
            'cost' => 10,
        ];
        $hashed=password_hash($password, PASSWORD_BCRYPT, $options);
        $query="Insert into $server_database.LOGIN (email,password) values ('$login','$hashed')";
        mysqli_query($con,$query);
        $login_id=mysqli_insert_id($con);
        $query="Insert into $server_database.USER_INFO (fname,lname,login_id) values ('$fname','$lname',$login_id)";
        mysqli_query($con,$query);
        $u_id=mysqli_insert_id($con);
        $query="Insert into $server_database.USER_ROLES (u_id,r_id) values ($u_id,$student)";
        mysqli_query($con,$query);
        
        header("refresh:5;url=login.php");
        echo "Success";
    }
    else
        echo "Passwords don't match";
    
}

else 
{
    
    header( "refresh:5;url=signup.html" );
    Echo "email already exists";
}





?>