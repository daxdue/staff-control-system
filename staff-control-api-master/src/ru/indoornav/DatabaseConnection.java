package ru.indoornav;

import java.sql.*;
import java.util.UUID;

public class DatabaseConnection {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/indoornav";
    static final String USER = "postgres";
    static final String PASS = "qzwxecasd123";
    static final String DB_DRIVER = "org.postgresql.Driver";

    public static void main(String[] args) {
        getUser(10001);
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    private static void getUser(int tabelId) {
        User user = null;
        Connection connection = getDBConnection();
        String query = "SELECT * FROM workers WHERE tabel_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tabelId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int worker_position = resultSet.getInt("worker_position");
                System.out.println(worker_position);
                query = "SELECT * FROM positions WHERE position_id  = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, worker_position);
                ResultSet resultSet1 = preparedStatement.executeQuery();

                while (resultSet1.next()) {

                    user = new User(resultSet.getString("worker_name"), resultSet1.getString("name"),
                            tabelId, UUID.fromString(resultSet.getString("worker_id")));

                }
            }
        } catch (SQLException se) {

        }

        //System.out.println("Name: " + user.getName() + ", position: " + user.getPosition() + ", id: " + user.getWorker_id());
    }
}
