<?php

//set it to writable location, a place for temp generated PNG files
$PNG_TEMP_DIR = dirname(__FILE__).DIRECTORY_SEPARATOR.'temp'.DIRECTORY_SEPARATOR;

//html PNG location prefix
$PNG_WEB_DIR = 'temp/';

include "qr_gen/QR/qrlib.php";

if (!file_exists($PNG_TEMP_DIR))
    mkdir($PNG_TEMP_DIR);

$filename = $PNG_TEMP_DIR.'test.png'; //<-- we need to set this properly

$errorCorrectionLevel = 'L';
$matrixPointSize = 4;
$data = "manbearpig rises";
QRcode::png($data, $filename, $errorCorrectionLevel, $matrixPointSize, 2);

echo '<img src="'.$PNG_WEB_DIR.basename($filename).'" /><hr/>';

?>
