<?php
    session_start();
    include ('../include.php');

    $var = $_POST['sid'];

    $query= "select t_ur_id from TUTOR_SESSION where ts_id= $var;";
    $stmt = $con->prepare($query);
    $stmt->bind_result($tutor_id);
    $stmt->execute();
    $stmt->store_result();
    $stmt->fetch();

    $query_courses = "select c.c_id, c.course_name
	from COURSE c,
	USER_ROLES_COURSE urc
    where
    c.c_id=urc.c_id and
    urc.ur_id= $tutor_id;";

    $stmt = $con->prepare($query_courses);
    $stmt->bind_result($course_id, $course_name);
    $stmt->execute();
    $stmt->store_result();

    $array=[];
    while($stmt->fetch()){
        $temp=[];
        array_push($temp,$course_id);
        array_push($temp,$course_name);
        array_push($array,$temp);
    }
    echo json_encode($array);
?>
