<?php
session_start();
include ('get_non_tutors.php');

#print_r($to_web);

?>



<?php

echo "<table>";
echo "<tr>";
echo "<th>Non-Tutors</th><th>Tutor</th>";
echo "</tr>";
echo "<tr>";
echo "<td>";
echo "<table>";
echo "<tr>";
echo "<th>Student</th><th>Elevate</th></tr>";
for($i=0;$i<sizeof($to_web);$i++)
{
    echo "<tr>";
        echo "<td>".$to_web[$i][0]." ".$to_web[$i][1]." ".$to_web[$i][2]." ".$to_web[$i][3]."</td>";
        echo "<td>"."<form name='input' action='new_tutor.php' method='post'>"."<input type='hidden' value='".$to_web[$i][3]."' name='u_id'>".'<input type="submit" value="elevate '.$to_web[$i][0].'"></form>'."</td>";
    echo "<tr>";
}
echo "</table>";
echo "</td>";
echo "<td>";
include ('get_tutors.php');
echo "<table>";
echo "<tr>";
echo "<th>Student</th><th>De-Elevate</th></tr>";
for($i=0;$i<sizeof($to_web);$i++)
{
    echo "<tr>";
        echo "<td>".$to_web[$i][0]." ".$to_web[$i][1]." ".$to_web[$i][2]." ".$to_web[$i][3]."</td>";
        echo "<td>"."<form name='input' action='remove_tutor.php' method='post'>"."<input type='hidden' value='".$to_web[$i][3]."' name='ur_id'>".'<input type="submit" value="remove '.$to_web[$i][0].'"></form>'."</td>";
    echo "<tr>";
}
echo "</table>";
echo "</td>";
echo "</tr>";
echo "</table>";

?>

<html>

<head>
    <style>
        table,
        th,
        td {
            border: 1px solid black;
        }
    </style>
</head>

<body>