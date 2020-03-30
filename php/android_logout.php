<?php
include 'dbconfig.php';

if(isset($_POST['logout'])){
  $email = $_POST['email'];
  $session_id = $_POST['session_id'];

  $query =
  ' delete from MOBILE_SESSION_DATA
    where login_id=(select login_id from LOGIN where email=?);
  ';
  $stmt = $con->prepare($query);
  $stmt->bind_param('s',$email);
  $stmt->execute();
  $affected_rows = $con->affected_rows;
  $stmt->close();

  if($affected_rows > 0){
    return 'SLo0';
  } else{
    return 'FLo0';
  }

}


?>
