<?php
  session_start();

  $server_id = "";
  for($x=0; $x<16; $x++){
    $next = rand(32,126);
    $server_id .= chr($next);
  }

  echo $_SESSION['Tutor_id']."#".$server_id;

?>
