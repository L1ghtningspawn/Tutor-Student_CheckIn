<?
session_start();
include ('../include.php');
#include ('change_department.php');
#include ('current_department.php');

$ur_id=$_SESSION['Tutor_id'];
$index=$_SESSION['Tutor_index'];
$t_array=$_SESSION['Tutor_array'];

echo $ur_id;
echo "<br>";
echo $index;
echo "<br>";
var_dump($t_array);



?>

