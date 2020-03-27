<?php
	header("content-type: text/plain");
	include 'dbconfig.php';
	include "password.php";

	$email = $_POST['email'];
	echo "EMAIL: {$email}\n";
	$query_id =
	'select ur.ur_id from USER_ROLES as ur
	join ROLES as r on r.r_id = ur.r_id
	join USER_INFO as ui on ui.u_id = ur.u_id
	join LOGIN as l on l.login_id = ui.login_id
	and r.role_name = \'TUTOR\'
	and l.email = ?;';
	$stmt = $con->prepare($query_id);
	$stmt->bind_param('s', $email);
	$stmt->bind_result($t_ur_id);
	$stmt->execute();
	$stmt->fetch();
	$stmt->store_result();
	$stmt->close();
	echo "UR_ID: {$t_ur_id}\n";

	$query_courses =
	'select B.c_id, B.course_name
	from USER_ROLES A, COURSE B, USER_ROLES_COURSE C
	where A.ur_id = ? and
			A.ur_id = C.ur_id and
			C.c_id=B.c_id;
	';
	$stmt = $con->prepare($query_courses);
	$stmt->bind_param('i',$t_ur_id);
	$stmt->bind_result($course_id, $course_name);
	$stmt->execute();
	$stmt->store_result();
	
	//make course_id list as well
	$stmt->fetch();
	echo "COURSE_ID: {$course_id} COURSE_NAME: {$course_name}\n";
	$course_list = ";$course_name";
	$couseid_list = ";$course_id";
	while($stmt->fetch()){
	echo "COURSE_ID: {$course_id} COURSE_NAME: {$course_name}\n";
	$course_list .= "^$course_name";
	$couse_list .= "^$course_id";
	}

	if($stmt->num_rows > 0){
	echo "STc0;".$course_id.$course_list;
	} else {
	echo "FTc0";
	}

?>
