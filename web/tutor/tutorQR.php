<?php
  session_start();

  $server_id = "";
  for($x=0; $x<16; $x++){
    $next = rand(32,126);
    $server_id .= chr($next);
  }
  $ur_id_array=$_SESSION['Tutor_id'];
  $ur_id_index=$_SESSION['index'];
  $ur_id=$ur_id_array[$ur_id_index];
  echo $ur_id."#".$server_id;

?>
