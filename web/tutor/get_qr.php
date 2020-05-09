<?php

session_start();

include "../qr_gen/QR/qrlib.php";
    include "../qr_gen/server_key.php";
    include "../include.php";
    $PNG_WEB_DIR='../qr_gen/tutor_codes/';
    $ur_id_array=$_SESSION['Tutor_id'];
$ur_id_index=$_SESSION['index'];
$ur_id=$ur_id_array[$ur_id_index];
    $filename=$PNG_WEB_DIR.$ur_id.'.png';

    echo json_encode($filename);


    ?>