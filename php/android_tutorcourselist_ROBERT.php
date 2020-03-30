<?php
include 'dbconfig.php';

if(isset($_POST['tutorcourselist'])){
  $user_role_id = $_POST['user_role_id'];
  $email = $_POST['email'];
  $session_id = $_POST['session_id'];

  if($session_id_exists){
      $query =
      ' select B.c_id, B.course_name
        from USER_ROLES A, COURSE B, USER_ROLES_COURSE C
        where A.ur_id = ? and
        		A.ur_id = C.ur_id and
        		C.c_id=B.c_id;
      ';
      $stmt = $con->prepare($query);
      $stmt->bind_param('i',$user_role_id);
      $stmt->bind_result($course_id, $course_name);
      $stmt->execute();
      $stmt->store_result();

      //make course_id list as well
      $stmt->fetch();
      $course_list = ";$course_name";
      $couseid_list = ";$course_id";
      while($stmt->fetch()){
        $course_list .= "^$course_name";
        $couse_list .= "^$course_id";
      }

      if($stmt->num_rows > 0){
        echo "STc0;".$course_id.$course_list;
      } else {
        echo "FTc0";
      }
    }
}

?>
