<?
session_start();

if($_SESSION['Supervisor'])
    echo $_SESSION['fname'].' '.$_SESSION['lname'];
else
    echo 'False';
?>