<?php



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

  <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no" />
    <script src="https://api.mapbox.com/mapbox-gl-js/v1.10.0/mapbox-gl.js"></script>
    <link href="https://api.mapbox.com/mapbox-gl-js/v1.10.0/mapbox-gl.css" rel="stylesheet" />
    <style>
    body { margin: 0; padding: 0; }

    #map { position: relative; top: 0; bottom: 0; height: 100%; width: 100%; }
    </style>


</head>

<body>


<div class="main-menu">

  <header class="header">
    <a href="test.php" class="logo">STAFF CTRL</a>
    <button type="button" class="button-close fa fa-times js__menu_close"></button>
    <div class="user">
      <a href="#" class="avatar"><img src="http://placehold.it/80x80" alt=""><span class="status online"></span></a>
      <h5 class="name"><a href="#">Сергей Кочетков</a></h5>
      <h5 class="position">Администратор</h5>
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
    <h1 class="page-title">Система мониторнига сотрудников</h1>
  </div>
  <!-- /.pull-left -->
  <div class="pull-right">
    <div class="ico-item">
      <!--a href="#" class="ico-item fa fa-search js__toggle_open" data-target="#searchform-header"></a-->
      <!--form action="#" id="searchform-header" class="searchform js__toggle"><input type="search" placeholder="Search..." class="input-search"><button class="fa fa-search button-search" type="submit"></button></form-->
    </div>

    <!--div class="ico-item fa fa-arrows-alt js__full_screen"></div-->
    <!--a href="#" class="ico-item pulse"><span class="ico-item fa fa-bell notice-alarm js__toggle_open" data-target="#notification-popup"></span></a-->
    <!--a href="#" class="ico-item fa fa-power-off js__logout"></a-->
  </div>
</div>

<div id="wrapper">
  <div class="main-content">


    <div class="row small-spacing">

      <div class="col-md-9 col-xs-12">
        <div class="row">

        <div class="col-xs-12">
          <div class="box-content card white">
            <h4 class="box-title">Добавить сотрудника</h4>
            <div class="card-content">
              <form class="form-horizontal" action="http://localhost:4567/api/v1/add/new_user" method="POST">

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Имя</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" id="inp-type-1" name="firstName" placeholder="Введите имя сотрудника">
                  </div>
                </div>

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Фамилия</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" id="inp-type-1" name="lastName" placeholder="Введите фамилию сотрудника">
                  </div>
                </div>

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Табельный номер</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" id="inp-type-1" name="tabelId" placeholder="Введите табельный номер сотрудника">
                  </div>
                </div>

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Должность</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" id="inp-type-1" name="workerPosition" placeholder="Введите должность сотрудника">
                  </div>
                </div>

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Пароль</label>
                  <div class="col-sm-9">
                    <input type="password" class="form-control" id="inp-type-1" name="password" placeholder="Придумайте и введите пароль">
                  </div>
                </div>


                <input type="hidden" name="tabelId" value="'.$tabelId.'">
                <input type="hidden" name="taskPriority" value="1">
                <input type="hidden" name="taskLocation" value="Location">

    						<div class="card-content">
    							<div class="row">
    								<input type="submit" class="btn btn-primary waves-effect waves-light" value="Добавить">
    							</div>
    						</div>


              </form>
            </div>
          </div>

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

?>
