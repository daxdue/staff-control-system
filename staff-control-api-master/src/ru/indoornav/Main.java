package ru.indoornav;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import ru.indoornav.authentificate.AuthenticationDetails;
import ru.indoornav.authentificate.BasicAuthenticationFilter;
import ru.indoornav.authentificate.PBKDF2Hasher;
import ru.indoornav.dao.UserEntity;
import ru.indoornav.dao.implementation.IndoorNavDAO;
import ru.indoornav.location.Coordinate;
import ru.indoornav.location.Feature;
import ru.indoornav.location.Location;
import ru.indoornav.location.LocationData;
import ru.indoornav.rest.ApiResponse;
import ru.indoornav.rest.StandartResponse;
import ru.indoornav.rest.StatusResponse;

import java.util.*;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        PBKDF2Hasher hasher = new PBKDF2Hasher();
        String page = "<html>\n" +
                "<head>\n" +
                "   <meta charset=\"utf-8\">\n" +
                "   <title>test</title></head>\n" +
                "<body>\n" +
                "    <form action=\"/api/v1/add/new_user\" enctype=\"application/json\" method=\"POST\" name=\"myForm\" >\n" +
                "        <p><label for=\"first_name\">Имя:</label>\n" +
                "        <input type=\"text\" name=\"firstName\" id=\"fname\"></p>\n" +
                "\n" +
                "        <p><label for=\"last_name\">Фамилия:</label>\n" +
                "        <input type=\"text\" name=\"lastName\" id=\"lname\"></p>\n" +
                "\n" +
                "        <p><label for=\"password\">Пароль:</label>\n" +
                "        <input type=\"password\" name=\"password\" id=\"password\"></p>\n" +
                "\n" +
                "        <p><label for=\"tabelId\">Табельный номер:</label>\n" +
                "        <input type=\"text\" name=\"tabelId\" id=\"tid\"></p>\n" +
                "\n" +
                "        <p><label for=\"workerPosition\">Должность:</label>\n" +
                "        <input type=\"text\" name=\"workerPosition\" id=\"wposition\"></p>\n" +
                "\n" +
                "        <input value=\"Submit\" type=\"submit\" onclick=\"submitform()\">\n" +
                "    </form>\n" +
                "</body>\n" +
                "</html>";

        IndoorNavDAO indoorNavDAO = new IndoorNavDAO();

        path("/api", () -> {
           path("/v1", () -> {

               path("/get", () -> {
                   before("/*", new BasicAuthenticationFilter("/*", new AuthenticationDetails("user", hasher)));
                   get("/users", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "usersList",
                                       new Gson().toJsonTree(indoorNavDAO.getUsers())));
                   });

                   get("/users/:tabel_id", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "user",
                                       new Gson().toJsonTree(indoorNavDAO.getUser(Integer.parseInt(req.params(":tabel_id"))))));
                   });

                   get("/tasks/:executor_id", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "userTasks",
                                       new Gson().toJsonTree(indoorNavDAO.getUserTasks(Integer.parseInt(req.params(":executor_id"))))));
                   });

                   get("/tasks/workshift/:workshift_id", (req, res) -> {
                       res.type("application/json");
                       UUID workshift_id = UUID.fromString(req.params(":workshift_id"));
                       System.out.println(workshift_id.toString());
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "workshiftTasks",
                                       new Gson().toJsonTree(indoorNavDAO.getWorkshiftTask(workshift_id))));
                   });

                   get("/alltasks/:executor_id", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "userTasks",
                                       new Gson().toJsonTree(indoorNavDAO.getAllUserTasks(Integer.parseInt(req.params(":executor_id"))))));
                   });

                   get("/rooms", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "rooms",
                                       new Gson().toJsonTree(indoorNavDAO.getRooms())));
                   });

                   get("/workshifts/:tabel_id", (req, res) -> {
                      res.type("application/json");
                      return new Gson().toJson(
                              new StandartResponse(StatusResponse.SUCCESS, "workshift",
                                      new Gson().toJsonTree(indoorNavDAO.getWorkshift(Integer.parseInt(req.params(":tabel_id"))))));
                   });

                   get("/lastworkshifts/:tabel_id", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "workshift",
                                       new Gson().toJsonTree(indoorNavDAO.getLastWorkshift(Integer.parseInt(req.params(":tabel_id"))))));
                   });

                   get("/reports/:report_id", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "report",
                                       new Gson().toJsonTree(indoorNavDAO.getReport(Integer.parseInt(req.params(":report_id"))))));
                   });


                   get("/report/:report_id", (req, res) -> {
                       res.type("application/json");
                       UUID report_id = UUID.fromString(req.params(":report_id"));
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "report",
                                       new Gson().toJsonTree(indoorNavDAO.getWorkshiftReport(report_id))));
                   });

                   get("/workshifts/all/:tabel_id", (req, res) -> {
                       res.type("application/json");
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "workshifts",
                                       new Gson().toJsonTree(indoorNavDAO.getAllWorkshifts(Integer.parseInt(req.params(":tabel_id"))))));
                   });

                   get("/locations/:workshift_id", (req, res) -> {
                       res.type("application/json");
                       System.out.println("Запрос на получение локаций");
                       ArrayList<Coordinate> coordinates = indoorNavDAO.getLocationsList(UUID.fromString(req.params(":workshift_id")));

                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "locations",
                                       new Gson().toJsonTree(coordinates)));
                   });

                   get("/maps/:id", (req, res) -> {
                       res.type("application/json");
                       String map = indoorNavDAO.getMap(Integer.parseInt(req.params(":id")));

                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS, "map",
                                       new Gson().toJsonTree(map)));
                   });

               });


               path("/add", () -> {
                   before("/*", new BasicAuthenticationFilter("/*", new AuthenticationDetails("superuser", hasher)));
                   get("/users", (req, res) -> {
                       res.type("text/html");
                       return page;
                   });

                   post("/new_user", (req, res) -> {
                       res.type("application/json");

                       UserEntity user = new UserEntity(req.queryParams("firstName"), req.queryParams("lastName"),
                               req.queryParams("password"), req.queryParams("tabelId"), req.queryParams("workerPosition"));
                       indoorNavDAO.createUser(user);
                       int tabelId = Integer.parseInt(req.queryParams("tabelId"));
                       res.redirect("http://localhost:8888/indoornav/worker.php?id=" + tabelId);
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS)
                       );
                   });

                   get("/tasks/:tabel_id", (req, res) -> {
                      res.type("text/html");
                      List<Task> taskList = indoorNavDAO.getUserTasks(Integer.parseInt(req.params(":tabel_id")));

                      String tasks = new String();
                      for(Task task : taskList) {
                          tasks += "<p>Задание: " + task.getName() + "</p>" +
                                  "<p>Описание: " + task.getDescription() + "</p>" +
                                  "<p>Task ID: " + task.getTaskId().toString() + "</p>" +
                                  "<p>Местоположение: " + task.getTaskLocation().getLocationName() + "</p>" +
                                  "<p>Приоритет: " + task.getPriority() + "</p>" +
                                  "<p>Статус: " + task.getStatus() + "</p>" +
                                  "<p>=====================================</p>";
                      }

                      res.type("text/html");

                       String taskPage = "<html>\n" +
                               "<head>\n" +
                               "   <meta charset=\"utf-8\">\n" +
                               "   <title>test</title></head>\n" +
                               "<body>\n" +
                               "    \n" + tasks +
                               "<form action=\"/api/v1/add/new_task\" enctype=\"application/json\" method=\"POST\" name=\"myForm\" >\n" +
                               "      <p><label for=\"taskName\">Название задачи:</label>\n" +
                               "      <input type=\"text\" name=\"taskName\" id=\"name\"></p>\n" +
                               "\n" +
                               "      <p><label for=\"taskDescription\">Описание задачи:</label>\n" +
                               "      <input type=\"text\" name=\"taskDescription\" id=\"description\"></p>\n" +
                               "\n" +
                               "      <p><label for=\"taskLocation\">Местоположение: </label>\n" +
                               "      <input type=\"text\" name=\"taskLocation\" id=\"location\"></p>\n" +
                               "\n" +
                               "      <p><label for=\"taskRoom\">Номер помещения: </label>\n" +
                               "      <input type=\"text\" name=\"taskRoom\" id=\"room\"></p>\n" +
                               "\n" +
                               "      <p><label for=\"tabelId\">Табельный номер исполнителя:</label>\n" +
                               "      <input type=\"text\" name=\"tabelId\" id=\"tid\"></p>\n" +
                               "\n" +
                               "      <p><label for=\"taskPriority\">Приоритет:</label>\n" +
                               "      <input type=\"text\" name=\"taskPriority\" id=\"priority\"></p>\n" +
                               "\n" +
                               "      <input value=\"Добавить\" type=\"submit\" onclick=\"submitform()\">\n" +
                               "  </form>" +
                               "</body>\n" +
                               "</html>";

                      return taskPage;
                   });

                   post("/new_task", (req, res) -> {
                       res.type("application/json");

                       String taskName = req.queryParams("taskName");
                       String taskDescription = req.queryParams("taskDescription");
                       String taskLocation = req.queryParams("taskLocation");
                       String roomId = req.queryParams("taskRoom");
                       int tabelId = Integer.parseInt(req.queryParams("tabelId"));
                       int priority = Integer.parseInt(req.queryParams("taskPriority"));
                       System.out.println(taskName);
                       Room room = indoorNavDAO.getRoomInfo(Integer.parseInt(roomId));
                       TaskLocation tasLoc = new TaskLocation(room.getRoomName(), String.valueOf(room.getRoomId()),
                               room.getLatitude(), room.getLongitude());
                       Gson gson = new Gson();
                       String location = gson.toJson(tasLoc);

                       Task task = new Task();
                       task.setName(taskName);
                       task.setDescription(taskDescription);
                       task.setLocation(location);
                       task.setExecutorId(tabelId);
                       task.setTaskId(UUID.randomUUID());
                       task.setPriority(priority);
                       task.setStatus("active");
                       indoorNavDAO.addTask(task);
                       res.redirect("http://localhost:8888/indoornav/worker.php?id=" + tabelId);
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS)
                       );

                   });

                   post("/locations", (req, res) -> {
                       res.type("application/json");
                       //LocationData locationData = new Gson().fromJson(req.body(), LocationData.class);
                       ApiResponse apiResponse = new Gson().fromJson(req.body(), ApiResponse.class);
                       Location location = null;
                       GeoJsonPath route = null;
                       UUID workshiftUUID = null;

                       if(apiResponse.getDataType().equals("location")) {
                           route = new Gson().fromJson(apiResponse.getData(), GeoJsonPath.class);
                           indoorNavDAO.updateWorkerLocation(route);
                       }

                       return new Gson().toJson(new StandartResponse(StatusResponse.SUCCESS));
                   });

                   put("/tasks", (req, res)-> {
                      res.type("application/json");
                      Task task = new Gson().fromJson(req.body(), Task.class);
                      indoorNavDAO.updateTaskStatus(task);

                      return new Gson().toJson(
                              new StandartResponse(StatusResponse.SUCCESS));
                   });

                   post("/workshifts", (req, res) -> {
                       res.type("application/json");
                       Workshift workshift = new Gson().fromJson(req.body(), Workshift.class);
                       System.out.println(workshift.toString());
                       indoorNavDAO.createWorkshift(workshift);
                       return new Gson().toJson(new StandartResponse(StatusResponse.SUCCESS));
                   });

                   put("/workshifts", (req, res) -> {
                       res.type("application/json");
                       System.out.println(req.body());
                       WorkshiftResult workshiftResult = new Gson().fromJson(req.body(), WorkshiftResult.class);
                       Workshift workshift = new Gson().fromJson(workshiftResult.getWorkshift(), Workshift.class);
                       Report report = new Gson().fromJson(workshiftResult.getReport(), Report.class);

                       System.out.println(workshift.toString());
                       System.out.println(report.toString());
                       indoorNavDAO.closeWorkshift(workshift, report);
                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS));
                   });

                   post("/maps", (req, res) -> {
                       res.type("application/json");
                       JsonElement element = new Gson().toJsonTree(req.body());
                       System.out.println(element);

                       return new Gson().toJson(
                               new StandartResponse(StatusResponse.SUCCESS));
                   });

               });
           });
        });
    }

}
