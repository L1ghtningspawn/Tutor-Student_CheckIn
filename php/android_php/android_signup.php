<?php
  header("content-type: text/plain");
  include 'dbconfig.php';
  include 'password.php';

  if(isset($_POST['signup'])){
    $email = $_POST['email'];
    $pwd = $_POST['pwd'];
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $year = $_POST['orgyear'];

    $query =
    'select email
      from LOGIN
      where email=?
    ';
    $stmt = $con->prepare($query);
    $stmt->bind_param("s",$email);
    $stmt->bind_result($email_result);
    $stmt->execute();
    $stmt->fetch();
    $stmt->close();

    if($email_result==$email){
      echo "FS0";
    } else {
      $pwd_hash = password_hash($pwd,PASSWORD_BCRYPT);
      $query =
      ' insert into LOGIN (email, password)
        values (?,?);
      ';
      $stmt = $con->prepare($query);
      $stmt->bind_param('ss',$email,$pwd_hash);
      $stmt->execute();
      $affected_rows = $stmt->affected_rows;
      $stmt->close();

      $query =
      ' insert into registration (login_id, fname, lname, year)
        values ((select login_id from LOGIN where email=?),?,?,?);
      ';
      $stmt = $con->prepare($query);
      $stmt->bind_param("sssi",$email,$fname,$lname,$year);
      $stmt->execute();
      $affected_rows = $affected_rows + $stmt->affected_rows;
      $stmt->close();

      if($affected_rows > 0){
        $session_id = $pwd_hash.(date("Y-m-d H:s:v"));
        $session_id = password_hash($session_id,PASSWORD_BCRYPT);

        $query =
        ' insert into MOBILE_SESSION_DATA (login_id, session_id, session_end)
            values ((select login_id from LOGIN where email=?), ?, date_add(now(), interval 1 day));
        ';
        $stmt = $con->prepare($query);
        $stmt->bind_param("ss",$email,$session_id);
        $stmt->execute();
        $stmt->close();

        echo "SS0-$session_id";
      } else {
        echo 'FS1';
      }
    }
  }
?>
