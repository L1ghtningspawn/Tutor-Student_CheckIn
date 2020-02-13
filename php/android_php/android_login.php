<?php
  header("content-type: text/plain");
  include "dbconfig.php";
  include "password.php";

  if(isset($_POST['login'])){
    $email = $_POST['email'];
    $password = $_POST['password'];

    $query = "select email, password from LOGIN where email=?";
    $stmt = $con->prepare($query);
    $stmt->bind_param("s",$email);
    $stmt->bind_result($email, $pwd);
    $stmt->execute();
    $stmt->store_result();
    $stmt->fetch();
    $num_rows = $stmt->num_rows;
    $stmt->close();
    if($num_rows == 0){
      echo "FL0 ";
    } elseif(password_verify($password,$pwd)) {
      //is there an existing session?
      $query =
      'select session_id
        from MOBILE_SESSION_DATA
        where login_id=(select login_id from LOGIN where email=?);';
      $stmt = $con->prepare($query);
      $stmt->bind_param("s",$email);
      $stmt->bind_result($session_id);
      $stmt->execute();
      $stmt->fetch();
      $stmt->store_result();
      $stmt->close();

      if(isset($session_id)){
        echo "SL0-$session_id";
      } else {
        $session_id = $pwd.(date("Y-m-d H:s:v"));
        $session_id = password_hash($session_id,PASSWORD_BCRYPT);

        $query =
        'insert into MOBILE_SESSION_DATA (login_id, session_id, session_end)
            values ((select login_id from LOGIN where email=?), ?, date_add(now(), interval 1 day));
        ';
        $stmt = $con->prepare($query);
        $stmt->bind_param("ss",$email,$session_id);
        $stmt->execute();
        $stmt->close();
        echo "SL0-$session_id";
      }
    } else {
      echo "FL1 ";
    }
  }
?>
