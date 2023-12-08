package com.example.bd8.Models;

import com.example.bd8.HelloController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.postgresql.util.PGInterval;
import java.sql.*;
import java.util.Arrays;

public class PlayerViewModel {
   static HelloController controller;

    SimpleStringProperty first_name;
    SimpleStringProperty last_name;
    SimpleIntegerProperty age;
    SimpleStringProperty email;
    SimpleIntegerProperty id;

    Connection connection;
    // JDBC connection parameters
    String jdbcUrl = "jdbc:postgresql://localhost:5432/BGC";
    String username = "postgres";
    String password = "alex235689";

    public PlayerViewModel(HelloController controller) {
        this.controller = controller;
        this.first_name = new SimpleStringProperty();
        this.last_name = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty();
        this.id = new SimpleIntegerProperty();
        try {
//            Class.forName("org.postgresql.Driver");
            // Establish a connection
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

    public PlayerViewModel (HelloController controller, String sqlQuery){
        this.controller = controller;
        //set up a connection

        this.first_name = new SimpleStringProperty();
        this.last_name = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty();
        this.id = new SimpleIntegerProperty();

        try {
//            Class.forName("org.postgresql.Driver");
            // Establish a connection
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getData(sqlQuery);
    }
    public PlayerViewModel(HelloController controller, String first_name, String last_name, Integer age, String email, Integer id) {
        this.controller = controller;
        this.first_name = new SimpleStringProperty(first_name);
        this.last_name = new SimpleStringProperty(last_name);
        this.age = new SimpleIntegerProperty(age);
        this.email = new SimpleStringProperty(email);
        this.id = new SimpleIntegerProperty(id);

        //set up a connection
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getFirst_name() {
        return first_name.get();
    }

    public SimpleStringProperty first_nameProperty() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }

    public String getLast_name() {
        return last_name.get();
    }

    public SimpleStringProperty last_nameProperty() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name.set(last_name);
    }

    public int getAge() {
        return age.get();
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void getData(String sqlQuery){
        try (
                // Create a PreparedStatement with the SQL query
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                // Execute the query and get the ResultSet
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            // Process the ResultSet
            while (resultSet.next()) {
                // Retrieve data from the result set
//                int column1Value = resultSet.getInt("column1");
//                String column2Value = resultSet.getString("column2");

                first_name.set(resultSet.getString("first_name"));
                last_name.set(resultSet.getString("last_name"));
                age.set(resultSet.getInt("age"));
                email.set(resultSet.getString("email"));
                id.set(resultSet.getInt("player_id"));
                // ... repeat for other columns

                // Print or process the retrieved data
                System.out.println("name: " + first_name.get() + ", second name: " + last_name.get());
            }
        } catch (SQLException e) {
        // Additional processing for SQLException
        handleSQLException(e);
        } catch (IllegalArgumentException e){
        handleIllegalArguments(e);
        }
    }

    public void insertIntoTable(String sqlQuery, String first_name, String last_name, Integer age, String email){
        try  {
            // Create a PreparedStatement with the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, email);
            // Execute the query and get the ResultSet
            preparedStatement.executeUpdate();

//            String sqlQueryGet = "SELECT * FROM player WHERE first_name = " + first_name + " and " + "last_name = " + last_name + " and " + "age = " + age;
            String sqlQueryGet = "SELECT * FROM player WHERE first_name = ? AND last_name = ? AND age = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQueryGet);
            preparedStatement2.setString(1, first_name);
            preparedStatement2.setString(2, last_name);
            preparedStatement2.setInt(3, age);

            ResultSet resultSet = preparedStatement2.executeQuery();

            try {
                // Process the ResultSet
                while (resultSet.next()) {
                    // Retrieve data from the result set
//                int column1Value = resultSet.getInt("column1");
//                String column2Value = resultSet.getString("column2");

                    this.first_name.set(resultSet.getString("first_name"));
                    this.last_name.set(resultSet.getString("last_name"));
                    this.age.set(resultSet.getInt("age"));
                    this.email.set(resultSet.getString("email"));
                    this.id.set(resultSet.getInt("player_id"));
                    // ... repeat for other columns

                    // Print or process the retrieved data
                    System.out.println("name: " + this.first_name.get() + ", second name: " + this.last_name.get());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            // Additional processing for SQLException
            handleSQLException(e);
        } catch (IllegalArgumentException e){
            handleIllegalArguments(e);
        }
    }

    public void executeProcedureInsertSession(String call, Integer[] participants, Integer session_winner, Integer room_taken, Integer gameset_taken, String timestamp, String duration){
        try {
            // Load the JDBC driver
            Class.forName("org.postgresql.Driver");

            // Define the SQL call for the stored procedure

            // Create a CallableStatement
            CallableStatement callableStatement = connection.prepareCall(call);

            // Set the input parameters for the stored procedure

//             participants (array)
//            Integer[] participantsArray = {1, 2, 3, 4}; // Example values
            Array participantsSQL = connection.createArrayOf("integer", participants);
            callableStatement.setArray(1, participantsSQL);

            // Convert Integer array to Object array
//            Object[] objectArray = Arrays.copyOf(participants, participants.length, Object[].class);
//
//            // Set the parameter as an array
//            callableStatement.setObject(1, objectArray);

            // session_winner_id (int)
//            int sessionWinnerId = 5; // Example value
            callableStatement.setInt(2, session_winner);

            // room_taken_id (int)
//            int roomTakenId = 10; // Example value
            callableStatement.setInt(3, room_taken);

            // gameset_taken_id (int)
//            int gamesetTakenId = 20; // Example value
            callableStatement.setInt(4, gameset_taken);

            // beginning_timestamp (timestamp)
            // example timestamp
            // 2023-01-01 12:00:00
            Timestamp beginningTimestamp = Timestamp.valueOf(timestamp);
            callableStatement.setTimestamp(5, beginningTimestamp);

            // session_duration (interval)
            PGInterval sessionDuration = new PGInterval(duration); // 2 hours and 30 minutes
            callableStatement.setObject(6, sessionDuration);

            // Execute the stored procedure
            callableStatement.execute();

            // Close resources
            callableStatement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            // Additional processing for SQLException
            handleSQLException(e);
        } catch (IllegalArgumentException e){
            handleIllegalArguments(e);
        }

    }

    private void handleIllegalArguments(IllegalArgumentException e) {
        System.err.println("SQLException:");
        StringBuilder str = new StringBuilder();

            str.append("SQL State  : " + e.getMessage() + "\n");
            str.append("Error Code : " + e.getCause() + "\n");
            str.append("Message    : " + e.toString() + "\n");

        controller.sessionErrorLabel.setText(str.toString());
        controller.playerErrorLabel.setText(str.toString());
        controller.playerErrorLabel1.setText(str.toString());
    }

    private static void handleSQLException(SQLException e) {
        System.err.println("SQLException:");
        StringBuilder str = new StringBuilder();

        while (e != null) {
            str.append("SQL State  : " + e.getSQLState() + "\n");
            str.append("Error Code : " + e.getErrorCode() + "\n");
            str.append("Message    : " + e.getMessage() + "\n");

            e = e.getNextException();
            if (e != null) {
                System.err.println("--- Next Exception ---");
            }
        }
        controller.sessionErrorLabel.setText(str.toString());
        controller.playerErrorLabel.setText(str.toString());
        controller.playerErrorLabel1.setText(str.toString());
    }
}
