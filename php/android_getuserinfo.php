<?php
	header("content-type: text/plain");
	include 'dbconfig.php';
	include "password.php";
	
	//this script should get whatever info is needed.
	//tutor_email, student_email, tutor_name, student_name, etc.
	
	$get_type = $_POST['get_type'];
	$user_role_id = $_POST['user_role_id'];
	//echo "get_type: {$get_type}\n";
	//echo "user_role_id: {$user_role_id}\n";
	if($get_type == 'name'){

		$query_name =
		'select concat(ui.fname, " ", ui.lname) as name
		from USER_INFO ui 
		join USER_ROLES ur on ur.u_id = ui.u_id
		where ur.ur_id =?';

		$stmt = $con->prepare($query_name);
		$stmt->bind_param('i', $user_role_id);
		$stmt->bind_result($name);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();
		
		if($name){
			echo "SGN0;{$name}";
		}
		else{
			echo "FGN0";
		}		
	} elseif($get_type == 'email'){
		//SGN0 - return name
		//FGN0 - failed to get name

		$query_email =
		'select l.email
		from LOGIN l
		join USER_INFO ui on ui.login_id = l.login_id
		join USER_ROLES ur on ur.u_id = ui.u_id
		where ur.ur_id =?';

		$stmt = $con->prepare($query_email);
		$stmt->bind_param('i', $user_role_id);
		$stmt->bind_result($email);
		$stmt->execute();
		$stmt->fetch();
		$stmt->store_result();
		$stmt->close();
		
		if($email){
			echo "SGE0;{$email}";
		}
		else{
			echo "FGE0";
		}
	}
	
?>