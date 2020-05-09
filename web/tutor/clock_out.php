<?
session_start();
include ("include.php");
include ("clock_state.php");

$check_punch=checkIfIn();
if($check_punch)
{
    echo "You are already punched out";
}
else
{
    $time=time();
    $t_ur_id=$_SESSION['Tutor_id'];
    $query="Select transaction_id from $server_database.PAYROLL where t_ur_id=$t_ur_id order by transaction_id desc limit 1";
    $result=mysqli_query($con,$query);
    while($row=mysqli_fetch_array($result))
    {
        $tid=$row['transaction_id'];
        $query="Update $server_database.PAYROLL set punchout=FROM_UNIXTIME($time) where transaction_id=$tid";
        mysqli_query($con,$query);
    }
}
?>