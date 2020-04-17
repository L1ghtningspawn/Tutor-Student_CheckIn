<?
session_start();

if($_SESSION['Student'])
    echo $_SESSION['fname'].' '.$_SESSION['lname'];
else
    echo 'False';
?>