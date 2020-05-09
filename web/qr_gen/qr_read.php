<?php

$PNG_TEMP_DIR = dirname(__FILE__).DIRECTORY_SEPARATOR.'tutor_codes'.DIRECTORY_SEPARATOR;
    
    //html PNG location prefix
    $PNG_WEB_DIR = 'tutor_codes/';

    include "./QR/qrlib.php";    
    
    //ofcourse we need rights to create temp dir
    if (!file_exists($PNG_TEMP_DIR))
        mkdir($PNG_TEMP_DIR);
    
    
    $filename = $PNG_TEMP_DIR.'generate_test.png';


echo '<img src="'.$PNG_WEB_DIR.basename($filename).'" /><hr/>';