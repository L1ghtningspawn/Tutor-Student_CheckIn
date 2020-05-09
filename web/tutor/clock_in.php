<?
session_start();

include ("../include.php");
include ("clock_state.php");
$time=time();

$check_punch=checkIfIn();
if($check_punch)
{
    echo "You are already punched in";
}
else
{
    $ur_id_array=$_SESSION['Tutor_id'];
    $ur_id_index=$_SESSION['index'];
    $ur_id=$ur_id_array[$ur_id_index];
    $t_ur_id=$ur_id;
    $query="Insert into $server_database.PAYROLL (t_ur_id,punch_in,punch_out) values ($t_ur_id,FROM_UNIXTIME($time),0)";
    mysqli_query($con,$query);
    echo "You are now punched in!";
    $_SESSION['clockin_id'] = $con->insert_id;
}
?>
