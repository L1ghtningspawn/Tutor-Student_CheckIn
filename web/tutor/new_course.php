<?php

include ('../include.php');
$c_id=$_POST['c_id'];
$ur_id=$_SESSION['Tutor_id'];

$query4="Insert into $server_database.USER_ROLES_COURSE (ur_id,c_id) values ($ur_id,$c_id)";
mysqli_query($con,$query4);

?>