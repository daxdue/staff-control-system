<?php
$username = "10001";
$password = "qzwxecasd123";
$host_api = "http://localhost:4567";
$param = 123;

// авторизация
$curl = curl_init($host_api);
curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
// get запрос
curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/users");
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($curl);
curl_close($curl);
$requestResult = json_decode($result, true);

$status = $requestResult['status'];
$dataType = $requestResult['dataType'];
$userList = $requestResult['data'];

$userTable = "";

foreach ($userList as $key) {
  $userName = $key['name'];
  $userPosition = $key['position'];
  $tabelId = $key['tabelId'];
  $workerId = $key['workerId'];

  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  // get запрос
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/workshifts/$tabelId");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);
  $reqRes = json_decode($result, true);

  $workshift = $reqRes['data'];
  $openDate = $workshift['start_time'];
  $closeDate = $workshift['end_time'];

  $totalTasksAmount = 0;
  $completedTasksAmount = 0;

  /* Get all tasks */
  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/alltasks/$tabelId");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);

  $reqRes = json_decode($result, true);
  $taskList = $reqRes['data'];
  /* End of getting all tasks */

  //Открыта ли смена
  if(empty($openDate)) {
    //Смена не открыта. Получить последнюю смену
    $curl = curl_init($host_api);
    curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
    curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
    // get запрос
    curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/lastworkshifts/$tabelId");
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($curl);
    curl_close($curl);
    $reqRes = json_decode($result, true);

    $workshift = $reqRes['data'];
    $openDate = $workshift['start_time'];
    $closeDate = $workshift['end_time'];

    if(empty($openDate)) {
      $openDate = "Не открыта";
    } else {
      //Задачи прошлой смены
      foreach ($taskList as $key) {
        if((strtotime($key['updated']) >= strtotime($openDate)) && (strtotime($key['updated']) <= strtotime($closeDate))) {
          $totalTasksAmount++;
          if(strcmp($key['status'], "completed") == 0) {
            $completedTasksAmount++;
          }
        }
      }
    }
    //$closeDate = "";
  } else {
    //Выбираем задачи текущей смены
    $tasksCounter = 0;
    foreach ($taskList as $key) {
      if(strcmp($key['status'], "active") == 0) {
        $totalTasksAmount++;
      } else if((strtotime($key['updated']) >= strtotime($openDate)) && (strcmp($key['status'], "completed") == 0)){
        $totalTasksAmount++;
        $completedTasksAmount++;
      }
    }

    if(empty($closeDate)) {
      $closeDate = "Не закрыта";
    }
  }

  $timestampToShowStart = "";
  $timestampToShowClose = "";


  $timestampToShowStart = transformTimestamp($openDate);
  $timestampToShowClose = transformTimestamp($closeDate);


  $userTable = $userTable .
   '<tr id = '.$tabelId.'>
    <td>'.$tabelId.'</td>
    <td><a href="worker.php?id='.$tabelId.'">'.$userName.'</td>
    <td>'.$userPosition.'</td>
    <td>'.$timestampToShowStart.'</td>
    <td>'.$timestampToShowClose.'</td>
    <td>'.$totalTasksAmount.'</td>
    <td>'.$completedTasksAmount.'</td>';

/*
  echo "Имя сотрудника: " . $userName . "<br></br>";
  echo "Должность сотрудника: " . $userPosition . "<br></br>";
  echo "Табельный номер: " . $tabelId . "<br></br>";
  echo "ID работника: " . $workerId . "<br></br>";
  echo "Смена: " . $openDate ."<br></br>";
  echo "===========================================<br></br>";
  */

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
  		<a href="test.html" class="logo">STAFF CTRL</a>
  		<button type="button" class="button-close fa fa-times js__menu_close"></button>
  		<div class="user">
  			<a href="#" class="avatar"><img src="http://placehold.it/80x80" alt=""><span class="status online"></span></a>
  			<h5 class="name"><a href="#">Сергей Кочетков</a></h5>
  			<h5 class="position">Администратор</h5>
  			<!-- /.name -->
  			<!--div class="control-wrap js__drop_down">
  				<i class="fa fa-caret-down js__drop_down_button"></i>
  				<div class="control-list">
  					<div class="control-item"><a href="profile.html"><i class="fa fa-user"></i> Профиль</a></div>
  					<div class="control-item"><a href="#"><i class="fa fa-gear"></i> Настройки</a></div>
  					<div class="control-item"><a href="#"><i class="fa fa-sign-out"></i> Выйти</a></div>
  				</div>
  				<!-- /.control-list -->
  			</div-->
  			<!-- /.control-wrap -->
  		</div>
  		<!-- /.user -->
  	</header>
  	<!-- /.header -->
  	<div class="content">

      <!-- There is menu data place -->
      <div class="content">
        <div class="navigation">
          <h5 class="title">Меню</h5>
          <!-- /.title -->
          <ul class="menu js__accordion">
            <li>
              <a class="waves-effect" href="add_worker.php"><i class="menu-icon fa fa-user ico"></i><span>Управление аккаунтами</span></a>
            </li>
          </ul>
        </div>
      </div>
      <!-- End of menu data -->

  		<!-- /.navigation -->
  	</div>
  	<!-- /.content -->
  </div>
  <!-- /.main-menu -->

  <div class="fixed-navbar">
  	<div class="pull-left">
  		<button type="button" class="menu-mobile-button glyphicon glyphicon-menu-hamburger js__menu_mobile"></button>
  		<h1 class="page-title">Система мониторнига сотрудников</h1>

  	</div>
  	<!-- /.pull-left -->
  	<div class="pull-right">
  		<div class="ico-item">
  			<!--a href="#" class="ico-item fa fa-search js__toggle_open" data-target="#searchform-header"></a>
  			<form action="#" id="searchform-header" class="searchform js__toggle"><input type="search" placeholder="Search..." class="input-search"><button class="fa fa-search button-search" type="submit"></button></form>
  			<!-- /.searchform -->
  		</div>
  		<!-- /.ico-item -->
  		<!--div class="ico-item fa fa-arrows-alt js__full_screen"></div-->
  		<!-- /.ico-item -->
  		<!--a href="#" class="ico-item pulse"><span class="ico-item fa fa-bell notice-alarm js__toggle_open" data-target="#notification-popup"></span></a-->
  		<!--a href="#" class="ico-item fa fa-power-off js__logout"></a-->
  	</div>
  	<!-- /.pull-right -->
  </div>
  <!-- /.fixed-navbar -->






  <div id="wrapper">

  	<div class="main-content">

  		<div class="box-content">
  			<h4 class="box-title">Сотрудники</h4>
  			<table id="example" class="table table-striped table-bordered display" style="width:100%">
        <!--table id="example-edit" class="display" style="width: 100%"-->
  				<thead>
  					<tr>
              <th>Табельный номер</th>
  						<th>Имя, фамилия сотрудника</th>
  						<th>Должность</th>
  						<th>Время открытия смены</th>
              <th>Время закрытия смены</th>
  						<th>Количество задач</th>
  						<th>Выполнено</th>
  					</tr>
  				</thead>
  				<tfoot>
  					<tr>
              <th>Табельный номер</th>
              <th>Имя, фамилия сотрудника</th>
  						<th>Должность</th>
  						<th>Время открытия смены</th>
              <th>Время закрытия смены</th>
  						<th>Количество задач</th>
  						<th>Выполнено</th>

  					</tr>
  				</tfoot>
  				<tbody id="content">
  					'.$userTable.'
  				</tbody>
  			</table>
  		</div>
  		<!-- /.box-content -->
  	</div>
  	</div>
  	<!-- /.main-content -->
  </div><!--/#wrapper -->
  	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  	<!--[if lt IE 9]>
  		<script src="assets/script/html5shiv.min.js"></script>
  		<script src="assets/script/respond.min.js"></script>
  	<![endif]-->
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

//var_dump(json_decode($result, true));
?>
