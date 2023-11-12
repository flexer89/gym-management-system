import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.sound.midi.Soundbank;

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
        String query = "SELECT client_id FROM credentials WHERE login = '" + username + "' AND password = '" + password + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next() && resultSet.getInt("client_id") != 0) {
            return "client," + resultSet.getInt("client_id");
        } 
        
        query = "SELECT employee_id FROM credentials JOIN employee on employee.id=credentials.employee_id WHERE login = '" + username + "' AND password = '" + password + "' AND position = 'admin'";
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            int employeeId = resultSet.getInt("employee_id");
            if (employeeId != 0) {
                return "admin," + employeeId;
            }
        }

        query = "SELECT employee_id FROM credentials JOIN employee on employee.id=credentials.employee_id WHERE login = '" + username + "' AND password = '" + password + "' AND position = 'trainer'";
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            int employeeId = resultSet.getInt("employee_id");
            if (employeeId != 0) {
                return "trainer," + employeeId;
            }
        }
        return query;
    }
    public int registerAccount(String username, String password, String firstName, String lastName, LocalDate birthDate, String phoneNumber, String email ) throws SQLException {
        String query = "INSERT INTO client (first_name, last_name, date_of_birth, phone_number, email) VALUES ('" + firstName + "', '" + lastName + "', '" + birthDate + "', '" + phoneNumber + "', '" + email + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        query = "SELECT id FROM client WHERE first_name = '" + firstName + "' AND last_name = '" + lastName + "' AND date_of_birth = '" + birthDate + "' AND phone_number = '" + phoneNumber + "' AND email = '" + email + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            int userID = resultSet.getInt("id");
            query = "INSERT INTO credentials (login, password, client_id) VALUES ('" + username + "', '" + password + "', '" + userID + " ')";
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

    public boolean canEnterGym(int card_number, int gymID) throws SQLException {
        String query = "SELECT expiration_date, all_gyms_access, original_gym_id, client_id FROM client join membership_card on client.id = membership_card.client_id WHERE membership_card.card_number = " + card_number;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
            boolean allGymAccess = resultSet.getBoolean("all_gyms_access");
            int originalGymID = resultSet.getInt("original_gym_id");
            int clientID = resultSet.getInt("client_id");
            System.out.println(expirationDate + " " + allGymAccess + " " + originalGymID);

            // Format time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime now = LocalTime.now();
            String formattedTime = formatter.format(now);

            if ((allGymAccess || originalGymID == gymID) && expirationDate.isAfter(LocalDate.now())) {
                // Check if client entered already
                query = "SELECT * FROM gym_visits WHERE client_id = " + clientID + " AND gym_id = " + gymID + " AND exit_date IS NULL";
                resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return false;
                }
                query = "INSERT INTO gym_visits (client_id, gym_id, entrance_date, entrance_time) VALUES (" + clientID + ", " + gymID + ", '" + LocalDate.now() + "', '" + formattedTime + "')";
                statement.executeUpdate(query);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    public boolean canExitGym(int card_number, int gymID) throws SQLException {
        String query = "SELECT client_id FROM membership_card WHERE card_number = " + card_number;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            int clientID = resultSet.getInt("client_id");
            query = "SELECT * FROM gym_visits WHERE client_id = " + clientID + " AND gym_id = " + gymID + " AND exit_date IS NULL";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                // Format time
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime now = LocalTime.now();
                String formattedTime = formatter.format(now);

                query = "UPDATE gym_visits SET exit_date = '" + LocalDate.now() + "', exit_time = '" + formattedTime + "' WHERE client_id = " + clientID + " AND gym_id = " + gymID + " AND exit_date IS NULL";
                statement.executeUpdate(query);
                return true;
            }
        }
        return false;
    }

}
