<?php
	header("content-type: text/plain");
	include 'dbconfig.php';
	include "password.php";
	
	//this script should get whatever info is needed.
	//tutor_email, student_email, etc.
	
	$get_type = $_POST['get_type'];
	if($get_type == 'tutor_email'){
		//SGE0 - return email
		//FGE0 - return no active session
		//FGE1 - failed to get email
		$email = $_POST['email'];
		echo "EMAIL: {$email}\n";
		
		$query_email =
		'select l.email
		from LOGIN l
		join USER_INFO ui on ui.login_id = l.login_id
		join USER_ROLES ur on ur.u_id = ui.u_id
		join TUTOR_SESSION ts on ts.t_ur_id = ur.ur_id
		where ts.s_ur_id = (select ur.ur_id from USER_ROLES as ur
						join ROLES as r on r.r_id = ur.r_id
						join USER_INFO as ui on ui.u_id = ur.u_id
						join LOGIN as l on l.login_id = ui.login_id
						and r.role_name = \'STUDENT\'
						and l.email = ?)
		and ts.time_out = \'00:00:00\';';
		$stmt = $con->prepare($query_email);
		$stmt->bind_param('s', $email);
		$stmt->bind_result($tutor_email);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$row_count = $stmt->affected_rows;
		$stmt->close();
		echo "TUTOR_EMAIL: {$tutor_email}\n";
		
		if($row_count > 0){
			echo "SGE0;{$tutor_email}";

		}
		else{
			echo "FGE0";
		}
	}
	
?>