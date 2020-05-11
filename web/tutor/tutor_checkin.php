<?php
session_start();
include("../include.php");

$tid = $_SESSION['Tutor_id'];
$student_email = $_POST['email'];

//echo "<br>tid = $tid (tutor_id)";
// echo "<br>student_email: $student_email";

//echo "{$tutor_email}\n{$student_email}\n{$session_id}\n";

// here use tutor and student email and check in student to session

$query2 =
'select ur.ur_id from USER_ROLES as ur
join ROLES as r on r.r_id = ur.r_id
join USER_INFO as ui on ui.u_id = ur.u_id
join LOGIN as l on l.login_id = ui.login_id
and r.role_name = \'STUDENT\'
and l.email = ?;';
$stmt = $con->prepare($query2);
$stmt->bind_param('s', $student_email);
$stmt->bind_result($sid);
$stmt->execute();
$stmt->fetch();
$stmt->store_result();
$stmt->close();
//echo "STUDENT_ID: {$sid}\n";

//echo("<br>sid: $sid (student id)");

//check to make sure t_ur_id and s_ur_id don't have active sessions (time_out=00:00:00)
//make initial insert with c_id of 4 for DUMMY course
$query3 =
'select count(ts_id)
from TUTOR_SESSION
where t_ur_id = ?
and s_ur_id = ?
and (time_out = \'00:00:00\'
or time_out is null);';
$stmt = $con->prepare($query3);
$stmt->bind_param('ii', $tid,$sid);
$stmt->bind_result($ts_count);
$stmt->execute();
$stmt->fetch();
$stmt->store_result();
$stmt->close();

//echo("<br>ts_count = $ts_count (# tutor sessions with tid=$tid and sid=$sid)");

if($ts_count == 0){
  $query4 = 'insert into TUTOR_SESSION (t_ur_id, s_ur_id, c_id) values (?,?,4);';
  $stmt = $con->prepare($query4);
  $stmt->bind_param('ii', $tid,$sid);
  $stmt->execute();

  $row_count = $stmt->affected_rows;

  //echo("<br>row_count = $row_count (number of rows inserted)");

  if ($row_count == 0){
    echo
    "<script>
      alert('Check-In Failed!');
      window.location.href='tutor.html';
    </script>";
  } else {
    echo
    "<script>
      alert('{$student_email} was successfully checked in!');
      window.location.href='tutor.html';
    </script>";
  }
  $stmt->close();
} else{
  echo
  "<script>
    alert('{$student_email} is already checked in!');
    window.location.href='tutor.html';
  </script>";
}
?>
