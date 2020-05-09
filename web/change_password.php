<?php

session_start();

include ("include.php");

$password_old = mysqli_real_escape_string($con, $_POST['password_old']);

$password_new = mysqli_real_escape_string($con, $_POST['password_new']);

$password_new_confirm = mysqli_real_escape_string($con, $_POST['password_new_confirm']);



$id=$_SESSION['u_id'];

if(strcmp($password_new,$password_new_confirm)==0)

{

    $query = "select password from $server_database.LOGIN where login_id=$id";

    $result = mysqli_query($con, $query);

    $row=mysqli_fetch_array($result);

    $hash=$row['password'];

    if(password_verify($password_old, $hash))

    {


        $pass=password_hash($password_new, PASSWORD_BCRYPT);

        $query="Update $server_database.LOGIN set password='$pass' where login_id=$id";

        mysqli_query($con,$query);

        #header( "Location: WHERE.php" );

        echo "Successful password update";

    }

    else

    {

        #header( "refresh:10;url=change_password.php" );

        echo "incorrect old password";

    }

}

else

{

    header( "refresh:10;url=change_password.php" );

    echo "New password does not match";

}
