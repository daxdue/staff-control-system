<?php
$username = "10001";
$password = "qzwxecasd123";
$host_api = "http://localhost:4567";
$param = 123;

$id = "";
if(isset($_GET["id"])) {
  $id = $_GET["id"];
}

$curl = curl_init($host_api);
curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
// get запрос
curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/workshifts/all/$id");
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($curl);
curl_close($curl);
$reqRes = json_decode($result, true);
$workshiftList = $reqRes['data'];


$curl = curl_init($host_api);
curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
// get запрос
curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/users/$id");
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($curl);
curl_close($curl);
$requestResult = json_decode($result, true);

$status = $requestResult['status'];
$dataType = $requestResult['dataType'];
$userList = $requestResult['data'];


$userName = $userList['name'];
$userPosition = $userList['position'];
$tabelId = $userList['tabelId'];
$workerId = $userList['workerId'];


$userInfo = '<div class="col-xs-12">
  <div class="box-content card">
    <h4 class="box-title"><i class="fa fa-user ico"></i>Сотрудник</h4>
    <div class="card-content">
        <div class="row">
          <div class="col-md-7">
            <div class="row">
              <div class="col-xs-5"><label>Табельный номер:</label></div>
              <div class="col-xs-7">'.$tabelId.'</div>
            </div>
            </div>
            <!-- /.row -->
          </div>

          <div class="row">
            <div class="col-md-7">
              <div class="row">
                <div class="col-xs-5"><label>Имя, фамилия:</label></div>
                <div class="col-xs-7">'.$userName.'</div>
              </div>
            </div>
          </div>
      </div>
    </div>
  </div>';


$listOfTasks = "";


foreach ($workshiftList as $key) {

  $workshiftId = $key['workshift_id'];
  $workshiftOpenDate = $key['start_time'];
  $workshiftClosedDate = $key['end_time'];
  $reportId = $key['report_id'];

  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  // get запрос
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/tasks/workshift/$workshiftId");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);
  $reqRes = json_decode($result, true);
  $taskList = $reqRes['data'];


  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  // get запрос
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/report/$reportId");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);
  $reqRes = json_decode($result, true);

  $report = $reqRes['data'];
  $reportContent = $report['content'];


  $taskTable = "";
  $taskAmount = 0;
  foreach ($taskList as $task) {
    $taskAmount++;

    $taskStatus = $task['status'];
    if(strcmp($taskStatus, "completed") == 0) {
      $taskStatus = "Завершена";
    } else if(strcmp($taskStatus, "active") == 0) {
      $taskStatus = "Активна";
    }
    $taskTable = $taskTable .
    '<tr id = '.$task['priority'].'>
     <td>'.$task['name'].'</td>
     <td>'.$task['description'].'</td>
     <td>'.$taskStatus.'</td>
     <td>'.transformTimestamp($task['updated']).'</td>';
  }


  $listOfTasks = $listOfTasks . '


              <div class="col-xs-12">
                <div class="box-content">
                  <h4 class="box-title">Смена</h4>
                  <div class="card-content">

  <div class="row">
    <div class="col-md-12">

      <div class="row">
        <div class="col-xs-7">Открыта: '.transformTimestamp($workshiftOpenDate).'</div>
      </div>
      <div class="row">
        <div class="col-xs-7">Закрыта: '.transformTimestamp($workshiftClosedDate).'</div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
    <h4>Задачи</h4>
    <table id="example" class="table table-striped table-bordered display" style="width:100%">
    <!--table id="example-edit" class="display" style="width: 100%"-->
      <thead>
        <tr>
          <th>Название задачи</th>
          <th>Описание задачи</th>
          <th>Статус задачи</th>
          <th>Обновлено</th>

        </tr>
      </thead>
      <tfoot>
        <tr>
        <th>Название задачи</th>
        <th>Описание задачи</th>
        <th>Статус задачи</th>
        <th>Обновлено</th>
        </tr>
      </tfoot>
      <tbody id="content">
        '.$taskTable.'
      </tbody>
    </table>
    </div>
  </div>

  <div class="row">
    <div class="col-md-7">
      <div class="row">
        <div class="col-xs-5"><label>Отчет:</label></div>
        <div class="col-xs-7">'.$reportContent.'</div>
      </div>
    </div>
  </div>
  </div>
</div>
</div>';

  //echo "Workshift id: " . $workshiftId . "<br></br>";
  //echo "Tasks: " . $taskAmount . "<br></br>";
  //echo "==========================================="."<br></br>";
}


