<?php

session_start();

function generateQR()
{
    include "../qr_gen/QR/qrlib.php";
    include "../qr_gen/server_key.php";
    include "../include.php";
    $query="Select qr_key from $server_database.SERVER_QR_KEYS order by sqk_id desc limit 1";
    $result=mysqli_query($con,$query);
    while($row=mysqli_fetch_assoc($result))
    {
        $qr_key=$row['qr_key'];
    }
    $qr_code=$_SESSION['Tutor_id']."#".$qr_key;
    $PNG_WEB_DIR='../qr_gen/tutor_codes/';
    #$ivlen = openssl_cipher_iv_length($cipher="AES-128-CBC");
        #$iv = openssl_random_pseudo_bytes($ivlen);
        #$ciphertext_raw = openssl_encrypt($qr_code, $cipher, $key, $options=OPENSSL_RAW_DATA, $iv);
        #$hmac = hash_hmac('sha256', $ciphertext_raw, $key, $as_binary=true);
        #$ciphertext = base64_encode( $iv.$hmac.$ciphertext_raw );
        $filename = $PNG_WEB_DIR.$_SESSION['Tutor_id'].'.png';
        #QRcode::png($ciphertext, $filename, 'H', 8, 2);
        QRcode::png($qr_code, $filename, 'H', 8, 2);
}


$array=[];
array_push($array,$_SESSION['u_id']);
array_push($array,$_SESSION['fname']);
array_push($array,$_SESSION['lname']);
array_push($array,$_SESSION['login_id']);
array_push($array,$_SESSION['email']);
array_push($array,$_SESSION['year_at_organization']);
if($_SESSION['Student'])
{
    array_push($array,"Student");
    array_push($array,$_SESSION['Student_id']);
}
else
{
    array_push($array,"Not-Student");
    array_push($array,"0");
}
if($_SESSION['Tutor'])
{
    array_push($array,"Tutor");
    array_push($array,$_SESSION['Tutor_id']);
    generateQR();
}
else
{
    array_push($array,"Not-Tutor");
    array_push($array,"0");
}
if($_SESSION['Supervisor'])
{
    array_push($array,"Supervisor");
    array_push($array,$_SESSION['Supervisor_id']);
}
else
{
    array_push($array,"Not-Supervisor");
    array_push($array,"0");
}

#supervisor array info

echo json_encode($array);
?>