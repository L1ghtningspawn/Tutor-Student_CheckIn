<?php
session_start();
include "../include.php";

//test connection
if (!$con){
    die("FAILED" . mysqli_connect_error());
}

$ts_id = $_POST['session_id'];

$time_out_query = "update TUTOR_SESSION
set time_out = now()
where time_out is null
and ts_id={$ts_id}";

$null_checkout_stmt = $con->prepare($time_out_query);
$null_checkout_stmt->execute();
$row_count = $con->affected_rows;
$null_checkout_stmt->close();
if($row_count>0){
    echo "You successfully checked out!";
}
else{
    echo "Something unexpected happened! Try again later.";
}




?>