$page = '<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Система мониторнига сотрудников</title>

  <!-- Main Styles -->
  <link rel="stylesheet" href="assets/styles/style.min.css">

  <!-- mCustomScrollbar -->
  <link rel="stylesheet" href="assets/plugin/mCustomScrollbar/jquery.mCustomScrollbar.min.css">

  <!-- Waves Effect -->
  <link rel="stylesheet" href="assets/plugin/waves/waves.min.css">

  <!-- Sweet Alert -->
  <link rel="stylesheet" href="assets/plugin/sweet-alert/sweetalert.css">

  <!-- Color Picker -->
  <link rel="stylesheet" href="assets/color-switcher/color-switcher.min.css">


</head>

<body>


<div class="main-menu">

  <header class="header">
    <a href="test.php" class="logo">Indoor Nav</a>
    <button type="button" class="button-close fa fa-times js__menu_close"></button>
    <div class="user">
      <a href="#" class="avatar"><img src="http://placehold.it/80x80" alt=""><span class="status online"></span></a>
      <h5 class="name"><a href="#">Имя пользователя</a></h5>
      <h5 class="position">Должность</h5>
    </div>
  </header>

  <div class="content">
    <div class="navigation">
      <h5 class="title">Меню</h5>
      <!-- /.title -->
      <ul class="menu js__accordion">
        <li>
          <a class="waves-effect" href="index.php"><i class="menu-icon fa fa-home"></i><span>Главная страница</span></a>
        </li>
      </ul>
    </div>
  </div>
</div>


<div class="fixed-navbar">
  <div class="pull-left">
    <button type="button" class="menu-mobile-button glyphicon glyphicon-menu-hamburger js__menu_mobile"></button>
    <h1 class="page-title">Система мониторнига грузов</h1>
  </div>
  <!-- /.pull-left -->
  <div class="pull-right">
    <div class="ico-item">
      <!--a href="#" class="ico-item fa fa-search js__toggle_open" data-target="#searchform-header"></a-->
      <!--form action="#" id="searchform-header" class="searchform js__toggle"><input type="search" placeholder="Search..." class="input-search"><button class="fa fa-search button-search" type="submit"></button></form-->
    </div>

    <div class="ico-item fa fa-arrows-alt js__full_screen"></div>
    <!--a href="#" class="ico-item pulse"><span class="ico-item fa fa-bell notice-alarm js__toggle_open" data-target="#notification-popup"></span></a-->
    <!--a href="#" class="ico-item fa fa-power-off js__logout"></a-->
  </div>
</div>

<div id="wrapper">
  <div class="main-content">


    <div class="row small-spacing">

      <div class="col-md-9 col-xs-12">
        <div class="row">

          <!-- User info -->
          '.$userInfo.'
          <!-- /User info -->


          <!-- Tasks list -->
          '.$listOfTasks.'







      </div>
    </div>
  </div>
</div>
</div>

  <!--
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="assets/scripts/jquery.min.js"></script>
  <script src="assets/scripts/modernizr.min.js"></script>
  <script src="assets/plugin/bootstrap/js/bootstrap.min.js"></script>
  <script src="assets/plugin/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
  <script src="assets/plugin/nprogress/nprogress.js"></script>
  <script src="assets/plugin/sweet-alert/sweetalert.min.js"></script>
  <script src="assets/plugin/waves/waves.min.js"></script>
  <!-- Full Screen Plugin -->
  <script src="assets/plugin/fullscreen/jquery.fullscreen-min.js"></script>

  <script src="assets/scripts/main.min.js"></script>
  <script src="assets/color-switcher/color-switcher.min.js"></script>
</body>
</html>';
echo $page;

function transformTimestamp($timestamp) {
  $timestampToShow = "";
  $character = "-";
  if(strpos($timestamp, $character) !== false) {
    //Выполняем преобразование timestamp
    //2020-05-12 10:09:56.862
    //12.05.2020 10:09:56
    $yearDB = mb_strcut($timestamp, 0, 4);
    $monthDB = mb_strcut($timestamp, 5, 2);
    $dayDB = mb_strcut($timestamp, 8, 2);
    $hourDB = mb_strcut($timestamp, 11, 8);
    $timestampToShow = $hourDB . " " . $dayDB . "." . $monthDB . "." . $yearDB;
    //$timestampToShow = $dayDB . "." . $monthDB . "." . $yearDB . " " . $hourDB;
  } else {
    $timestampToShow = $timestamp;
  }

  return $timestampToShow;
}
?>
