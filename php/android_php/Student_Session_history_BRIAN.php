<?php
header("content-type: text/plain");
include "dbconfig.php";
include "password.php";


//test connection
if (!$con){
    die("FAILED" . mysqli_connect_error());
}

$email = $_POST['email'];

//query for user role that contains email and is a tutor role
$query ="select ur.ur_id from USER_ROLES as ur
        join ROLES as r on r.r_id = ur.r_id
        join USER_INFO as ui on ui.u_id = ur.u_id
        join LOGIN as l on l.login_id = ui.login_id
        and r.role_name = 'STUDENT'
        and l.email = ?;";

$stmt = $con->prepare($query);
$stmt->bind_param("s",$email);
$stmt->bind_result($ur_id);
$stmt->execute();
$stmt->fetch();
$stmt->store_result();
$stmt->close();
$session_history_query= "select ui.fname, ui.lname, ts.time_in, ts.time_out from USER_INFO ui, TUTOR_SESSION  ts, USER_ROLES ur
where ts.s_ur_id=ur.ur_id and ur.u_id=ui.u_id
and ts.t_ur_id=" . $ur_id . ";";


$history_stmt = $con->prepare($session_history_query);
$history_stmt->bind_result($fname,$lname,$time_in,$time_out);
$history_stmt->execute();
$arr = array();
$count = 0;
while($history_stmt->fetch()){
    $arr[$count]['fname'] = $fname;
    $arr[$count]['lname'] = $lname;
    $arr[$count]['time_in'] = $time_in;
    $arr[$count]['time_out'] = $time_out;
    $count++;
}
$history_stmt->close();
$json_history = json_encode($arr);

echo $json_history;

?>
