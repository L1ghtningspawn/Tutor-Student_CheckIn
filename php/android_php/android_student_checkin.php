<?php
	header("content-type: text/plain");
	include "dbconfig.php";
	include "password.php";

	if (isset($_POST['checkin_mode'])){
		$mode = $_POST['checkin_mode'];
	}
	if ($mode == 'tutor'){
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
		where s_ur_id = ?
		and time_out = \'00:00:00\';';
		$stmt = $con->prepare($query3);
		$stmt->bind_param('i', $sid);
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
			$stmt->close();

			if ($row_count == 0){
				echo "FC2";
			} else {
				echo "SC0";
			}
		} else{
			echo "FC1";
		}
	} else{
		echo "STUDENT_MODE!\n";
		// checkin mode is student entry
		$student_email = $_POST['email'];
		echo "STUDENT_EMAIL: {$student_email}\n";

		if(isset($_POST['checkin_type'])){
			$checkin_type = $_POST['checkin_type'];
		}
		if ($checkin_type == 'qrcode'){
		//TODO
		//check if qrcode doesn't already exist, if it does: return failure code "old_qrcode"
		//otherwise return success code
		} else{
			if (isset($_POST['checkin_time'])){
				$checkin_time = $_POST['checkin_time'];
			}
			$s_query =
			'select ur.ur_id from USER_ROLES as ur
			join ROLES as r on r.r_id = ur.r_id
			join USER_INFO as ui on ui.u_id = ur.u_id
			join LOGIN as l on l.login_id = ui.login_id
			and r.role_name = \'STUDENT\'
			and l.email = ?;';
			$stmt = $con->prepare($s_query);
			$stmt->bind_param('s', $student_email);
			$stmt->bind_result($sid);
			$stmt->execute();
			$stmt->fetch();
			$stmt->store_result();
			$stmt->close();
			echo "STUDENT_ID: {$sid}\n";

			//check for s_ur_id have active session (time_out=00:00:00) and update row
			$s_query1 =
			'select ts_id
			from TUTOR_SESSION 
			where s_ur_id = ?
			and time_out = \'00:00:00\';';
			$stmt = $con->prepare($s_query1);
			$stmt->bind_param('i', $sid);
			$stmt->bind_result($ts_id);
			$stmt->execute();
			$stmt->fetch();
			$stmt->store_result();
			$stmt->close();
			echo "TUTOR_SESSION_ID: {$ts_id}\n";

			if($ts_id){
				$s_query2 = 
				'update TUTOR_SESSION set time_in=?
				where ts_id = ?;';
				$stmt = $con->prepare($s_query2);
				$stmt->bind_param('si', $checkin_time, $ts_id);
				$stmt->execute();
				$row_count = $stmt->affected_rows;
				$stmt->close();

				if ($row_count == 0){
					echo "FCE0 - CHECKIN_FAILED";
				} else {
					echo "SCE0 - CHECKIN_SUCCESSFUL";
				}
			} else{
				echo "FCE1 - NO_VALID_SESSION_EXISTS";
			}
		}
	}
?>
