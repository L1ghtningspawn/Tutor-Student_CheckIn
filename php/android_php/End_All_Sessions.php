<?php
header("content-type: text/plain");
include "dbconfig.php";
include "password.php";

//test connection
if (!$con){
    die("FAILED" . mysqli_connect_error());
}

$t_ur_id = $_POST['user_role_id'];

$time_out_query = "update TUTOR_SESSION 
set time_out = now()
where time_out is null 
and t_ur_id={$t_ur_id};";


$null_checkout_stmt = $con->prepare($time_out_query);
$null_checkout_stmt->execute();
$row_count = $stmt->affected_rows;
$null_checkout_stmt->close();
if($row_count>0){
    echo "S01";
}
else{
    echo "?";
}

?>