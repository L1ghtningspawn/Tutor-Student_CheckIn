<?php
include 'dbconfig.php';

if($_POST['clock_in']){
  $email = $_POST['email'];
  $session_id = $_POST['session_id'];
  $user_role_id = $_POST['user_role_id'];

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

    $query_clockin =
    'insert into PAYROLL (t_ur_id, punch_in)
          values (?, now());
    ';
    $stmt_clockin = $con->prepare($query_clockin);
    $stmt_clockin->bind_param("i",$user_role_id);
    $stmt_clockin->execute();
    $clockin_id = $con->insert_id;

    if($stmt_clockin->affected_rows > 0){
      echo "SC0;".$clockin_id.";".date("U");//date("Y-m-d H:i:s");
    } else {
      echo "FC0";
    }

    $stmt_clockin->close();
  } else {
    echo "SC1";
  }
}

?>
