package Handlers;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import utils.CustomLogger;
import utils.GenerateCard;
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
                CustomLogger.logError("Error while connecting to database: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
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
                    CustomLogger.logInfo("No user found with login " + login);
                    throw new SQLException("No user found with login " + login);
                }
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while getting user ID by login: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    CustomLogger.logInfo("Error while closing ResultSet: " + e.getMessage());
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
                CustomLogger.logInfo("No user found with id " + id + " in table " + table);
                throw new SQLException("No user found with id " + id + " in table " + table);
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while getting hash by ID: " + e.getMessage());
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
                CustomLogger.logInfo("No user found with id " + id + " in table " + table);
                throw new SQLException("No user found with id " + id + " in table " + table);
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while getting salt by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String loginToAccount(String username, String hash) throws SQLException {

        CustomLogger.logInfo("Trying to log as employee");
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
    
        CustomLogger.logInfo("Trying to log as client");
        String clientQuery = "SELECT client_id FROM client_credentials WHERE login = ? AND password = ?";
        try (PreparedStatement clientStatement = connection.prepareStatement(clientQuery)) {
            clientStatement.setString(1, username);
            clientStatement.setString(2, hash);
            try (ResultSet clientResultSet = clientStatement.executeQuery()) {
                if (clientResultSet.next()) {
                    int card_number = clientResultSet.getInt("client_id");
                    return "client" + "," + card_number;
                }
            }
        }
        CustomLogger.logInfo("Invalid login credentials");
        throw new SQLException("Invalid login credentials");
    }
    
    public boolean changePassword(String userID, String newPassword, String saltString, String userType) {   
        String table;
        if (userType.equals("client")) {
            table = "client_credentials";
        }
        else if (userType.equals("employee")) {
            table = "employee_credentials";
        }
        else {
            CustomLogger.logInfo("Invalid user type");
            throw new RuntimeException("Invalid user type");
        }
    
        String query = "UPDATE " + table + " SET password = ?, salt = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, saltString);
            pstmt.setString(3, userID);
            int count = pstmt.executeUpdate();
            if (count == 1) {
                return true;
            } else {
                CustomLogger.logInfo("Invalid change happened");
                throw new SQLException("Invalid change happened");
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while changing password: " + e.getMessage());
            throw new RuntimeException(e);
        }
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
                CustomLogger.logInfo("Invalid register credentials");
                throw new SQLException("Invalid register credentials");
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while registering account: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    CustomLogger.logInfo("Error while closing ResultSet: " + e.getMessage());
                }
            }
        }
    }

    public boolean canEnterTraining(String card_number, int roomID) throws SQLException {
        // get client id by card number
        // We dont have to check if membership card is valid, because it is checked before entering gym at all, not before entering training
        String query = "SELECT client_id FROM client JOIN membership_card ON client.id = membership_card.client_id WHERE card_number = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, card_number);
        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            int clientID = resultSet.getInt("client_id");

            // check if client has a reservation for this room
            query = "SELECT training.date, training.start_hour FROM reservation JOIN training ON training.id = reservation.training_id WHERE client_id = ? and room = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, clientID);
            pstmt.setInt(2, roomID);
            resultSet = pstmt.executeQuery();

            // check if client has a reservation for this room
            if (resultSet.next()) {
                LocalDate trainingDate = resultSet.getDate("date").toLocalDate();
                LocalTime trainingHour = resultSet.getTime("start_hour").toLocalTime();

                // check if training is today
                if (trainingDate.equals(LocalDate.now())) {
                    // check if 10 minutes before training
                    if (LocalTime.now().isBefore(trainingHour) && LocalTime.now().isAfter(trainingHour.minusMinutes(10))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canEnterGym(String card_number, int gymID) throws SQLException {
        String query = "SELECT expiration_date, all_gyms_access, original_gym_id, client_id, isCanceled FROM client join membership_card on client.id = membership_card.client_id WHERE membership_card.card_number = ? AND membership_card.isCanceled = 0";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, card_number);
        ResultSet resultSet = pstmt.executeQuery();
    
        if (resultSet.next()) {
            LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
            boolean allGymAccess = resultSet.getBoolean("all_gyms_access");
            int originalGymID = resultSet.getInt("original_gym_id");
            int userID = resultSet.getInt("client_id");
            boolean isCanceled = resultSet.getBoolean("isCanceled");

            // print all the data
            CustomLogger.logInfo("User membership card data: Expiration Date: " + expirationDate + " | All gyms access: " + allGymAccess + " | Original gym ID: " + originalGymID + " | User ID: " + userID + " | Is canceled: " + isCanceled);
    
            // Format time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime now = LocalTime.now();
            String formattedTime = formatter.format(now);
    
            if ((allGymAccess || originalGymID == gymID) && expirationDate.isAfter(LocalDate.now()) && !isCanceled) {
                // Check if client entered already
                query = "SELECT * FROM gym_visits WHERE client_id = ? AND gym_id = ? AND exit_date IS NULL";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, userID);
                pstmt.setInt(2, gymID);
                resultSet = pstmt.executeQuery();
    
                if (resultSet.next()) {
                    return false;
                }
    
                query = "INSERT INTO gym_visits (client_id, gym_id, entrance_date, entrance_time) VALUES (?, ?, ?, ?)";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, userID);
                pstmt.setInt(2, gymID);
                pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                pstmt.setString(4, formattedTime);
                pstmt.executeUpdate();
    
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    
    public boolean canExitGym(String userID, int gymID) throws SQLException {
        String query = "SELECT client_id FROM membership_card WHERE card_number = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, userID);
        ResultSet resultSet = pstmt.executeQuery();
    
        if (resultSet.next()) {
            int ID = resultSet.getInt("client_id");
            query = "SELECT * FROM gym_visits WHERE client_id = ? AND gym_id = ? AND exit_date IS NULL";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, ID);
            pstmt.setInt(2, gymID);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                // Format time
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime now = LocalTime.now();
                String formattedTime = formatter.format(now);
    
                query = "UPDATE gym_visits SET exit_date = ?, exit_time = ? WHERE client_id = ? AND gym_id = ? AND exit_date IS NULL";
                pstmt = connection.prepareStatement(query);
                pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                pstmt.setString(2, formattedTime);
                pstmt.setInt(3, ID);
                pstmt.setInt(4, gymID);
                pstmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    public boolean addGym(String name, String address, String postalCode, String city, String phone, String email) throws SQLException {
        String query = "INSERT INTO gym (name, address, postal_code, city, phone, email) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setString(2, address);
        pstmt.setString(3, postalCode);
        pstmt.setString(4, city);
        pstmt.setString(5, phone);
        pstmt.setString(6, email);
        int count = pstmt.executeUpdate();
        return count > 0;
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

    public String paymentReport(LocalDate fromDate, LocalDate toDate, int minimumPayment, int maximumPayment, String paymentMethod, int card_number)
        throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM payment WHERE payment_date BETWEEN ? AND ? AND amount BETWEEN ? AND ?");

        if (!paymentMethod.equals("all")) {
            query.append(" AND payment_method = ?");
        }

        if (card_number != 0) {
        query.append(" AND client_id = ?");
        }

        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        pstmt.setDate(1, java.sql.Date.valueOf(fromDate));
        pstmt.setDate(2, java.sql.Date.valueOf(toDate));
        pstmt.setInt(3, minimumPayment);
        pstmt.setInt(4, maximumPayment);

        int index = 5;
        if (!paymentMethod.equals("all")) {
            pstmt.setString(index++, paymentMethod);
        }

        if (card_number != 0) {
            pstmt.setInt(index, card_number);
        }

        ResultSet resultSet = pstmt.executeQuery();

        StringBuilder report = new StringBuilder();
        while (resultSet.next()) {
            report.append(resultSet.getInt("id")).append(",")
            .append(resultSet.getDate("payment_date")).append(",")
            .append(resultSet.getInt("amount")).append(",")
            .append(resultSet.getString("payment_method")).append(",")
            .append(resultSet.getInt("client_id")).append("///");
        }
        return report.toString();
    }

    public String gymReport(String name, String address, String postalCode, String city, String phoneNumber,
            String email) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM gym WHERE 1=1");
        /*
        The `1=1` is a common SQL trick. It's used here to simplify the construction of the SQL query.

        In SQL, `1=1` is always true for every row. So, it doesn't filter out any rows, and doesn't affect the results of the query.

        The reason it's used here is to avoid having to check if each subsequent condition is the first condition in the WHERE clause 
        (and thus should be prefixed with `WHERE` instead of `AND`). By starting with `WHERE 1=1`, you can safely append all subsequent 
        conditions with `AND`, knowing that there's always a valid condition before.
        */
        List<String> parameters = new ArrayList<>();

        if (!name.equals("%")) {
            query.append(" AND name = ?");
            parameters.add(name);
        }
        if (!address.equals("%")) {
            query.append(" AND address = ?");
            parameters.add(address);
        }
        if (!postalCode.equals("%")) {
            query.append(" AND postal_code = ?");
            parameters.add(postalCode);
        }
        if (!city.equals("%")) {
            query.append(" AND city = ?");
            parameters.add(city);
        }
        if (!phoneNumber.equals("%")) {
            query.append(" AND phone = ?");
            parameters.add(phoneNumber);
        }
        if (!email.equals("%")) {
            query.append(" AND email = ?");
            parameters.add(email);
        }

        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (int i = 0; i < parameters.size(); i++) {
            pstmt.setString(i + 1, parameters.get(i));
        }

        ResultSet resultSet = pstmt.executeQuery();

        StringBuilder report = new StringBuilder();
        while (resultSet.next()) {
            report.append(resultSet.getInt("id")).append(",")
                    .append(resultSet.getString("name")).append(",")
                    .append(resultSet.getString("address")).append(",")
                    .append(resultSet.getString("postal_code")).append(",")
                    .append(resultSet.getString("city")).append(",")
                    .append(resultSet.getString("phone")).append(",")
                    .append(resultSet.getString("email")).append("///");
        }
        return report.toString();
    }

    //todo: MEMBERSHIPCARD ID IS FUCKED
    public String clientReport(String name, String surname, LocalDate fromDate, LocalDate toDate, String phoneNumber, String email,
        String membershipCard) throws SQLException {
            StringBuilder query = new StringBuilder("SELECT * FROM client WHERE date_of_birth BETWEEN ? AND ?");

            List<Object> parameters = new ArrayList<>();
            parameters.add(fromDate);
            parameters.add(toDate);

            if (!name.equals("%")) {
                query.append(" AND first_name = ?");
                parameters.add(name);
            }
            if (!surname.equals("%")) {
                query.append(" AND last_name = ?");
                parameters.add(surname);
            }
            if (!email.equals("%")) {
                query.append(" AND email = ?");
                parameters.add(email);
            }
            if (!phoneNumber.equals("%")) {
                query.append(" AND phone_number = ?");
                parameters.add(phoneNumber);
            }
            if (!membershipCard.equals("%")) {
                query.append(" AND membership_card_id = ?");
                parameters.add(membershipCard);
            }

            PreparedStatement pstmt = connection.prepareStatement(query.toString());
            for (int i = 0; i < parameters.size(); i++) {
                if (parameters.get(i) instanceof LocalDate) {
                    pstmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) parameters.get(i)));
                } else {
                    pstmt.setString(i + 1, (String) parameters.get(i));
                }
            }

            ResultSet resultSet = pstmt.executeQuery();

            StringBuilder report = new StringBuilder();
            while (resultSet.next()) {
                report.append(resultSet.getInt("id")).append(",")
                        .append(resultSet.getString("first_name")).append(",")
                        .append(resultSet.getString("last_name")).append(",")
                        .append(resultSet.getDate("date_of_birth")).append(",")
                        .append(resultSet.getString("phone_number")).append(",")
                        .append(resultSet.getString("email")).append(",")
                        .append(resultSet.getInt("membership_card_id")).append("///");
            }
            return report.toString();
        }


    public String employeeReport(String name, String surname, LocalDate fromDateBirth, LocalDate toDateBirth,
                                LocalDate fromDateEmployment, LocalDate toDateEmployment, String phoneNumber, String email,
                                String position) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM employee WHERE date_of_birth BETWEEN ? AND ? AND date_of_employment BETWEEN ? AND ?");

        List<Object> parameters = new ArrayList<>();
        parameters.add(fromDateBirth);
        parameters.add(toDateBirth);
        parameters.add(fromDateEmployment);
        parameters.add(toDateEmployment);

        if (!name.equals("%")) {
            query.append(" AND first_name = ?");
            parameters.add(name);
        }
        if (!surname.equals("%")) {
            query.append(" AND last_name = ?");
            parameters.add(surname);
        }
        if (!email.equals("%")) {
            query.append(" AND email = ?");
            parameters.add(email);
        }
        if (!phoneNumber.equals("%")) {
            query.append(" AND phone_number = ?");
            parameters.add(phoneNumber);
        }
        if (!position.equals("all")) {
            query.append(" AND position = ?");
            parameters.add(position);
        }

        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i) instanceof LocalDate) {
                pstmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) parameters.get(i)));
            } else {
                pstmt.setString(i + 1, (String) parameters.get(i));
            }
        }

        ResultSet resultSet = pstmt.executeQuery();

        StringBuilder report = new StringBuilder();
        while (resultSet.next()) {
            report.append(resultSet.getInt("id")).append(",")
                    .append(resultSet.getString("first_name")).append(",")
                    .append(resultSet.getString("last_name")).append(",")
                    .append(resultSet.getDate("date_of_birth")).append(",")
                    .append(resultSet.getDate("date_of_employment")).append(",")
                    .append(resultSet.getString("phone_number")).append(",")
                    .append(resultSet.getString("email")).append(",")
                    .append(resultSet.getString("position")).append("///");
        }
        return report.toString();
    }


    public String trainingReport(String name, LocalDate fromDate, LocalDate toDate, LocalTime fromHour, LocalTime toHour,
                                int capacity, int room, int trainerId, int card_number) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM training WHERE date BETWEEN ? AND ? AND start_hour BETWEEN ? AND ?");

        List<Object> parameters = new ArrayList<>();
        parameters.add(fromDate);
        parameters.add(toDate);
        parameters.add(fromHour);
        parameters.add(toHour);

        if (!name.equals("%")) {
            query.append(" AND name = ?");
            parameters.add(name);
        }
        if (capacity != 0) {
            query.append(" AND capacity = ?");
            parameters.add(capacity);
        }
        if (room != 0) {
            query.append(" AND room = ?");
            parameters.add(room);
        }
        if (trainerId != 0) {
            query.append(" AND trainer_id = ?");
            parameters.add(trainerId);
        }
        if (card_number != 0) {
            query.append(" AND id IN (SELECT training_id FROM reservation WHERE client_id = ?)");
            parameters.add(card_number);
        }

        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i) instanceof LocalDate) {
                pstmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) parameters.get(i)));
            } else if (parameters.get(i) instanceof LocalTime) {
                pstmt.setTime(i + 1, java.sql.Time.valueOf((LocalTime) parameters.get(i)));
            } else {
                pstmt.setInt(i + 1, (Integer) parameters.get(i));
            }
        }

        ResultSet resultSet = pstmt.executeQuery();

        StringBuilder report = new StringBuilder();
        while (resultSet.next()) {
            report.append(resultSet.getInt("id")).append(",")
                    .append(resultSet.getString("name")).append(",")
                    .append(resultSet.getDate("date")).append(",")
                    .append(resultSet.getTime("start_hour")).append(",")
                    .append(resultSet.getInt("capacity")).append(",")
                    .append(resultSet.getInt("room")).append(",")
                    .append(resultSet.getInt("trainer_id")).append("///");
        }
        return report.toString();
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
            CustomLogger.logInfo("Error while loading gym: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean deleteGym(int gymID) throws SQLException {
        // Delete all reservations for this gym
        String query = "DELETE FROM reservation WHERE training_id IN (SELECT id FROM training WHERE gym_id = ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gymID);
            int count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from reservation");

            // Delete all trainings for this gym
            query = "DELETE FROM training WHERE gym_id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gymID);
            count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from training");

            // Delete all gym visits for this gym
            query = "DELETE FROM gym_visits WHERE gym_id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gymID);
            count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from gym_visits");

            // Delete the gym
            query = "DELETE FROM gym WHERE id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gymID);
            count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from gym");

            return true;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while deleting gym: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public boolean deleteEmployee(int employeeID) throws SQLException {
        try {
            // Delete reservations for trainings that are led by this employee
            String query = "DELETE FROM reservation WHERE training_id IN (SELECT id FROM training WHERE trainer_id = ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, employeeID);
            int count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from reservation");

            // Delete trainings that are led by this employee
            query = "DELETE FROM training WHERE trainer_id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, employeeID);
            count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from training");
    
            // Now delete the employee credentials
            query = "DELETE FROM employee_credentials WHERE employee_id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, employeeID);
            count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from employee_credentials");
    
            if (count > 0) {
                query = "DELETE FROM employee_card WHERE employee_id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, employeeID);
                count = pstmt.executeUpdate();
                CustomLogger.logInfo("Deleted " + count + " rows from employee_card");
    
                if (count > 0) {
                    query = "DELETE FROM employee WHERE id = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, employeeID);
                    count = pstmt.executeUpdate();
                    CustomLogger.logInfo("Deleted " + count + " rows from employee");
    
                    return true;
                }
                return false;
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while deleting employee: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String loadEmployees() throws SQLException{
        String query = "SELECT * FROM employee";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String report = "";
            while (resultSet.next()) {
                report += resultSet.getInt("id") + "," + resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getString("position") + "," + resultSet.getDate("date_of_birth") + "," + resultSet.getDate("date_of_employment") + "," + resultSet.getString("phone_number") + "," + resultSet.getString("email") + "///";
            }
            return report;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while loading employees: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getClient(int card_number) throws SQLException {
        String query = "SELECT * FROM client WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, card_number);
            ResultSet resultSet = pstmt.executeQuery();
    
            String report = "";
            if (resultSet.next()) {
                report += resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getDate("date_of_birth") + "," + resultSet.getString("phone_number") + "," + resultSet.getString("email");
            }
            return report;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while getting client: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getTrainer(int trainerID) throws SQLException {
        String query = "SELECT * FROM employee WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, trainerID);
            ResultSet resultSet = pstmt.executeQuery();
    
            String report = "";
            if (resultSet.next()) {
                report += resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getDate("date_of_birth") + "," +  resultSet.getString("phone_number") + "," + resultSet.getString("email") + "," + resultSet.getDate("date_of_employment");
            }
            return report;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while getting trainer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public boolean addTraining(String name, LocalDate date, LocalTime startHour, LocalTime endHour, int capacity, int room, int trainerID, int gymID) throws SQLException{
        // Check if room and trainer are available
        String query = "SELECT * FROM training WHERE " +
        "((room = ? AND gym_id = ? AND date = ? AND start_hour <= ? AND end_hour >= ?) " +
        "OR " +
        "(trainer_id = ? AND date = ? AND start_hour <= ? AND end_hour >= ? AND gym_id = ? AND room != ? " +
        "AND trainer_id NOT IN (SELECT trainer_id FROM training WHERE date = ? AND start_hour <= ? AND end_hour >= ? AND (gym_id != ? OR room IS NULL))))";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, room);
        pstmt.setInt(2, gymID);
        pstmt.setDate(3, java.sql.Date.valueOf(date));
        pstmt.setTime(4, java.sql.Time.valueOf(endHour));
        pstmt.setTime(5, java.sql.Time.valueOf(startHour));
        pstmt.setInt(6, trainerID);
        pstmt.setDate(7, java.sql.Date.valueOf(date));
        pstmt.setTime(8, java.sql.Time.valueOf(endHour));
        pstmt.setTime(9, java.sql.Time.valueOf(startHour));
        pstmt.setInt(10, gymID);
        pstmt.setInt(11, room);
        pstmt.setDate(12, java.sql.Date.valueOf(date));
        pstmt.setTime(13, java.sql.Time.valueOf(endHour));
        pstmt.setTime(14, java.sql.Time.valueOf(startHour));
        pstmt.setInt(15, gymID);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return false; 
        }

        // Check if the trainer is already occupied at the specified time
        query = "SELECT * FROM training WHERE trainer_id = ? AND date = ? AND start_hour <= ? AND end_hour >= ? AND gym_id != ?";
        pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, trainerID);
        pstmt.setDate(2, java.sql.Date.valueOf(date));
        pstmt.setTime(3, java.sql.Time.valueOf(endHour));
        pstmt.setTime(4, java.sql.Time.valueOf(startHour));
        pstmt.setInt(5, gymID);
        resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return false; 
        }

        // Add training
        query = "INSERT INTO training (name, date, start_hour, end_hour, capacity, room, trainer_id, gym_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setDate(2, java.sql.Date.valueOf(date));
        pstmt.setTime(3, java.sql.Time.valueOf(startHour));
        pstmt.setTime(4, java.sql.Time.valueOf(endHour));
        pstmt.setInt(5, capacity);
        pstmt.setInt(6, room);
        pstmt.setInt(7, trainerID);
        pstmt.setInt(8, gymID);
        int count = pstmt.executeUpdate();
        if (count > 0) {
            return true; 
        } else {
            return false;
        }
    }

    @SuppressWarnings("resource")
    public String loadTrainings(int userID) throws SQLException {
        String query = "SELECT original_gym_id, all_gyms_access FROM membership_card JOIN client ON client.id = membership_card.client_id WHERE client.id = ?";
        String report = "";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userID);
            ResultSet resultSet = pstmt.executeQuery();
    
            if (resultSet.next()) {
                int originalGymID = resultSet.getInt("original_gym_id");
                int allGymsAccess = resultSet.getInt("all_gyms_access");
                
                if (allGymsAccess == 1) {
                    query = "SELECT tr.id, tr.name, tr.date, tr.start_hour, tr.end_hour, tr.capacity, tr.room, CONCAT(e.first_name, ' ', e.last_name) AS trainer_name, CONCAT(g.address, ', ', g.city) AS gym_location, (SELECT COUNT(*) FROM reservation r WHERE r.training_id = tr.id) AS current_reservations FROM training tr JOIN employee e ON tr.trainer_id = e.id JOIN gym g ON tr.gym_id = g.id WHERE tr.date > CURDATE() ORDER BY tr.date, tr.start_hour";
                    pstmt = connection.prepareStatement(query);
                }
                else {
                    query = "SELECT tr.id, tr.name, tr.date, tr.start_hour, tr.end_hour, tr.capacity, tr.room, CONCAT(e.first_name, ' ', e.last_name) AS trainer_name, CONCAT(g.address, ', ', g.city) AS gym_location, (SELECT COUNT(*) FROM reservation r WHERE r.training_id = tr.id) AS current_reservations FROM training tr JOIN employee e ON tr.trainer_id = e.id JOIN gym g ON tr.gym_id = g.id WHERE tr.date > CURDATE() AND tr.gym_id = ? ORDER BY tr.date, tr.start_hour";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, originalGymID);
                }
                resultSet = pstmt.executeQuery();
                
                while (resultSet.next()) {
                    report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getDate("date") + "," + resultSet.getTime("start_hour") + "," + resultSet.getTime("end_hour") + "," + resultSet.getInt("current_reservations") + "/" + resultSet.getInt("capacity") + "," + resultSet.getInt("room") + "," + resultSet.getString("trainer_name") + "," + resultSet.getString("gym_location") + "///";
                }
            }
            return report;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while loading trainings: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }


    public boolean reserveTraining(int userID, int trainingID) throws SQLException {
        // check if client has a valid membership card
        String query = "SELECT * FROM membership_card JOIN client ON client.id = membership_card.client_id WHERE client.id = ? AND expiration_date > CURDATE() AND isCanceled = 0";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            // check if client has already reserved this training
            query = "SELECT * FROM reservation WHERE client_id = ? AND training_id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userID);
            pstmt.setInt(2, trainingID);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return false;
            }
    
            // get training capacity
            query = "SELECT capacity FROM training WHERE id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, trainingID);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int capacity = resultSet.getInt("capacity");
    
                // check if there is still space for this training
                query = "SELECT COUNT(*) AS current_reservations FROM reservation WHERE training_id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, trainingID);
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    int currentReservations = resultSet.getInt("current_reservations");
                    if (currentReservations < capacity) {
                        // reserve training
                        query = "INSERT INTO reservation (client_id, training_id) VALUES (?, ?)";
                        pstmt = connection.prepareStatement(query);
                        pstmt.setInt(1, userID);
                        pstmt.setInt(2, trainingID);
                        int count = pstmt.executeUpdate();
                        if (count > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public String timeSpentReport(LocalDate entranceFromDate, LocalDate entranceToDate, LocalTime entranceFromHour,
            LocalTime entranceToHour, LocalDate exitFromDate, LocalDate exitToDate, LocalTime exitFromHour,
            LocalTime exitToHour, int userID) throws SQLException {
        String query = "SELECT * FROM gym_visits WHERE client_id = ? AND entrance_date BETWEEN ? AND ? AND entrance_time BETWEEN ? AND ? AND exit_date BETWEEN ? AND ? AND exit_time BETWEEN ? AND ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, userID);
        pstmt.setDate(2, java.sql.Date.valueOf(entranceFromDate));
        pstmt.setDate(3, java.sql.Date.valueOf(entranceToDate));
        pstmt.setTime(4, java.sql.Time.valueOf(entranceFromHour));
        pstmt.setTime(5, java.sql.Time.valueOf(entranceToHour));
        pstmt.setDate(6, java.sql.Date.valueOf(exitFromDate));
        pstmt.setDate(7, java.sql.Date.valueOf(exitToDate));
        pstmt.setTime(8, java.sql.Time.valueOf(exitFromHour));
        pstmt.setTime(9, java.sql.Time.valueOf(exitToHour));
        ResultSet resultSet = pstmt.executeQuery();

        String report = "";

        while (resultSet.next()) {
            // Calculate time spent (hh:mm)
            LocalTime entranceTime = resultSet.getTime("entrance_time").toLocalTime();
            LocalTime exitTime = resultSet.getTime("exit_time").toLocalTime();
            int hours = exitTime.getHour() - entranceTime.getHour();
            int minutes = exitTime.getMinute() - entranceTime.getMinute();
            String timeSpent = hours + ":" + minutes;

            report += resultSet.getInt("id") + "," + resultSet.getDate("entrance_date") + "," + resultSet.getTime("entrance_time") + "," + resultSet.getDate("exit_date") + "," + resultSet.getTime("exit_time") + "," + timeSpent;

            // Get the gym name
            query = "SELECT name FROM gym WHERE id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, resultSet.getInt("gym_id"));
            ResultSet gymResultSet = pstmt.executeQuery();
            String gymName = "";
            if (gymResultSet.next()) {
                gymName = gymResultSet.getString("name");
            }

            report += "," + gymName + "///";
        }
        return report;
    }

    public String getMembershipCard(int userID) throws SQLException {
        String query = "SELECT * FROM membership_card WHERE client_id = ? AND expiration_date > CURDATE() AND isCanceled = 0";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet resultSet = pstmt.executeQuery();

        String report = "";
        if (resultSet.next()) {
            report += resultSet.getString("card_number") + "," + resultSet.getDate("expiration_date") + "," + resultSet.getString("type");
            
            if (resultSet.getInt("all_gyms_access") == 1) {
                report += ",Yes";
            }
            else {
                report += ",No";
            }

            // get gym name
            query = "SELECT name FROM gym WHERE id = ?";
            pstmt = connection.prepareStatement(query);
            int originalGymId = resultSet.getInt("original_gym_id");
            pstmt.setInt(1, originalGymId);
            ResultSet gymResultSet = pstmt.executeQuery();

            if (gymResultSet.next()) {
                String gymName = gymResultSet.getString("name");
                report += "," + gymName;
            }
        }
        return report;
    }

    public boolean payment(int userID, int amount, String paymentMethod) throws SQLException {
        String query = "INSERT INTO payment (payment_date, amount, payment_method, client_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, amount);
            pstmt.setString(3, paymentMethod);
            pstmt.setInt(4, userID);
            int count = pstmt.executeUpdate();
            CustomLogger.logInfo("Inserted " + count + " rows into payment");
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while inserting into payment: " + e.getMessage());
        }
        return false;
    }

    public String addMembershipCard(int userID, LocalDate expirationDate, String type, boolean allGymsAccess, int originalGymID) throws SQLException {
        // generate card number 
        boolean isUnique = false;
        String card_number = "";

        while (!isUnique) {
            card_number = GenerateCard.generateClientCardNumber();
            CustomLogger.logInfo("Generated card number: " + card_number);
            String query = "SELECT * FROM membership_card WHERE card_number = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, card_number);
            ResultSet resultSet = pstmt.executeQuery();
            if (!resultSet.next()) {
                isUnique = true;
            }
        }

        // if client already has a membership card, cancel it
        String query = "SELECT * FROM client WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            int membership_card_id = resultSet.getInt("membership_card_id");
            if (membership_card_id != 0) {
                query = "UPDATE membership_card SET isCanceled = 1 WHERE id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, membership_card_id);
                int count = pstmt.executeUpdate();
                if (count > 0) {
                    // Delete all reservations
                    query = "DELETE FROM reservation WHERE client_id = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, userID);
                    count = pstmt.executeUpdate();
                    
                    // Set membership_card_id to null in client
                    query = "UPDATE client SET membership_card_id = NULL WHERE id = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, userID);
                    count = pstmt.executeUpdate();
                }
            }
        }

        query = "INSERT INTO membership_card (card_number, expiration_date, type, all_gyms_access, original_gym_id, client_id) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, card_number);
            pstmt.setDate(2, java.sql.Date.valueOf(expirationDate));
            pstmt.setString(3, type);
            pstmt.setBoolean(4, allGymsAccess);
            pstmt.setInt(5, originalGymID);
            pstmt.setInt(6, userID);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                // get membership_card_id
                query = "SELECT id FROM membership_card WHERE card_number = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, card_number);
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    int membership_card_id = resultSet.getInt("id");
                    // set membership_card_id in client
                    query = "UPDATE client SET membership_card_id = ? WHERE id = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, membership_card_id);
                    pstmt.setInt(2, userID);
                    count = pstmt.executeUpdate();
                    if (count > 0) {
                        return card_number;
                    }
                }
            } else {
                return "--------";
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while inserting into membership_card: " + e.getMessage());
        }
        return "--------";
    }

    public boolean cancelSubscription(int userID) throws SQLException {
        String query = "UPDATE membership_card SET isCanceled = 1 WHERE client_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userID);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                // Delete all reservations
                query = "DELETE FROM reservation WHERE client_id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, userID);
                count = pstmt.executeUpdate();
                
                // Set membership_card_id to null in client
                query = "UPDATE client SET membership_card_id = NULL WHERE id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, userID);
                count = pstmt.executeUpdate();
                if (count > 0) {
                    return true;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while updating membership_card: " + e.getMessage());
        }
        return false;
    }

    public boolean updateGym(int gymID, String name, String address, String postalCode, String city, String phone,
            String email) throws SQLException{
        String query = "UPDATE gym SET name = ?, address = ?, postal_code = ?, city = ?, phone = ?, email = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, postalCode);
            pstmt.setString(4, city);
            pstmt.setString(5, phone);
            pstmt.setString(6, email);
            pstmt.setInt(7, gymID);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while updating gym: " + e.getMessage());
        }
        return false;
    }

    public boolean updateEmployee(int employeeID, String name, String surname, String position, LocalDate dateOfBirth,
            LocalDate dateOfEmployment, String phone, String email) throws SQLException {
        String query = "UPDATE employee SET first_name = ?, last_name = ?, position = ?, date_of_birth = ?, date_of_employment = ?, phone_number = ?, email = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, position);
            pstmt.setObject(4, dateOfBirth);
            pstmt.setObject(5, dateOfEmployment);
            pstmt.setString(6, phone);
            pstmt.setString(7, email);
            pstmt.setInt(8, employeeID);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while updating employee: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTraining(int trainingID, String name, LocalDate date, LocalTime startHour, LocalTime endHour,
            int capacity, int room, int trainerID, int gymID) throws SQLException {
        // Check if room is available (start and end hours should not be between start and end hours of another training in the same room)
        String query = "SELECT * FROM training WHERE room = ? AND gym_id = ? AND date = ? AND start_hour <= ? AND end_hour >= ? AND id != ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, room);
        pstmt.setInt(2, gymID);
        pstmt.setDate(3, java.sql.Date.valueOf(date));
        pstmt.setTime(4, java.sql.Time.valueOf(endHour));
        pstmt.setTime(5, java.sql.Time.valueOf(startHour));
        pstmt.setInt(6, trainingID);

        if (pstmt.executeQuery().next()) {
            return false;
        }

        // Check if trainer is available (start and end hours should not be between start and end hours of another training of the same trainer)
        query = "SELECT * FROM training WHERE trainer_id = ? AND date = ? AND start_hour <= ? AND end_hour >= ? AND id != ?";
        pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, trainerID);
        pstmt.setDate(2, java.sql.Date.valueOf(date));
        pstmt.setTime(3, java.sql.Time.valueOf(endHour));
        pstmt.setTime(4, java.sql.Time.valueOf(startHour));
        pstmt.setInt(5, trainingID);

        if (pstmt.executeQuery().next()) {
            return false;
        }

        // Update training
        query = "UPDATE training SET name = ?, date = ?, start_hour = ?, end_hour = ?, capacity = ?, room = ?, trainer_id = ?, gym_id = ? WHERE id = ?";
        pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setDate(2, java.sql.Date.valueOf(date));
        pstmt.setTime(3, java.sql.Time.valueOf(startHour));
        pstmt.setTime(4, java.sql.Time.valueOf(endHour));
        pstmt.setInt(5, capacity);
        pstmt.setInt(6, room);
        pstmt.setInt(7, trainerID);
        pstmt.setInt(8, gymID);
        pstmt.setInt(9, trainingID);
        int count = pstmt.executeUpdate();
        if (count > 0) {
            return true; 
        } else {
            return false;
        }
    }

    public String timeSpentEmployeeReport(LocalDate entranceFromDate, LocalDate entranceToDate,
            LocalTime entranceFromHour, LocalTime entranceToHour, LocalDate exitFromDate, LocalDate exitToDate,
            LocalTime exitFromHour, LocalTime exitToHour, int employeeID) throws SQLException {
        String query = "SELECT * FROM employee_work_time WHERE employee_id = ? AND entrance_date BETWEEN ? AND ? AND entrance_time BETWEEN ? AND ? AND exit_date BETWEEN ? AND ? AND exit_time BETWEEN ? AND ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, employeeID);
        pstmt.setDate(2, java.sql.Date.valueOf(entranceFromDate));
        pstmt.setDate(3, java.sql.Date.valueOf(entranceToDate));
        pstmt.setTime(4, java.sql.Time.valueOf(entranceFromHour));
        pstmt.setTime(5, java.sql.Time.valueOf(entranceToHour));
        pstmt.setDate(6, java.sql.Date.valueOf(exitFromDate));
        pstmt.setDate(7, java.sql.Date.valueOf(exitToDate));
        pstmt.setTime(8, java.sql.Time.valueOf(exitFromHour));
        pstmt.setTime(9, java.sql.Time.valueOf(exitToHour));
        ResultSet resultSet = pstmt.executeQuery();

        String report = "";
        
        // Calculate time spent (hh:mm)
        while (resultSet.next()) {
            LocalTime entranceTime = resultSet.getTime("entrance_time").toLocalTime();
            LocalTime exitTime = resultSet.getTime("exit_time").toLocalTime();
            int hours = exitTime.getHour() - entranceTime.getHour();
            int minutes = exitTime.getMinute() - entranceTime.getMinute();

            // count time spent (if minute is below 10 add 0 before)
            String timeSpent = hours + ":";
            if (minutes < 10) {
                timeSpent += "0" + minutes;
            } else {
                timeSpent += minutes;
            }

            report += resultSet.getInt("id") + "," + resultSet.getDate("entrance_date") + "," + resultSet.getTime("entrance_time") + "," + resultSet.getDate("exit_date") + "," + resultSet.getTime("exit_time") + "," + timeSpent + "///";
        }

        return report;
    }

    public String loadEmployeeTrainings(int employeeID) throws SQLException {
        String query = "SELECT * FROM training WHERE trainer_id = ? AND date >= CURDATE()";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, employeeID);
            ResultSet resultSet = pstmt.executeQuery();

            String report = "";
            while (resultSet.next()) {
                report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getDate("date") + "," + resultSet.getTime("start_hour") + "," + resultSet.getTime("end_hour") + "," + resultSet.getInt("capacity") + "," + resultSet.getInt("room") + "," + resultSet.getInt("gym_id");

                // Get the gym name
                query = "SELECT name FROM gym WHERE id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, resultSet.getInt("gym_id"));
                ResultSet gymResultSet = pstmt.executeQuery();
                String gymName = "";
                if (gymResultSet.next()) {
                    gymName = gymResultSet.getString("name");
                }

                report += "," + gymName + "///";
            }
            return report;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while loading employee trainings: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean deleteTraining(int trainingID) throws SQLException {
        // Delete all reservations for this training
        String query = "DELETE FROM reservation WHERE training_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, trainingID);
            int count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from reservation");

            // Delete the training
            query = "DELETE FROM training WHERE id = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, trainingID);
            count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from training");

            return true;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while deleting training: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String loadClientTrainings(int userID) throws SQLException {
        String query = "SELECT * FROM training WHERE id IN (SELECT training_id FROM reservation WHERE client_id = ?) AND date >= CURDATE()";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userID);
            ResultSet resultSet = pstmt.executeQuery();

            String report = "";
            while (resultSet.next()) {
                report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getDate("date") + "," + resultSet.getTime("start_hour") + "," + resultSet.getTime("end_hour") + "," + resultSet.getInt("room");

                // Get trainer name
                query = "SELECT first_name, last_name FROM employee WHERE id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, resultSet.getInt("trainer_id"));
                ResultSet trainerResultSet = pstmt.executeQuery();
                String trainerName = "";
                if (trainerResultSet.next()) {
                    trainerName = trainerResultSet.getString("first_name") + " " + trainerResultSet.getString("last_name");
                }

                report += "," + trainerName;

                // Get the gym location
                query = "SELECT address, city FROM gym WHERE id = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, resultSet.getInt("gym_id"));
                ResultSet gymResultSet = pstmt.executeQuery();
                String gymLocation = "";
                if (gymResultSet.next()) {
                    gymLocation = gymResultSet.getString("address") + ", " + gymResultSet.getString("city");
                }

                report += "," + gymLocation + "///";
            }
            return report;
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while loading client trainings: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean cancelReservation(int trainingID, int userID) throws SQLException{
        String query = "DELETE FROM reservation WHERE training_id = ? AND client_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, trainingID);
            pstmt.setInt(2, userID);
            int count = pstmt.executeUpdate();
            CustomLogger.logInfo("Deleted " + count + " rows from reservation");
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logInfo("Error while deleting reservation: " + e.getMessage());
        }
        return false;
    }
}
