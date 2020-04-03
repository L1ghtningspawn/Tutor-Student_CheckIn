<?php
	header("content-type: text/plain");
	include "dbconfig.php";
	include "password.php";

	if (isset($_POST['checkin_mode'])){
		$mode = $_POST['checkin_mode'];
	}

	$checkin_type = $_POST['checkin_type'];
	if ($checkin_type == 'qrcode'){

		$tutor_role_id = $_POST['tutor_role_id'];
		$student_role_id = $_POST['student_role_id'];
		$qr_server_key = $_POST['qr_server_key'];
		$course_id = $_POST['course_id'];

		//TODO check if qrcode doesn't already exist, if it does: return failure code "old_qrcode"

		$query_testQRCode =
		"select qr_key
		from SERVER_QR_KEYS
		where qr_key=?";

		$query_insertQRCode =
		"insert into SERVER_QR_KEYS
		(qr_key, date_created)
		VALUES (?,now());";

		$stmt = $con->prepare($query_testQRCode);
		$stmt->bind_param("s", $qr_server_key);
		$stmt->bind_result($qr_key);
		$stmt->execute();
		$stmt->fetch();
		if(isset($qr_key)){
			echo "FQR1";
		} else {
			$stmt->close();

			$stmt = $con->prepare($query_insertQRCode);
			$stmt->bind_param("s",$qr_server_key);
			$stmt->execute();
			$stmt->close();

			$query_session =
			"insert into TUTOR_SESSION
			(t_ur_id, s_ur_id, time_in, time_out, c_id)
			values (?, ?, now(), null, ?);";
			$stmt = $con->prepare($query_session);
			$stmt->bind_param("iii",$tutor_role_id, $student_role_id, $course_id);
			$stmt->execute();
			$stmt->close();

			echo "SC0";
		}
		//otherwise return success code

	} elseif ($mode == 'tutor'){
		$tutor_role_id = $_POST['tutor_role_id'];
		$student_email = $_POST['student_email'];

		// here use tutor id and student email and check in student to session
		$query2 =
		'select ur.ur_id from USER_ROLES as ur
		join ROLES as r on r.r_id = ur.r_id
		join USER_INFO as ui on ui.u_id = ur.u_id
		join LOGIN as l on l.login_id = ui.login_id
		and r.role_name = \'STUDENT\'
		and l.email = ?;';
		$stmt = $con->prepare($query2);
		$stmt->bind_param('s', $student_email);
		$stmt->bind_result($student_role_id);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();

		if($student_role_id){
			//check to make sure t_ur_id and s_ur_id don't have active sessions (time_out is null)
			//make initial insert with c_id of 4 for DUMMY course
			$query3 =
			'select count(ts_id)
			from TUTOR_SESSION
			where s_ur_id = ?
			and time_out is null;';
			$stmt = $con->prepare($query3);
			$stmt->bind_param('i', $student_role_id);
			$stmt->bind_result($ts_count);
			$stmt->execute();
			$stmt->fetch();
			$stmt->store_result();
			$stmt->close();

			if($ts_count == 0){
				$query4 = 'insert into TUTOR_SESSION (t_ur_id, s_ur_id, time_in, c_id) values (?,?,now(),4);';
				$stmt = $con->prepare($query4);
				$stmt->bind_param('ii', $tutor_role_id,$student_role_id);
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
			echo "FCE2";
		}
	} else{
		// insert course into active tutor session
		$student_role_id = $_POST['student_role_id'];
		$course_id = $mode;
		//echo "STUDENT ROLE ID: {$student_role_id}\n";
		//echo "COURSE ID: {$course_id}\n";
		//check for s_ur_id have active session (time_out is null) and update row
		$s_query1 =
		'select ts_id
		from TUTOR_SESSION
		where s_ur_id = ?
		and time_out is null;';
		$stmt = $con->prepare($s_query1);
		$stmt->bind_param('i', $student_role_id);
		$stmt->bind_result($ts_id);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();
		//echo "TUTOR_SESSION_ID: {$ts_id}\n";

		if($ts_id){
			$s_query2 =
			'update TUTOR_SESSION set c_id=?
			where ts_id = ?;';
			$stmt = $con->prepare($s_query2);
			$stmt->bind_param('si', $course_id, $ts_id);
			$stmt->execute();
			$row_count = $stmt->affected_rows;
			$stmt->close();

			if ($row_count == 0){
				echo "FCE0";
			} else {
				echo "SCE0";
			}
		} else{
			echo "FCE1";
		}
	}
?>
