<?php
  header("content-type: text/plain");
  include "dbconfig.php";
  include "password.php";

  if(isset($_POST['login'])){
    $email = trim($_POST['email']);
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
      echo "FL0";
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
        $stmt_available_roles->close();

        $query =
        'select a.ur_id

         from USER_ROLES a, LOGIN b, ROLES c

         where a.u_id = b.login_id

		 and a.r_id = c.r_id and b.email = ?';
        $stmt = $con->prepare($query);
        $stmt->bind_param("s",$email);
        $stmt->bind_result($login_id);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();

        echo "SL0;$login_id;$roles_string;$session_id";
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
        $stmt_available_roles->close();

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

        echo "SL0;$login_id;$roles_string;$session_id";
        $stmt->close();
      }
    } else {
      echo "FL1";
    }
  }
?>
