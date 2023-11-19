import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.swing.plaf.nimbus.State;

import utils.Secure;

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
    
    public String getIDbyLogin(String login) {
        String employeeQuery = "SELECT id FROM employee_credentials WHERE login = ?";
        String clientQuery = "SELECT id FROM client_credentials WHERE login = ?";
        ResultSet resultSet = null;
    
        try (PreparedStatement employeeStatement = connection.prepareStatement(employeeQuery);
             PreparedStatement clientStatement = connection.prepareStatement(clientQuery)) {
    
            employeeStatement.setString(1, login);
            resultSet = employeeStatement.executeQuery();
    
            if (resultSet.next()) {
                return resultSet.getInt("id")+","+"employee_credentials";
            } else {
                resultSet.close(); // Close the previous ResultSet
    
                clientStatement.setString(1, login);
                resultSet = clientStatement.executeQuery();
    
                if (resultSet.next()) {
                    return resultSet.getInt("id")+","+"client_credentials";
                } else {
                    throw new SQLException("No user found with login " + login);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getHashByID(int id, String table) {
        String query = "SELECT password FROM " + table + " WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getString("password");
            } else {
                throw new SQLException("No user found with id " + id + " in table " + table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSaltByID(int id, String table) {
        String query = "SELECT salt FROM " + table + " WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getString("salt");
            } else {
                throw new SQLException("No user found with id " + id + " in table " + table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loginToAccount(String username, String hash) throws SQLException {

        System.out.println("Trying to log a employee");
        String roleQuery = "SELECT position, employee_id FROM employee_credentials " +
                "JOIN employee ON employee.id = employee_credentials.employee_id " +
                "WHERE login = ? AND password = ? AND position IN ('admin', 'employee', 'trainer')";
        try (PreparedStatement roleStatement = connection.prepareStatement(roleQuery)) {
            roleStatement.setString(1, username);
            roleStatement.setString(2, hash);
            try (ResultSet resultSet = roleStatement.executeQuery()) {
                if (resultSet.next()) {
                    String position = resultSet.getString("position");
                    int employeeId = resultSet.getInt("employee_id");
                    return position + "," + employeeId;
                }
            }
        }
    
        System.out.println("Trying to log as client");
        String clientQuery = "SELECT client_id FROM client_credentials WHERE login = ? AND password = ?";
        try (PreparedStatement clientStatement = connection.prepareStatement(clientQuery)) {
            clientStatement.setString(1, username);
            clientStatement.setString(2, hash);
            try (ResultSet clientResultSet = clientStatement.executeQuery()) {
                if (clientResultSet.next()) {
                    int clientId = clientResultSet.getInt("client_id");
                    return "client" + "," + clientId;
                }
            }
        }
    
        throw new SQLException("Invalid login credentials");
    }
    
    public int registerAccount(String username, String password, String salt, String firstName, String lastName, LocalDate birthDate, String phoneNumber, String email ) throws SQLException {
        String insertClientQuery = "INSERT INTO client (first_name, last_name, date_of_birth, phone_number, email) VALUES (?, ?, ?, ?, ?)";
        String selectClientQuery = "SELECT id FROM client WHERE first_name = ? AND last_name = ? AND date_of_birth = ? AND phone_number = ? AND email = ?";
        String insertCredentialsQuery = "INSERT INTO client_credentials (login, password, salt, client_id) VALUES (?, ?, ?, ?)";
        ResultSet resultSet = null;
    
        try (PreparedStatement insertClientStatement = connection.prepareStatement(insertClientQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement selectClientStatement = connection.prepareStatement(selectClientQuery);
             PreparedStatement insertCredentialsStatement = connection.prepareStatement(insertCredentialsQuery)) {
    
            insertClientStatement.setString(1, firstName);
            insertClientStatement.setString(2, lastName);
            insertClientStatement.setDate(3, java.sql.Date.valueOf(birthDate));
            insertClientStatement.setString(4, phoneNumber);
            insertClientStatement.setString(5, email);
            insertClientStatement.executeUpdate();
    
            resultSet = insertClientStatement.getGeneratedKeys();
    
            if (resultSet.next()) {
                int userID = resultSet.getInt(1);
                resultSet.close(); // Close the previous ResultSet
    
                insertCredentialsStatement.setString(1, username);
                insertCredentialsStatement.setString(2, password);
                insertCredentialsStatement.setString(3, salt);
                insertCredentialsStatement.setInt(4, userID);
                insertCredentialsStatement.executeUpdate();
    
                return userID;
            } else {
                throw new SQLException("Invalid register credentials");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

    public boolean addGym(String name, String address, String postalCode, String city, String phone, String email) throws SQLException{
        String query = "INSERT INTO gym (name, address, postal_code, city, phone, email) VALUES ('" + name + "', '" + address + "', '" + postalCode + "', '" + city + "', '" + phone + "', '" + email + "')";
        Statement statement = connection.createStatement();
        int count = statement.executeUpdate(query);
        if (count > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addEmployee(String name, String surname, String position, LocalDate dateOfBirth, LocalDate dateOfEmployment, String phone,
        String email, String login) throws SQLException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltString = Base64.getEncoder().encodeToString(salt);
        String hash = Secure.hashWithSalt("passwd", saltString);

        String query = "INSERT INTO employee (first_name, last_name, position, date_of_birth, date_of_employment, phone_number, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.setString(2, surname);
        pstmt.setString(3, position);
        pstmt.setObject(4, dateOfBirth);
        pstmt.setObject(5, dateOfEmployment);
        pstmt.setString(6, phone);
        pstmt.setString(7, email);
        int count = pstmt.executeUpdate();

        if (count > 0) {
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int employeeID = rs.getInt(1);
                query = "INSERT INTO employee_credentials (login, password, salt, employee_id) VALUES (?, ?, ?, ?)";
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, login);
                pstmt.setString(2, hash);
                pstmt.setString(3, saltString);
                pstmt.setInt(4, employeeID);
                count = pstmt.executeUpdate();

                if (count > 0) {
                    query = "INSERT INTO employee_card (employee_number, expiration_date, employee_id) VALUES (?, ?, ?)";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, employeeID);
                    pstmt.setObject(2, dateOfEmployment.plusYears(1));
                    pstmt.setInt(3, employeeID);
                    count = pstmt.executeUpdate();

                    if (count > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String paymentReport(LocalDate fromDate, LocalDate toDate, int minimumPayment, int maximumPayment,
            String paymentMethod) throws SQLException{
        // Select payments between fromDate and toDate and between minimumPayment and maximumPayment
        String query = "SELECT * FROM payment WHERE payment_date BETWEEN '" + fromDate + "' AND '" + toDate + "' AND amount BETWEEN " + minimumPayment + " AND " + maximumPayment;

        if (!paymentMethod.equals("all")) {
            query += " AND payment_method = '" + paymentMethod + "'";
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        String report = "";
        while (resultSet.next()) {
            report += resultSet.getInt("id") + "," + resultSet.getDate("payment_date") + "," + resultSet.getInt("amount") + "," + resultSet.getString("payment_method") + "," + resultSet.getInt("client_id") + "///";
        }
        return report;
    }

    public String gymReport(String name, String address, String postalCode, String city, String phoneNumber,
            String email) throws SQLException {
        String query = "SELECT * FROM gym WHERE 1=1";

        if (!name.equals("%")) {
            query += " AND name = '" + name + "'";
        }
        if (!address.equals("%")) {
            query += " AND address = '" + address + "'";
        }
        if (!postalCode.equals("%")) {
            query += " AND postal_code = '" + postalCode + "'";
        }
        if (!city.equals("%")) {
            query += " AND city = '" + city + "'";
        }
        if (!phoneNumber.equals("%")) {
            query += " AND phone = '" + phoneNumber + "'";
        }
        if (!email.equals("%")) {
            query += " AND email = '" + email + "'";
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        String report = "";
        while (resultSet.next()) {
            report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getString("address") + "," + resultSet.getString("postal_code") + "," + resultSet.getString("city") + "," + resultSet.getString("phone") + "," + resultSet.getString("email") + "///";
        }
        return report;
    }

    public String clientReport(String name, String surname, LocalDate fromDate, LocalDate toDate, String phoneNumber, String email,
            String membershipCard) throws SQLException {
        String query = "SELECT * FROM client WHERE date_of_birth BETWEEN '" + fromDate + "' AND '" + toDate + "'";

        if (!name.equals("%")) {
            query += " AND first_name = '" + name + "'";
        }
        if (!surname.equals("%")) {
            query += " AND last_name = '" + surname + "'";
        }
        if (!email.equals("%")) {
            query += " AND email = '" + email + "'";
        }
        if (!phoneNumber.equals("%")) {
            query += " AND phone_number = '" + phoneNumber + "'";
        }
        if (!membershipCard.equals("%")) {
            query += " AND membership_card_id = '" + membershipCard + "'";
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        String report = "";
        while (resultSet.next()) {
            report += resultSet.getInt("id") + "," + resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getDate("date_of_birth") + "," + resultSet.getString("phone_number") + "," + resultSet.getString("email") + "," + resultSet.getInt("membership_card_id") + "///";
        }
        return report;
    }




    public String employeeReport(String name, String surname, LocalDate fromDateBirth, LocalDate toDateBirth,
            LocalDate fromDateEmployment, LocalDate toDateEmployment, String phoneNumber, String email,
            String position) throws SQLException{
        String query = "SELECT * FROM employee WHERE date_of_birth BETWEEN '" + fromDateBirth + "' AND '" + toDateBirth + "' AND date_of_employment BETWEEN '" + fromDateEmployment + "' AND '" + toDateEmployment + "'";

        if (!name.equals("%")) {
            query += " AND first_name = '" + name + "'";
        }
        if (!surname.equals("%")) {
            query += " AND last_name = '" + surname + "'";
        }
        if (!email.equals("%")) {
            query += " AND email = '" + email + "'";
        }
        if (!phoneNumber.equals("%")) {
            query += " AND phone_number = '" + phoneNumber + "'";
        }
        if (!position.equals("all")) {
            query += " AND position = '" + position + "'";
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String report = "";
            while (resultSet.next()) {
                report += resultSet.getInt("id") + "," + resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getDate("date_of_birth") + "," + resultSet.getDate("date_of_employment") + "," + resultSet.getString("phone_number") + "," + resultSet.getString("email") + "," + resultSet.getString("position") + "///";
            }
            return report;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String trainingReport(String name, LocalDate fromDate, LocalDate toDate, LocalTime fromHour, LocalTime toHour,
            int capacity, int room, int trainerId) throws SQLException {
        String query = "SELECT * FROM training WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "'" + " AND hour BETWEEN '" + fromHour + "' AND '" + toHour + "'";

        if (!name.equals("%")) {
            query += " AND name = '" + name + "'";
        }
        if (capacity != 0) {
            query += " AND capacity = '" + capacity + "'";
        }
        if (room != 0) {
            query += " AND room = '" + room + "'";
        }
        if (trainerId != 0) {
            query += " AND trainer_id = '" + trainerId + "'";
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String report = "";
            while (resultSet.next()) {
                report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getDate("date") + "," + resultSet.getTime("hour") + "," + resultSet.getInt("capacity") + "," + resultSet.getInt("room") + "," + resultSet.getInt("trainer_id") + "///";
            }
            return report;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadGym() throws SQLException{
        String query = "SELECT * FROM gym";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String report = "";
            while (resultSet.next()) {
                report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getString("address") + "," + resultSet.getString("postal_code") + "," + resultSet.getString("city") + "," + resultSet.getString("phone") + "," + resultSet.getString("email") + "///";
            }
            return report;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteGym(int gymID) throws SQLException{
        String query = "DELETE FROM gym WHERE id = " + gymID;
        try {
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            if (count > 0) {
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
