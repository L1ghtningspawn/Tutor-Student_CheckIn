<?php
session_start();
include ('../include.php');
#$var=$_POST['var_from_ajax'];
$var=$_SESSION["Tutor_id"];
$query="Select ts_id,s_ur_id,time_in,time_out,c_id from $server_database.TUTOR_SESSION where t_ur_id=$var order by ts_id desc";
#echo $query;
$result=mysqli_query($con,$query);
$array=[];
while($row=mysqli_fetch_array($result))
{
    $temp=[];
    array_push($temp,$row['ts_id']);
    array_push($temp,$row['s_ur_id']);
    array_push($temp,$row['time_in']);
    array_push($temp,$row['time_out']);
    array_push($temp,$row['c_id']);
    array_push($array,$temp);
}

for($i=0;$i<count($array);$i++)
{
    for($j=0;$j<count($array[$i]);$j++)
    {
        if($j==1)
        {
            $var=$array[$i][$j];
            $query="Select ui.fname as fname, ui.lname as lname from USER_INFO ui, USER_ROLES ur where ur.u_id=ui.u_id and ur.ur_id=$var";
            $result=mysqli_query($con,$query);
            while($row=mysqli_fetch_array($result))
            {
                $array[$i][$j]=$row['fname']." ".$row['lname'];
            }
        }
        if($j==4)
        {
            $var=$array[$i][$j];
            $query="Select course_name from COURSE where c_id=$var";
            $result=mysqli_query($con,$query);
            while($row=mysqli_fetch_array($result))
            {
                $array[$i][$j]=$row['course_name'];
            }
        }
    }
}

echo json_encode($array);

?>