<?
session_start();

if($_SESSION['Student'])
    echo $_SESSION['Student'];
else
    echo False;
?>