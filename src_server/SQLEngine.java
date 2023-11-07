import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

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
    
    public int loginToAccount(String username, String password) throws SQLException {
        String query = "SELECT id FROM client_credentials WHERE login = '" + username + "' AND password = '" + password + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("id");
        } else {
            throw new SQLException("Invalid login credentials");
        }
    }
    public int registerAccount(String username, String password, String firstName, String lastName, LocalDate birthDate, String phoneNumber, String email ) throws SQLException {
        String query = "INSERT INTO client_credentials (login, password) VALUES ('" + username + "', '" + password + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        query = "SELECT id FROM client_credentials WHERE login = '" + username + "' AND password = '" + password + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            int userID = resultSet.getInt("id");
            query = "INSERT INTO client (id, first_name, last_name, date_of_birth, phone_number, email) VALUES (" + userID + ", '" + firstName + "', '" + lastName + "', '" + birthDate + "', '" + phoneNumber + "', '" + email + "')";
            statement.executeUpdate(query);
            return userID;
        } else {
            throw new SQLException("Invalid register credentials");
        }
    }
}
