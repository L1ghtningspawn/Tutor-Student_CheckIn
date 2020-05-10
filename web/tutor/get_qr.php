<?php

session_start();

include "../qr_gen/QR/qrlib.php";
    include "../qr_gen/server_key.php";
    include "../include.php";
    $PNG_WEB_DIR='../qr_gen/tutor_codes/';
    $filename=$PNG_WEB_DIR.$_SESSION['Tutor_id'].'.png';

    echo json_encode($filename);


    ?>