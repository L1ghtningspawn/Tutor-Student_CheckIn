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

	//is password valid?
	if(strlen($pwd) >= 25){
		echo "FS1";
		exit();
	}

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
      ' insert into USER_INFO (login_id, fname, lname, year_at_organization)
        values ((select login_id from LOGIN where email=?),?,?,?);
      ';
      $stmt = $con->prepare($query);
      $stmt->bind_param("sssi",$email,$fname,$lname,$year);
      $stmt->execute();
      $affected_rows = $affected_rows + $stmt->affected_rows;
      $stmt->close();

      $query_user_roles =
      ' insert into USER_ROLES (u_id, r_id)
        values ((select login_id from LOGIN where email=?), (select r_id from ROLES where role_name="STUDENT"));
      ';
      $stmt_user_roles = $con->prepare($query_user_roles);
      $stmt_user_roles->bind_param('s',$email);
      $stmt_user_roles->execute();
      $affected_rows += $stmt_user_roles->affected_rows;
      $stmt_user_roles->close();

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

        $query_available_roles =
        'select distinct A.role_name
         from ROLES A, USER_ROLES B, LOGIN C
         where A.r_id=B.r_id
         and B.u_id=C.login_id
         and C.email=?;
        ';
        $stmt_available_roles = $con->prepare($query_available_roles);
        $stmt_available_roles->bind_param('s',$email);
        $stmt_available_roles->bind_result($available_role);
        $stmt_available_roles->execute();
        $stmt_available_roles->store_result();

        $roles_string = '';
        while($stmt_available_roles->fetch()){
          $available_role;
          if($available_role=='TUTOR'){ $roles_string .= 'Tu'; }
          if($available_role=='STUDENT'){ $roles_string .= 'St'; }
          if($available_role=='SUPERVISOR'){ $roles_string .= 'Su'; }
        }

        $query =
        'select a.ur_id
         from USER_ROLES a, LOGIN b, ROLES c
         where a.u_id = b.login_id
     and a.r_id = c.r_id and b.email = ?;';
        $stmt = $con->prepare($query);
        $stmt->bind_param("s",$email);
        $stmt->bind_result($login_id);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();

        $stmt_available_roles->close();

        echo "SS0;$login_id;$roles_string;$session_id";
      } else {
        echo 'FS1';
      }
    }
  }
?>
