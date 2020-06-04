package ru.indoornav.dao.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.hibernate.jdbc.Work;
import org.postgresql.util.PGobject;
import ru.indoornav.*;
import ru.indoornav.authentificate.PBKDF2Hasher;
import ru.indoornav.authentificate.UserService;
import ru.indoornav.dao.UserEntity;
import ru.indoornav.dao.interfaces.IndoorNavDaoInterface;
import ru.indoornav.location.Coordinate;
import ru.indoornav.location.Feature;
import ru.indoornav.location.LocationMap;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IndoorNavDAO implements IndoorNavDaoInterface {

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/indoornav";
    private static final String USER = "postgres";
    private static final String PASS = "qzwxecasd123";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    private PBKDF2Hasher hasher;

    public void setHasher(PBKDF2Hasher hasher) {
        this.hasher = hasher;
    }

    @Override
    public void createUser(UserEntity user) {
        System.out.println("Name:" + user.getName());
        System.out.println("Position: " + user.getWorkerPosition());
        System.out.println("Tabel id: " + user.getTabelId());
        System.out.println("Token: " + user.getToken());

        addUser(user.getName(), user.getWorkerPosition(), user.getTabelId(), user.getToken(), 555);
    }

    @Override
    public void addUser(String name, String position, int tabelId, String token, int rule) {

        if(!userExist(tabelId)) {
            String query = "INSERT INTO workers(worker_name, tabel_id, position, worker_id) VALUES (?, ?, ?, ?)";
            Connection connection = null;
            UUID workerId = UUID.randomUUID();


            try {
                connection = getDBConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setInt(2, tabelId);
                statement.setString(3, position);
                statement.setObject(4, workerId);
                statement.executeUpdate();

                query =  "INSERT INTO authentification (account_id, token, rule) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setInt(1, tabelId);
                statement.setString(2, token);
                statement.setInt(3, rule);
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void createWorkshift(Workshift workshift) {

        String query = "INSERT INTO workshifts(workshift_id, worker_id, start_time, status) " +
                "VALUES (?, ?, ?, ?)";
        Connection connection = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, UUID.fromString(workshift.getWorkshift_id()));
            statement.setInt(2, workshift.getWorker_id());
            statement.setObject(3, Timestamp.valueOf(workshift.getStart_time()));
            statement.setString(4, workshift.getStatus());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public User getUser(int tabelId) {

        User user = null;

        if(userExist(tabelId)) {
            String query = "SELECT * FROM workers WHERE tabel_id = ?";
            Connection connection = null;

            try {
                connection = getDBConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, tabelId);
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    user = new User();
                    user.setName(rs.getString("worker_name"));
                    user.setTabelId(tabelId);
                    user.setPosition(rs.getString("position"));
                    user.setWorkerId(UUID.fromString(rs.getString("worker_id")));
                }

                connection.commit();
            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            }
        }
        return user;
    }

    @Override
    public Collection<User> getUsers() {

        String query = "SELECT * FROM workers ORDER BY tabel_id";
        Connection connection = null;
        Collection<User> users = new ArrayList<User>();

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("worker_name"));
                user.setTabelId(rs.getInt("tabel_id"));
                user.setPosition(rs.getString("position"));
                user.setWorkerId(UUID.fromString(rs.getString("worker_id")));
                users.add(user);
            }
            connection.commit();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(connection !=  null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        return users;
    }

    @Override
    public List<Workshift> getAllWorkshifts(int tabelId) {

        String query = "SELECT * FROM workshifts WHERE worker_id = ? ORDER BY start_time DESC";
        Connection connection = null;
        List<Workshift> workshifts = new ArrayList<>();

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Workshift workshift = new Workshift();
                workshift.setWorkshift_id(rs.getString("workshift_id"));
                workshift.setWorker_id(tabelId);
                workshift.setStart_time(rs.getString("start_time"));
                workshift.setEnd_time(rs.getString("end_time"));
                workshift.setStatus(rs.getString("status"));
                workshift.setReport_id(rs.getString("report_id"));
                workshifts.add(workshift);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return workshifts;
    }

    @Override
    public UserService getSuperuserCredentials() {
        String query = "SELECT * FROM authentification WHERE rule = ?";
        Connection connection = null;
        UserService userService = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 777);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                userService = new UserService(rs.getInt("account_id"), rs.getString("token"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return userService;
    }

    @Override
    public UserService getUserCredentials(int tabelId) {
        String query = "SELECT * FROM authentification WHERE account_id = ?";
        Connection connection = null;
        UserService user = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                user = new UserService(rs.getInt("account_id"), rs.getString("token"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    @Override
    public boolean userExist(int tabelId) {
        boolean userExist = false;
        String query = "SELECT true FROM workers WHERE tabel_id = ? LIMIT 1";
        Connection connection = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                userExist = true;
            }
            connection.commit();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        return userExist;
    }

    @Override
    public boolean workshiftOpened(int tabelId) {
        boolean workshiftOpened = false;
        String query = "SELECT * FROM workshifts WHERE worker_id = ?";
        Connection connection = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                if(rs.getString("status").equals("active")) {
                    workshiftOpened = true;
                    break;
                }
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return workshiftOpened;
    }


    @Override
    public void addTask(Task task) {

        String query = "SELECT worker_id FROM workers WHERE tabel_id = ?";
        Connection connection = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, task.getExecutorId());
            ResultSet rs = statement.executeQuery();

            UUID worker_id = null;
            while (rs.next()) {
                worker_id = (UUID) rs.getObject("worker_id");
            }

            query = "INSERT INTO tasks(task_id, name, description, executor_id, location, priority, status) VALUES (?, ?, ?, ?, ?::json, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setObject(1, task.getTaskId());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setObject(4, worker_id);
            statement.setObject(5, task.getLocation());
            statement.setInt(6, task.getPriority());
            statement.setString(7, task.getStatus());
            statement.executeUpdate();
            connection.commit();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Task> getUserTasks(int tabelId) {
        String query = "SELECT worker_id FROM workers WHERE tabel_id = ?";
        Connection connection = null;
        List<Task> taskList = new ArrayList<Task>();

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            UUID worker_id = null;
            while (rs.next()) {
                worker_id = (UUID) rs.getObject("worker_id");
            }

            query = "SELECT * FROM tasks WHERE executor_id = ?  AND status = ? ORDER BY priority";
            statement = connection.prepareStatement(query);
            statement.setObject(1, worker_id);
            statement.setString(2, "active");
            rs = statement.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId((UUID)rs.getObject("task_id"));
                task.setName(rs.getString("name"));
                task.setDescription(rs.getString("description"));
                Gson gson = new Gson();
                TaskLocation taskLocation = gson.fromJson(rs.getString("location"), TaskLocation.class);
                task.setTaskLocation(taskLocation);
                task.setExecutorId(tabelId);
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                taskList.add(task);
            }

            connection.commit();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        return taskList;
    }

    @Override
    public List<Task> getWorkshiftTask(UUID workshift_id) {
        String query = "SELECT * FROM tasks WHERE workshift_id = ? ORDER BY updated DESC";
        Connection connection = null;
        List<Task> taskList = new ArrayList<>();

        try {
            connection  = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, workshift_id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId((UUID)rs.getObject("task_id"));
                task.setName(rs.getString("name"));
                task.setDescription(rs.getString("description"));
                Gson gson = new Gson();
                TaskLocation taskLocation = gson.fromJson(rs.getString("location"), TaskLocation.class);
                task.setTaskLocation(taskLocation);
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                task.setUpdated(rs.getObject("updated").toString());
                taskList.add(task);
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return taskList;
    }

    @Override
    public List<Task> getAllUserTasks(int tabelId) {
        String query = "SELECT worker_id FROM workers WHERE tabel_id = ?";
        Connection connection = null;
        List<Task> taskList = new ArrayList<Task>();

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            UUID worker_id = null;
            while (rs.next()) {
                worker_id = (UUID) rs.getObject("worker_id");
            }

            query = "SELECT * FROM tasks WHERE executor_id = ? ORDER BY priority";
            statement = connection.prepareStatement(query);
            statement.setObject(1, worker_id);
            rs = statement.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId((UUID)rs.getObject("task_id"));
                task.setName(rs.getString("name"));
                task.setDescription(rs.getString("description"));
                Gson gson = new Gson();
                TaskLocation taskLocation = gson.fromJson(rs.getString("location"), TaskLocation.class);
                task.setTaskLocation(taskLocation);
                task.setExecutorId(tabelId);
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                task.setUpdated(rs.getObject("updated").toString());
                taskList.add(task);
            }

            connection.commit();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        return taskList;
    }

    @Override
    public void updateTaskStatus(Task task) {

        String query = "UPDATE tasks SET status = ?, updated = ?, workshift_id = ? WHERE task_id = ?";
        Connection connection = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, task.getStatus());
            statement.setTimestamp(2, getTimestamp());
            statement.setObject(3, UUID.fromString(task.getWorkshiftId()));
            statement.setObject(4, task.getTaskId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public Workshift getWorkshift(int tabelId) {

        Workshift workshift = null;

        if(workshiftOpened(tabelId)) {
            String query = "SELECT * FROM workshifts WHERE worker_id = ? AND status = ?";
            Connection connection = null;
            try {
                connection = getDBConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, tabelId);
                statement.setString(2, "active");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    workshift = new Workshift();
                    workshift.setWorkshift_id(rs.getString("workshift_id"));
                    workshift.setWorker_id(tabelId);
                    workshift.setStart_time(rs.getString("start_time"));
                    workshift.setStatus(rs.getString("status"));

                }
                System.out.println(workshift.getWorker_id());
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return workshift;
    }

    @Override
    public Workshift getLastWorkshift(int tabelId) {

        Workshift workshift = null;
        if(!workshiftOpened(tabelId)) {
            String query = "SELECT * FROM workshifts WHERE worker_id = ? AND status = ? ORDER BY id DESC LIMIT 1";
            Connection connection = null;
            try {
                connection = getDBConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, tabelId);
                statement.setString(2, "closed");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    workshift = new Workshift();
                    workshift.setWorkshift_id(rs.getString("workshift_id"));
                    workshift.setWorker_id(tabelId);
                    workshift.setStart_time(rs.getString("start_time"));
                    workshift.setEnd_time(rs.getString("end_time"));
                    workshift.setStatus(rs.getString("status"));

                }
                System.out.println(workshift.getWorker_id());
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return workshift;
    }


    @Override
    public void closeWorkshift(Workshift workshift, Report report) {

        if(workshiftOpened(workshift.getWorker_id())) {
            String query = "UPDATE workshifts SET end_time = ?, status = ?, report_id = ? WHERE worker_id = ? AND status = ?";
            Connection connection = null;

            try {
                connection = getDBConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setTimestamp(1, Timestamp.valueOf(workshift.getEnd_time()));
                statement.setString(2, workshift.getStatus());
                statement.setObject(3, UUID.fromString(workshift.getReport_id()));
                statement.setInt(4, workshift.getWorker_id());
                statement.setString(5, "active");
                statement.executeUpdate();

                query = "INSERT INTO reports (report_id, worker_id, time, content) VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setObject(1, UUID.fromString(report.getReport_id()));
                statement.setInt(2, workshift.getWorker_id());
                statement.setTimestamp(3, Timestamp.valueOf(report.getTime()));
                statement.setString(4, report.getContent());
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Workshift closed");
        }
    }

    @Override
    public Report getReport(int tabelId) {

        String query = "SELECT * FROM reports WHERE worker_id = ? ORDER BY id DESC LIMIT 1";
        Connection connection = null;
        Report report = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tabelId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                report = new Report();
                report.setReport_id(rs.getString("report_id"));
                report.setWorker_id(String.valueOf(rs.getInt("worker_id")));
                report.setTime(rs.getString("time"));
                report.setContent(rs.getString("content"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return report;
    }


    @Override
    public Report getWorkshiftReport(UUID report_id) {
        String query = "SELECT * FROM reports WHERE report_id = ?";
        Connection connection = null;
        Report report = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, report_id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                report = new Report();
                report.setReport_id(rs.getString("report_id"));
                report.setWorker_id(String.valueOf(rs.getInt("worker_id")));
                report.setTime(rs.getString("time"));
                report.setContent(rs.getString("content"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return report;
    }

    @Override
    public Room getRoomInfo(int roomId) {

        String query = "SELECT * FROM rooms WHERE room_id = ?";
        Connection connection = null;
        Room room = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                room = new Room(roomId, rs.getString("name"), rs.getString("latitude"),
                        rs.getString("longitude"));
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return room;
    }

    @Override
    public List<Room> getRooms() {
        String query = "SELECT * FROM rooms";
        Connection connection = null;
        List<Room> roomList = new ArrayList<>();

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomName(rs.getString("name"));
                room.setRoomId(rs.getInt("room_id"));
                room.setLatitude(rs.getString("latitude"));
                room.setLongitude(rs.getString("longitude"));
                roomList.add(room);
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return roomList;
    }

    @Override
    public void updateWorkerLocation(GeoJsonPath route) {

        String query = "SELECT true FROM locations WHERE workshift_id = ? LIMIT 1";
        Connection connection = null;
        boolean workshiftLocationExist = false;



        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, route.getWorkshiftId());
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                workshiftLocationExist = true;
            }

            String routeAsJson = new Gson().toJson(route.getRoute());
            PGobject locationObject = new PGobject();
            locationObject.setType("json");
            locationObject.setValue(routeAsJson);

            if(workshiftLocationExist) {

                query = "UPDATE locations SET location_track = ? WHERE workshift_id = ?";
                statement = connection.prepareStatement(query);

                statement.setObject(1, locationObject);
                statement.setObject(2, route.getWorkshiftId());
                statement.executeUpdate();

            } else {

                query = "INSERT INTO locations (location_track, workshift_id) VALUES (?, ?)";
                statement = connection.prepareStatement(query);

                statement.setObject(1, locationObject);
                statement.setObject(2, route.getWorkshiftId());
                statement.executeUpdate();

            }
            connection.commit();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Coordinate> getLocationsList(UUID workshiftUUID) {

        String query = "SELECT * FROM locations WHERE workshift_id = ?";
        Connection connection = null;
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        String jsonElement = null;

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, workshiftUUID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                jsonElement = rs.getString("location_track");
            }
            connection.commit();

            //Type listType = new TypeToken<List<Feature>>() {}.getType();
            //features = new Gson().fromJson(jsonElement, listType);
            Type coordinatesListType = new TypeToken<List<Coordinate>>() {}.getType();
            coordinates = new Gson().fromJson(jsonElement, coordinatesListType);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return coordinates;

    }

    public String getMap(int mapId) {

        String query = "SELECT * FROM maps WHERE id = ?";
        Connection connection = null;
        String map = new String();

        try {
            connection = getDBConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, mapId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                map = rs.getString("map");
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }


    private static Connection getDBConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private Timestamp getTimestamp() {
        java.util.Date date = new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        return timestamp;
    }
}
