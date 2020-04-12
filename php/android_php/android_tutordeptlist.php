<?php
include 'dbconfig.php';

if(isset($_POST['tutordeptlist'])){
  $email = $_POST['email'];
  $session_id = $_POST['session_id'];

  $query =
  'select B.SESSION_ID
   from LOGIN A, MOBILE_SESSION_DATA B
   where A.login_id=B.login_id and
     A.email=? and B.session_id=? and B.session_end>now();
  ';
  $stmt = $con->prepare($query);
  $stmt->bind_param('ss',$email,$session_id);
  $stmt->bind_result($session_id_from_server);
  $stmt->execute();
  $stmt->store_result();
  $session_id_exists = false;
  while($stmt->fetch()){
    $session_id_exists = true;
  }
  $stmt->close();

  if($session_id_exists){

    $query =
    'select B.ur_id, D.dept_name
      from LOGIN A, USER_ROLES B, USER_ROLES_DEPARTMENT C, DEPARTMENT D
      where A.email=? and
        A.login_id=B.u_id and
        B.r_id=(select r_id from ROLES where role_name="TUTOR") AND
        B.ur_id=C.ur_id and
        C.d_id=D.d_id;
    ';
    $stmt = $con->prepare($query);
    $stmt->bind_param('s',$email);
    $stmt->bind_result($user_role_id,$dept_name);
    $stmt->execute();
    $stmt->store_result();

    $stmt->fetch();
    $user_roles = $user_role_id;
    $depts = $dept_name;
    while($stmt->fetch()){
      $user_roles .= "^".$user_role_id;
      $depts .= "^".$dept_name;
    }
    $rows = $stmt->num_rows;
    $stmt->close();

    if($rows > 0){
      $result = "STu0;".$user_roles.";".$depts;
      echo $result;
    } else {
      $result = "FTu0";
      echo $result;
    }
  } else {
    echo "STu1";
  }
}


?>
