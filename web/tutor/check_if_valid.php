<?
session_start();

if($_SESSION['Tutor'])
    echo $_SESSION['fname'].' '.$_SESSION['lname'];
else
    echo 'False';
?>