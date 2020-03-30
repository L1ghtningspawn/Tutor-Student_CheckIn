<?php
  include '../dbconfig.php';
  include '../password.php';

  if(isset($_POST['create_supervisor'])){
    $email = $_POST['email'];
    $password = $_POST['password'];
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];
    $year_at_organization = $_POST['year_at_organization'];
    $department = $_POST['department'];

    $password_hashed = password_hash($password,PASSWORD_BCRYPT);

    $query_login =
    'insert into LOGIN (email,password)
     values (?,?);
    ';
    $stmt_login = $con->prepare($query_login);
    $stmt_login->bind_param('ss',$email,$password_hashed);
    $stmt_login->execute();
    if($con->affected_rows > 0){
      echo "user entered into LOGIN table successfully <br>";
    }else{
      echo "user did not get entered into LOGIN table <br>";
    }
    $stmt_login->close();
    $login_id = $con->insert_id;

    $query_user_info =
    'insert into USER_INFO (login_id,fname, lname, year_at_organization)
     values (?,?,?,?);
    ';
    $stmt_user_info = $con->prepare($query_user_info);
    $stmt_user_info->bind_param('issi',$login_id,$first_name,$last_name,$year_at_organization);
    $stmt_user_info->execute();
    if($con->affected_rows > 0){
      echo "user info entered into USER_INFO table successfully <br>";
    }else{
      echo "user info not entered into USER_INFO table <br>";
    }
    $stmt_user_info->close();

    $query_user_roles =
    'insert into USER_ROLES (u_id, r_id) values
          (?, (select r_id from ROLES where role_name="STUDENT")),
          (?, (select r_id from ROLES where role_name="TUTOR")),
          (?, (select r_id from ROLES where role_name="SUPERVISOR"));
    ';
    $stmt_user_roles = $con->prepare($query_user_roles);
    $stmt_user_roles->bind_param('iii',$login_id,$login_id,$login_id);
    $stmt_user_roles->execute();
    if($con->affected_rows > 0){
      echo "student, tutor, and supervisor role added for user into USER_ROLES table successfully <br>";
    }else{
      echo "student role not added for user into USER_ROLES table <br>";
    }
    $stmt_user_roles->close();
    $user_student_role_id = $con->insert_id;
    $user_tutor_role_id = $user_student_role_id + 1;
    $user_supervisor_role_id = $user_tutor_role_id + 1;

    $query_department =
    'INSERT INTO DEPARTMENT (dept_name)
     values (?);
    ';
    $stmt_department = $con->prepare($query_department);
    $stmt_department->bind_param('s',$department);
    $stmt_department->execute();
    if($con->affected_rows > 0){
      echo "department was successfully added to table <br>";
    }else{
      echo "department failed to be added to table <br>";
    }
    $stmt_department->close();
    $department_id = $con->insert_id;

    $query_user_roles_department =
    'INSERT INTO USER_ROLES_DEPARTMENT (ur_id,d_id) values
         (?,?), (?,?)
    ';
    $stmt_user_roles_department = $con->prepare($query_user_roles_department);
    $stmt_user_roles_department->bind_param('iiii',$user_tutor_role_id,$department_id,$user_supervisor_role_id,$department_id);
    $stmt_user_roles_department->execute();
    if($con->affected_rows > 0){
      echo "user_role connected to department successfully <br>";
    }else{
      echo "user_role not connected to department <br>";
    }
    $stmt_user_roles_department->close();
  }

?>

<form action='create_supervisor_department.php' method='post'>
<table>
  <tr><th>Email:</th><td><input type='text' name='email'></td></tr>
  <tr><th>Password:</th><td><input type='password' name='password'></td></tr>
  <tr><th>First Name:</th><td><input type='text' name='first_name'></td></tr>
  <tr><th>Last Name:</th><td><input type='text' name='last_name'></td></tr>
  <tr><th>Year At Organization:</th><td><input type='number' name='year_at_organization'></td></tr>
  <tr><th>Department:</th><td><input type='text' name='department'></td></tr>
  <tr><td><input type='submit' name='create_supervisor'></td></tr>
</table>
</form>
