<?php
header("content-type: text/plain");
include "dbconfig.php";
include "password.php";


//test connection
if (!$con){
    die("FAILED" . mysqli_connect_error());
}

$query ="select ur.ur_id from USER_ROLES as ur
        join ROLES as r on r.r_id = ur.r_id
        join USER_INFO as ui on ui.u_id = ur.u_id
        join LOGIN as l on l.login_id = ui.login_id
        and r.role_name = 'TUTOR'
        and l.email = ?;";


$stmt = $con->prepare($query);
$stmt->bind_param("s",$email);
$stmt->bind_result($ur_id);
$stmt->execute();
$stmt->fetch();
$stmt->store_result();
$stmt->close();

$clock_out_query = "update TUTOR_SESSION
set time_out = now()
where time_out is null and t_ur_id=" . $ur_id . ";";

$all_clock_out_stmt = $con->prepare($clock_out_query);
$all_clock_out_stmt->execute();


?>