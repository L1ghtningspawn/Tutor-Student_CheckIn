<?php

$PNG_TEMP_DIR = dirname(__FILE__).DIRECTORY_SEPARATOR.'tutor_codes'.DIRECTORY_SEPARATOR;
    
    //html PNG location prefix
    $PNG_WEB_DIR = 'tutor_codes/';

    include "./QR/qrlib.php";
    include "server_key.php";
    
    //ofcourse we need rights to create temp dir
    if (!file_exists($PNG_TEMP_DIR))
        mkdir($PNG_TEMP_DIR);
    
        $plaintext = "Tutor#".$Current_key;
        $ivlen = openssl_cipher_iv_length($cipher="AES-128-CBC");
        $iv = openssl_random_pseudo_bytes($ivlen);
        $ciphertext_raw = openssl_encrypt($plaintext, $cipher, $key, $options=OPENSSL_RAW_DATA, $iv);
        $hmac = hash_hmac('sha256', $ciphertext_raw, $key, $as_binary=true);
        $ciphertext = base64_encode( $iv.$hmac.$ciphertext_raw );

    $filename = $PNG_TEMP_DIR.'generate_test.png';
    QRcode::png($ciphertext, $filename, 'H', 8, 2);