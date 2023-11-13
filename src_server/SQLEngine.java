import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.text.Position;

public class SQLEngine {

    private Connection connection;
    private String url;
    private String username;
    private String password;

    public SQLEngine(String host, int port, String database, String username, String password) throws SQLException {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    public synchronized Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public String getEmployeeName(int userID) throws SQLException {
        String query = "SELECT first_name, last_name FROM employee WHERE id = " + userID;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getString("first_name") + " " + resultSet.getString("last_name");
        } else {
            throw new SQLException("No employee found with id " + userID);
        }
    }
    
    public String loginToAccount(String username, String password) throws SQLException {
        String roleQuery = "SELECT position, employee_id FROM employee_credentials " +
                "JOIN employee ON employee.id = employee_credentials.employee_id " +
                "WHERE login = ? AND password = ? AND position IN ('admin', 'employee', 'trainer')";
        try (PreparedStatement roleStatement = connection.prepareStatement(roleQuery)) {
            roleStatement.setString(1, username);
            roleStatement.setString(2, password);
            try (ResultSet resultSet = roleStatement.executeQuery()) {
                if (resultSet.next()) {
                    String position = resultSet.getString("position");
                    int employeeId = resultSet.getInt("employee_id");
                    return position + "," + employeeId;
                }
            }
        }
    
    
        String clientQuery = "SELECT client_id FROM client_credentials WHERE login = ? AND password = ?";
        try (PreparedStatement clientStatement = connection.prepareStatement(clientQuery)) {
            clientStatement.setString(1, username);
            clientStatement.setString(2, password);
            try (ResultSet clientResultSet = clientStatement.executeQuery()) {
                if (clientResultSet.next()) {
                    int clientId = clientResultSet.getInt("client_id");
                    return "client" + "," + clientId;
                }
            }
        }
    
        throw new SQLException("Invalid login credentials");
    }
    
    public int registerAccount(String username, String password, String firstName, String lastName, LocalDate birthDate, String phoneNumber, String email ) throws SQLException {
        String query = "INSERT INTO client (first_name, last_name, date_of_birth, phone_number, email) VALUES ('" + firstName + "', '" + lastName + "', '" + birthDate + "', '" + phoneNumber + "', '" + email + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        query = "SELECT id FROM client WHERE first_name = '" + firstName + "' AND last_name = '" + lastName + "' AND date_of_birth = '" + birthDate + "' AND phone_number = '" + phoneNumber + "' AND email = '" + email + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            int userID = resultSet.getInt("id");
            query = "INSERT INTO client_credentials (login, password, client_id) VALUES ('" + username + "', '" + password + "', '" + userID + " ')";
            statement.executeUpdate(query);
            return userID;
        } else {
            throw new SQLException("Invalid register credentials");
        }
    }

    public boolean canEnterTraining(int clientID, int roomID) throws SQLException {
        String query = "SELECT training.date, training.hour FROM reservation JOIN training ON training.id = reservation.training_id WHERE client_id = " + clientID + " and room = " + roomID;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Check if client has a reservation for this room
        if (resultSet.next()) {
            LocalDate trainingDate = resultSet.getDate("date").toLocalDate();
            LocalTime trainingHour = resultSet.getTime("hour").toLocalTime();

            // Check if training is today
            if (trainingDate.equals(LocalDate.now())) {
                // Check if 10 minutes before training
                if (LocalTime.now().isBefore(trainingHour) && LocalTime.now().isAfter(trainingHour.minusMinutes(10))) {
                    return true;
                }
                else {
                    return false;
                }

            }
        }
        return false;

    }
}
