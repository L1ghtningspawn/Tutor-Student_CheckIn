<?
session_start();
include ('../include.php');
#include ('change_department.php');
#include ('current_department.php');

$ur_id=$_SESSION['Supervisor_id'];
$index=$_SESSION['Supervisor_index'];
$t_array=$_SESSION['Supervisor_array'];

echo $ur_id;
echo "<br>";
echo $index;
echo "<br>";
var_dump($t_array);



?>

