<?php
    session_start();
    include ('../include.php');

    $session=$_POST['sid'];
    $courseid= $_POST['cid'];


    $query = "update TUTOR_SESSION set time_in=NOW(), c_id=$courseid where ts_id= $session;";
    $stmt = $con->prepare($query);
    $stmt->execute();

?>
