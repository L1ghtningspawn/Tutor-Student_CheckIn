<?
session_start();
include ("../include.php");
include ("clock_state.php");

$check_punch=checkIfOut();
if($check_punch)
{
    echo "You are already punched out";
}
else
{
    $time=time();
    $ur_id_array=$_SESSION['Tutor_id'];
    $ur_id_index=$_SESSION['index'];
    $ur_id=$ur_id_array[$ur_id_index];
    $t_ur_id=$ur_id;
    $tid=$_SESSION['clockin_id'];
    $query="Update $server_database.PAYROLL set punch_out=FROM_UNIXTIME($time) where transaction_id=$tid";
    mysqli_query($con,$query);
    echo "you are now punched out!";
}
?>
