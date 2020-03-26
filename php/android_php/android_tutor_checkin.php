<?php
	header("content-type: text/plain");
	include "dbconfig.php";
	include "password.php";

	if(isset($_POST['checkin_type'])){
		$checkin_type = $_POST['checkin_type'];
	}

	if ($checkin_type == 'qrcode'){
		//TODO
		//check if qrcode doesn't already exist, if it does: return failure code "old_qrcode"
		//otherwise return success code
	} else{
		$tutor_email = $_POST['tutor_email'];
		$student_email = $_POST['student_email'];

		// here use tutor and student email and check in student to session
		$query1 =
		'select ur.ur_id from USER_ROLES as ur
		join ROLES as r on r.r_id = ur.r_id
		join USER_INFO as ui on ui.u_id = ur.u_id
		join LOGIN as l on l.login_id = ui.login_id
		and r.role_name = \'TUTOR\'
		and l.email = ?;';
		$stmt = $con->prepare($query1);
		$stmt->bind_param('s', $tutor_email);
		$stmt->bind_result($tid);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();
		//echo "TUTOR_ID: {$tid}\n";

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

		//check to make sure t_ur_id and s_ur_id don't have active sessions (time_out=00:00:00)
		//make initial insert with c_id of 4 for DUMMY course
		$query3 =
		'select count(ts_id)
		from TUTOR_SESSION 
		where t_ur_id = ?
		and s_ur_id = ?
		and time_out = \'00:00:00\';';
		$stmt = $con->prepare($query3);
		$stmt->bind_param('ii', $tid,$sid);
		$stmt->bind_result($ts_count);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();

		if($ts_count == 0){
			$query4 = 'insert into TUTOR_SESSION (t_ur_id, s_ur_id, c_id) values (?,?,4);';
			$stmt = $con->prepare($query4);
			$stmt->bind_param('ii', $tid,$sid);
			$stmt->execute();

			$row_count = $stmt->affected_rows;

			if ($row_count == 0){
				echo "FC2";
			} else {
				echo "SC0";
			}
			$stmt->close();
		} else{
			echo "FC1";
		}
	}
?>
