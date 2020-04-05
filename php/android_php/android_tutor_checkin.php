<?php
	header("content-type: text/plain");
	include "dbconfig.php";
	include "password.php";

	if(isset($_POST['checkin_type'])){
		$checkin_type = $_POST['checkin_type'];
	}

	if ($checkin_type == 'qrcode'){
		$tutor_role_id = $_POST['tutor_role_id'];
		$student_role_id = $_POST['student_role_id'];
		$qr_server_key = $_POST['qr_server_key'];

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
		if($stmt->fetch()){
			echo "FQR1";
		} else {
			$stmt->close();
			$stmt = $con->prepare($query_insertQRCode);
			$stmt->bind_param("s",$qr_server_key);
			$stmt->execute();
			$stmt->close();
			echo "sc0";
		}
		//otherwise return success code

	} else{
		$tutor_email = $_POST['tutor_email'];
		$student_email = $_POST['student_email'];



		//is there an existing session?
		$query =
		'select session_id
		from MOBILE_SESSION_DATA
		where login_id=(select login_id from LOGIN where email=?);';
		$stmt = $con->prepare($query);
		$stmt->bind_param("s",$tutor_email);
		$stmt->bind_result($session_id);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();

		//echo "{$tutor_email}\n{$student_email}\n{$session_id}\n";

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
