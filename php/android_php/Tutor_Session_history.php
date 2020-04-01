<?php
header("content-type: text/plain");
include "dbconfig.php";
include "password.php";


//test connection
if (!$con){
    die("FAILED" . mysqli_connect_error());
}

$ur_id = $_POST['user_role_id'];

// $session_history_query= "select ui.fname, ui.lname, ts.time_in, ts.time_out, ts.s_ur_id from USER_INFO ui, TUTOR_SESSION  ts, USER_ROLES ur
// where ts.s_ur_id=ur.ur_id and ur.u_id=ui.u_id
// and ts.t_ur_id= {$ur_id}
// order by ts.time_out is not null, ts.time_out desc ;";

$session_history_query =
"select ui.fname, ui.lname, ts.time_in, ts.time_out, ts.s_ur_id, ts.c_id, crs.course_name
from USER_INFO ui, TUTOR_SESSION ts, USER_ROLES ur, COURSE crs
where ts.s_ur_id = ur.ur_id and
	ur.u_id = ui.u_id and
    ts.c_id = crs.c_id and
    ts.t_ur_id = {$ur_id}
order by ts.time_out is not null, ts.time_out desc ;";

$history_stmt = $con->prepare($session_history_query);
$history_stmt->bind_result($fname,$lname,$time_in,$time_out, $s_ur_id, $course_id, $course_name);
$history_stmt->execute();
$arr = array();
$count = 0;
while($history_stmt->fetch()){
    $arr[$count]['fname'] = $fname;
    $arr[$count]['lname'] = $lname;
    $arr[$count]['time_in'] = $time_in;
    $arr[$count]['time_out'] = $time_out;
    $arr[$count]['s_ur_id'] = $s_ur_id;
    $arr[$count]['course_id'] = $course_id;
    $arr[$count]['course_name'] = $course_name;
    $count++;
}
$history_stmt->close();
$json_history = json_encode($arr);


echo $json_history;
?>
