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

$curl = curl_init($host_api);
curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/alltasks/$tabelId");
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($curl);
curl_close($curl);

$reqRes = json_decode($result, true);
$taskList = $reqRes['data'];

//echo $result;


$workshiftInfo = "";
$taskTable = "";


if(empty($workshift['start_time'])) {

  $workshiftInfo = "Не открыта";

  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  // get запрос
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/lastworkshifts/$tabelId");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);
  $reqRes = json_decode($result, true);

  $lastWorkshift = $reqRes['data'];

  if(empty($lastWorkshift['start_time'])) {
    $workshiftInfo = '<div class="row">
      <div class="col-md-6">
        <div class="row">
          <div class="col-xs-5"><label>Смена:</label></div>
          <div class="col-xs-7">Не открыта</div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="row">
          <div class="col-xs-5"><label>Крайняя смена:</label></div>
          <div class="col-xs-7">Нет предыдущих смен</div>
        </div>
      </div>
    </div>';

  } else {

    //Выбираем задачи текущей смены
    foreach ($taskList as $key) {
       if((strtotime($key['updated']) >= strtotime($lastWorkshift['start_time'])) && (strcmp($key['status'], "completed") == 0)){
         $taskName = $key['name'];
         $taskDescription = $key['description'];
         $taskStatus = $key['status'];
         $taskUpdated = transformTimestamp($key['updated']);
         $taskPriority = $key['priority'];

         $taskLocationData = $key['taskLocation'];
         $locationName = $taskLocationData['locationName'];

         //$taskLocation = json_decode($taskLocationData, true);

         //$locationName = $taskLocation['roomId'];

         if(strcmp($taskStatus, "active") == 0) {
           $taskStatus = "Активна";
         } else if(strcmp($taskStatus, "completed") == 0) {
           $taskStatus = "Завершена";
         }

         $taskTable = $taskTable .
         '<tr id = '.$taskPriority.'>
          <td>'.$taskName.'</td>
          <td>'.$taskDescription.'</td>
          <td>'.$locationName.'</td>
          <td>'.$taskStatus.'</td>
          <td>'.$taskUpdated.'</td>';
      } else if(strcmp($key['status'], "active") == 0) {
        $taskName = $key['name'];
        $taskDescription = $key['description'];
        $taskStatus = "Активна";
        $taskUpdated = transformTimestamp($key['updated']);
        $taskPriority = $key['priority'];

        $taskLocationData = $key['taskLocation'];
        $locationName = $taskLocationData['locationName'];

        $taskTable = $taskTable .
        '<tr id = '.$taskPriority.'>
         <td>'.$taskName.'</td>
         <td>'.$taskDescription.'</td>
         <td>'.$locationName.'</td>
         <td>'.$taskStatus.'</td>
         <td>'.$taskUpdated.'</td>';
      }
    }


    $curl = curl_init($host_api);
    curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
    curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
    // get запрос
    curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/reports/$tabelId");
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($curl);
    curl_close($curl);
    $reqRes = json_decode($result, true);

    $report = $reqRes['data'];
    $reportContent = $report['content'];

    $openDateLastWs = transformTimestamp($lastWorkshift['start_time']);
    $workshiftInfo = '<div class="row">
      <div class="col-md-7">
        <div class="row">
          <div class="col-xs-5"><label>Смена:</label></div>
          <div class="col-xs-7">Не открыта</div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-7">
        <div class="row">
          <div class="col-xs-5"><label>Крайняя смена:</label></div>
          <div class="col-xs-7"></div>
        </div>
        <div class="row">
          <div class="col-xs-5"><label></label></div>
          <div class="col-xs-7">Открыта: '.$openDateLastWs.'</div>
        </div>
        <div class="row">
          <div class="col-xs-5"><label></label></div>
          <div class="col-xs-7">Закрыта: '.$openDateLastWs.'</div>
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
            <th>Местоположение</th>
            <th>Статус задачи</th>
            <th>Обновлено</th>

          </tr>
        </thead>
        <tfoot>
          <tr>
          <th>Название задачи</th>
          <th>Описание задачи</th>
          <th>Местоположение</th>
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
    </div>';
  }

} else {

  foreach ($taskList as $key) {
     if((strtotime($key['updated']) >= strtotime($workshift['start_time'])) && (strcmp($key['status'], "completed") == 0)){
       $taskName = $key['name'];
       $taskDescription = $key['description'];
       $taskStatus = $key['status'];
       $taskUpdated = transformTimestamp($key['updated']);
       $taskPriority = $key['priority'];

       $taskLocationData = $key['taskLocation'];
       $locationName = $taskLocationData['locationName'];

       if(strcmp($taskStatus, "active") == 0) {
         $taskStatus = "Активна";
       } else if(strcmp($taskStatus, "completed") == 0) {
         $taskStatus = "Завершена";
       }

       $taskTable = $taskTable .
       '<tr id = '.$taskPriority.'>
        <td>'.$taskName.'</td>
        <td>'.$taskDescription.'</td>
        <td>'.$locationName.'</td>
        <td>'.$taskStatus.'</td>
        <td>'.$taskUpdated.'</td>';
    } else if(strcmp($key['status'], "active") == 0) {
      $taskName = $key['name'];
      $taskDescription = $key['description'];
      $taskStatus = "Активна";
      $taskUpdated = transformTimestamp($key['updated']);
      $taskPriority = $key['priority'];

      $taskLocationData = $key['taskLocation'];
      $locationName = $taskLocationData['locationName'];

      $taskTable = $taskTable .
      '<tr id = '.$taskPriority.'>
       <td>'.$taskName.'</td>
       <td>'.$taskDescription.'</td>
       <td>'.$locationName.'</td>
       <td>'.$taskStatus.'</td>
       <td>'.$taskUpdated.'</td>';
    }
  }


  //Получаем инфо о местоположении
  $workshiftUUID = $workshift['workshift_id'];

  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  // get запрос
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/locations/$workshiftUUID");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);
  $reqRes = json_decode($result, true);
  $route = $reqRes['data'];


  //Получаем карту
  $curl = curl_init($host_api);
  curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
  curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
  // get запрос
  curl_setopt($curl, CURLOPT_URL, "$host_api/api/v1/get/maps/1");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $result = curl_exec($curl);
  curl_close($curl);
  $reqRes = json_decode($result, true);
  $map = $reqRes['data'];




  $coordinates = "";
  $index = 0;

  $startCoordinate = $route[0];
  $startLatitude = $startCoordinate['latitude'];
  $startLongitude = $startCoordinate['longitude'];
  $endLatitude = "";
  $endLongitude = "";

  $showRoute = "";

  if(!empty($route)) {
    foreach ($route as $coordinate) {
      if((count($route)-1) != $index) {
        $coordinates = $coordinates .
          "[".$coordinate['longitude'].", ".$coordinate['latitude']."],";
          $index++;
      } else {
        $coordinates = $coordinates .
          "[".$coordinate['longitude'].", ".$coordinate['latitude']."]";
        $endLongitude = $coordinate['longitude'];
        $endLatitude = $coordinate['latitude'];
      }
    }

    $showRoute = 'map.addSource(\'route\', {
      \'type\': \'geojson\',
      \'data\': {
      \'type\': \'Feature\',
      \'properties\': {},
      \'geometry\': {
      \'type\': \'LineString\',
      \'coordinates\': [
      '.$coordinates.'
      ]
      }
      }
    });
    map.addLayer({
      \'id\': \'route\',
      \'type\': \'line\',
      \'source\': \'route\',
      \'layout\': {
      \'line-join\': \'round\',
      \'line-cap\': \'round\'
      },
      \'paint\': {
      \'line-color\': \'#EB0054\',
      \'line-width\': 7
      }
    });';

    $showMarker = 'var marker = new mapboxgl.Marker()
      .setLngLat(['.$endLongitude.', '.$endLatitude.'])
      .addTo(map);';

  }



  $openDate = transformTimestamp($workshift['start_time']);
  $workshiftInfo = '<div class="row">
    <div class="col-md-7">
      <div class="row">
        <div class="col-xs-5"><label>Смена:</label></div>
        <div class="col-xs-7"></div>
      </div>
      <div class="row">
        <div class="col-xs-5"><label></label></div>
        <div class="col-xs-7">Открыта: '.$openDate.'</div>
      </div>
      <div class="row">
        <div class="col-xs-5"><label></label></div>
        <div class="col-xs-7">Закрыта: </div>
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
          <th>Местоположение</th>
          <th>Статус задачи</th>
          <th>Обновлено</th>

        </tr>
      </thead>
      <tfoot>
        <tr>
        <th>Название задачи</th>
        <th>Описание задачи</th>
        <th>Местоположение</th>
        <th>Статус задачи</th>
        <th>Обновлено</th>
        </tr>
      </tfoot>
      <tbody id="content">
        '.$taskTable.'
      </tbody>
    </table>
    </div>
  </div>';
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


          <!-- Start show details here -->
          <div class="col-xs-12">
            <div class="box-content card">
              <h4 class="box-title"><i class="fa fa-user ico"></i>Карточка сотрудника</h4>
              <!-- /.box-title -->

              <!-- /.dropdown js__dropdown -->
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


                  <div class="row">
                    <div class="col-md-7">
                      <div class="row">
                        <div class="col-xs-5"><label>Должность:</label></div>
                        <div class="col-xs-7">'.$userPosition.'</div>
                      </div>
                    </div>
                  </div>

                  <!-- Workshift info here -->
                  '.$workshiftInfo.'

                    <a href="workshifts.php?id='.$tabelId.'" class="btn btn-primary waves-effect waves-light">Все смены</a>



              </div>
              <!-- /.card-content -->
            </div>
            <!-- /.box-content card -->
        </div>





        <div class="col-xs-12">
              <div class="box-content card">
                <h4 class="box-title"><i class="fa fa-map-marker ico"></i>Местоположение</h4>

                <div id="map" style="width: 100%; height: 500px"></div>
                <script>
                  mapboxgl.accessToken = \'pk.eyJ1Ijoia3N5cGljbWljcm8iLCJhIjoiY2s5dnN0MGRiMDJiOTNmb2c3d2hiMmVoOSJ9.sb3kTG1GxvoTMC8fJPwCRQ\';
                var map = new mapboxgl.Map({
                container: \'map\', // container id
                style: \'mapbox://styles/mapbox/streets-v11\', // stylesheet location
                center: [30.412779, 60.071886], // starting position [lng, lat]
                zoom: 15 // starting zoom
                });

                '.$showMarker.'

                var rooms = {
                  \'type\': \'FeatureCollection\',
                  \'features\': [
                  {
                    \'type\': \'Feature\',
                    \'properties\': {
                    \'description\': "Цех 1"
                    },
                    \'geometry\': {
                    \'type\': \'Point\',
                    \'coordinates\': [30.411765575408936,60.071607095275525]
                    }
                  },
                  {
                    \'type\': \'Feature\',
                    \'properties\': {
                    \'description\': "Цех 2"
                    },
                    \'geometry\': {
                    \'type\': \'Point\',
                    \'coordinates\': [30.414855480194092,60.07280610103768]
                    }
                  },
                  {
                    \'type\': \'Feature\',
                    \'properties\': {
                    \'description\': "Цех 3"
                    },
                    \'geometry\': {
                    \'type\': \'Point\',
                    \'coordinates\': [30.412087440490723, 60.070343809910376]
                    }
                  }]
              };


                  map.on(\'load\', function() {

                    map.addSource(\'maine\', {
                    \'type\': \'geojson\',
                    \'data\': {
                      "type": "FeatureCollection",
                        "features": [
                          {
                            "type": "Feature",
                            "properties": {},
                            "geometry": {
                              "type": "Polygon",
                              "coordinates": [
                                [
                                  [
                                    30.4076886177063,
                                    60.070065452392996
                                  ],
                                  [
                                    30.41736602783203,
                                    60.070065452392996
                                  ],
                                  [
                                    30.41736602783203,
                                    60.07353404755727
                                  ],
                                  [
                                    30.4076886177063,
                                    60.07353404755727
                                  ],
                                  [
                                    30.4076886177063,
                                    60.070065452392996
                                  ]
                                ]
                              ]
                            }
                          },
                          {
                            "type": "Feature",
                            "properties": {},
                            "geometry": {
                              "type": "LineString",
                              "coordinates": [
                                [
                                  30.40915846824646,
                                  60.07353404755727
                                ],
                                [
                                  30.409179925918576,
                                  60.07273116445446
                                ],
                                [
                                  30.4076886177063,
                                  60.07270975397085
                                ]
                              ]
                            }
                          },
                          {
                            "type": "Feature",
                            "properties": {},
                            "geometry": {
                              "type": "LineString",
                              "coordinates": [
                                [
                                  30.40766716003418,
                                  60.07072922413352
                                ],
                                [
                                  30.417344570159912,
                                  60.070707812350385
                                ]
                              ]
                            }
                          },
                          {
                            "type": "Feature",
                            "properties": {},
                            "geometry": {
                              "type": "LineString",
                              "coordinates": [
                                [
                                  30.41431903839111,
                                  60.070718518243694
                                ],
                                [
                                  30.41431903839111,
                                  60.0700761584948
                                ]
                              ]
                            }
                          },
                          {
                            "type": "Feature",
                            "properties": {},
                            "geometry": {
                              "type": "LineString",
                              "coordinates": [
                                [
                                  30.40983438491821,
                                  60.07072922413352
                                ],
                                [
                                  30.40983438491821,
                                  60.0700761584948
                                ]
                              ]
                            }
                          },
                          {
                            "type": "Feature",
                            "properties": {},
                            "geometry": {
                              "type": "LineString",
                              "coordinates": [
                                [
                                  30.412280559539795,
                                  60.07353404755727
                                ],
                                [
                                  30.412237644195557,
                                  60.072142371090735
                                ],
                                [
                                  30.417344570159912,
                                  60.07212096022492
                                ]
                              ]
                            }
                          },
                          {
                            "type": "Feature",
                            "properties": {
                            "description": "Fords Theater"
                            },
                            "geometry": {
                            "type": "Point",
                            "coordinates": [30.417344570159912,60.07212096022492]
                            }
                            }
                        ]
                    }
                    });
                    map.addLayer({
                    \'id\': \'maine\',
                    \'type\': \'line\',
                    \'source\': \'maine\',
                    \'layout\': {},
                    \'paint\': {
                      \'line-color\': \'#00A9B0\',
                      \'line-width\': 5
                    }
                    });

                    map.addSource(\'rooms\', {
                      \'type\': \'geojson\',
                      \'data\': rooms
                      });

                      map.addLayer({
                        \'id\': \'poi-labels\',
                        \'type\': \'symbol\',
                        \'source\': \'rooms\',
                        \'layout\': {
                        \'text-field\': [\'get\', \'description\'],
                        \'text-variable-anchor\': [\'top\', \'bottom\', \'left\', \'right\'],
                        \'text-radial-offset\': 0.5,
                        \'text-justify\': \'auto\',
                        \'icon-image\': [\'concat\', [\'get\', \'icon\'], \'-15\']
                        }
                        });


                    '.$showRoute.'




                  });



                </script>
              </div>
            </div>






        <div class="col-xs-12">
          <div class="box-content card white">
            <h4 class="box-title">Добавить задачу</h4>
            <div class="card-content">
              <form class="form-horizontal" action="http://localhost:4567/api/v1/add/new_task" method="POST">

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Название задачи</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" id="inp-type-1" name="taskName" placeholder="Введите название задачи">
                  </div>
                </div>

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Описание задачи</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" id="inp-type-1" name="taskDescription" placeholder="Введите описание задачи">
                  </div>
                </div>

                <div class="form-group">
                  <label for="inp-type-1" class="col-sm-3 control-label">Местоположение</label>
                  <div class="col-sm-9">
      							<select class="form-control" name="taskRoom">
      								<option value="">Выберите местоположение</option>
      								<option value="20001">Цех 1</option>
      								<option value="20002">Цех 2</option>
      								<option value="20003">Цех 3</option>
      							</select>
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

//var_dump($locationName);
//echo $taskLocation;

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
