<?php
session_start();
include ('../include.php');

function checkIfIn()
{
    include ('../include.php');
    $ur_id_array=$_SESSION['Tutor_id'];
$ur_id_index=$_SESSION['index'];
$ur_id=$ur_id_array[$ur_id_index];
    $t_ur_id=$ur_id;
    $q_checkIfIn="Select transaction_id from $server_database.PAYROLL where t_ur_id='$t_ur_id' and punch_out='00:00:00'";
    $result=mysqli_query($con,$q_checkIfIn);
    if(mysqli_num_rows($result)!=0)
    {
      $row = $result->fetch_assoc();
      $_SESSION['clockin_id'] = $row['transaction_id'];
        return TRUE;
    }
    else
    {
        return FALSE;
    }

}

function checkIfOut()
{
    include ('../include.php');
    $ur_id_array=$_SESSION['Tutor_id'];
$ur_id_index=$_SESSION['index'];
$ur_id=$ur_id_array[$ur_id_index];
    $t_ur_id=$ur_id;
    $clockin_id = $_SESSION['clockin_id'];
    $q_checkIfOut="Select transaction_id from $server_database.PAYROLL where t_ur_id='$t_ur_id' and punch_out!='00:00:00' and transaction_id='$clockin_id'";
    $result=mysqli_query($con,$q_checkIfOut);
    if(mysqli_num_rows($result)!=0)
    {
        return TRUE;
    }
    else
    {
        return FALSE;
    }

}
