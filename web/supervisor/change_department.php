<?php
session_start();
include ('../include.php');
$d_id=$_POST['d_id'];
$ur_id=$_SESSION['Supervisor_id'];
$supervisor_array=$_SESSION['Supervisor_array'];
$supervisor_index=$_SESSION['Supervisor_index'];

$departments="Select d_id from $server_database.DEPARTMENT where d_id in(Select d_id from $server_database.USER_ROLES_DEPARTMENT where ur_id=$ur_id)";
$result=mysqli_query($con,$departments);
$to_web=[];
while($row=mysqli_fetch_assoc($result))
{
    $current=$row['d_id'];
}
if($current==$d_id)
{
    echo "Cannot change to current active department";
}
else
{
    $new_role="Select ur_id from $server_database.USER_ROLES_DEPARTMENT where d_id=$d_id";
    #echo $new_role;
    $result=mysqli_query($con,$new_role);
    $i=0;
    while($row=mysqli_fetch_assoc($result))
    {
        $urid=$row['ur_id'];
        /*if($row['ur_id']==$supervisor_array[$i])
        {
            $_SESSION['Supervisor_index']=$i;
            $_SESSION['Supervisor_id']=$row['ur_id'];
            $i++;
        }
        else
        {
            $i++;
        }*/
        for($j=0;$j<sizeof($supervisor_array);$j++)
        {
            if($urid==$supervisor_array[$j])
            {
                $_SESSION['Supervisor_index']=$j;
                $_SESSION['Supervisor_id']=$row['ur_id'];
            }

        }
    }
}