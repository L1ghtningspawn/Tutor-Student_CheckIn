<?php
include 'dbconfig.php';


if($_POST['clockout']){
  $email = $_POST['email'];
  $session_id = $_POST['session_id'];
  $clockin_id = $_POST['clockin_id'];

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
    $query_clockout =
    'update PAYROLL
     set punch_out=now()
     where transaction_id=?
    ';
    $stmt_clockout = $con->prepare($query_clockout);
    $stmt_clockout->bind_param("i",$clockin_id);
    $stmt_clockout->execute();
    if($stmt_clockout->affected_rows > 0){
      echo "SCl0;".date("U");
    } else {
      echo "FCl0";
    }
    $stmt_clockout->close();
  } else {
    echo "SCl1";
  }

}

?>
