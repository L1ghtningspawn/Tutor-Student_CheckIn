<?php

include ("dbconfig.php");
$permitted_chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';

function generate_string($input, $strength = 16) {
    $input_length = strlen($input);
    $random_string = '';
    for($i = 0; $i < $strength; $i++) {
        $random_character = $input[mt_rand(0, $input_length - 1)];
        $random_string .= $random_character;
    }

    return $random_string;
}
?>

<form action="generate_server_key.php" name="new_key" method="post">
Generate new code
<input type="submit" name="new_key" value="GENERATE">
</form>

<?php

if(isset($_POST["new_key"]))
{
    $key=generate_string($permitted_chars,16);
    $time=time();
    $query="Insert into $server_database.SERVER_QR_KEYS (qr_key,date_created) values ('$key',FROM_UNIXTIME($time))";
    mysqli_query($con,$query);
}

?>
