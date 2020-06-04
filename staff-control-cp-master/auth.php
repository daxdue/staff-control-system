<?php
/*
$mysql_host = '127.0.0.1';
$mysql_user = 'root';
$mysql_password = '';
$mysql_database = 'test';
*/
require_once 'db_connection.php';

if(isset($_POST['username'])) {
  $username = $_POST['username'];
}
if(isset($_POST['password'])) {
  $password = $_POST['password'];
}

$conn = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_database);
if(!$conn) {
  die("Connection error: " . mysqli_connect_error());
}

$query = "SELECT * FROM `users`";
$execute = mysqli_query($conn, $query);
while ($row = mysqli_fetch_array($execute)) {
  if((strcmp($row["user_login"], $username) == 0) && (strcmp($row["user_password"], $password) == 0)) {
    echo "Autorized!";
  }
  else {
    echo "Error";
  }
}
?>
