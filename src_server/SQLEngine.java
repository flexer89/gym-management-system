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

    //TODO: TEST IT FULLY
    public boolean canEnterTraining(int clientID, int roomID) throws SQLException {
        String query = "SELECT training.date, training.hour FROM reservation JOIN training ON training.id = reservation.training_id WHERE client_id = ? and room = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, clientID);
        pstmt.setInt(2, roomID);
        ResultSet resultSet = pstmt.executeQuery();
    
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
            }
        }
        return false;
    }

    public boolean canEnterGym(int card_number, int gymID) throws SQLException {
        String query = "SELECT expiration_date, all_gyms_access, original_gym_id, client_id FROM client join membership_card on client.id = membership_card.client_id WHERE membership_card.card_number = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, card_number);
        ResultSet resultSet = pstmt.executeQuery();
    
        if (resultSet.next()) {
            LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
            boolean allGymAccess = resultSet.getBoolean("all_gyms_access");
            int originalGymID = resultSet.getInt("original_gym_id");
            int clientID = resultSet.getInt("client_id");
    
            // Format time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime now = LocalTime.now();
            String formattedTime = formatter.format(now);
    
            if ((allGymAccess || originalGymID == gymID) && expirationDate.isAfter(LocalDate.now())) {
                // Check if client entered already
                query = "SELECT * FROM gym_visits WHERE client_id = ? AND gym_id = ? AND exit_date IS NULL";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, clientID);
                pstmt.setInt(2, gymID);
                resultSet = pstmt.executeQuery();
    
                if (resultSet.next()) {
                    return false;
                }
    
                query = "INSERT INTO gym_visits (client_id, gym_id, entrance_date, entrance_time) VALUES (?, ?, ?, ?)";
                pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, clientID);
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
    
    public boolean canExitGym(int card_number, int gymID) throws SQLException {
        String query = "SELECT client_id FROM membership_card WHERE card_number = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, card_number);
        ResultSet resultSet = pstmt.executeQuery();
    
        if (resultSet.next()) {
            int clientID = resultSet.getInt("client_id");
            query = "SELECT * FROM gym_visits WHERE client_id = ? AND gym_id = ? AND exit_date IS NULL";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, clientID);
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
                pstmt.setInt(3, clientID);
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

    public String paymentReport(LocalDate fromDate, LocalDate toDate, int minimumPayment, int maximumPayment, String paymentMethod, int clientID)
        throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM payment WHERE payment_date BETWEEN ? AND ? AND amount BETWEEN ? AND ?");

        if (!paymentMethod.equals("all")) {
            query.append(" AND payment_method = ?");
        }

        if (clientID != 0) {
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

        if (clientID != 0) {
            pstmt.setInt(index, clientID);
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
                                int capacity, int room, int trainerId, int clientID) throws SQLException {
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
        if (clientID != 0) {
            query.append(" AND id IN (SELECT training_id FROM reservation WHERE client_id = ?)");
            parameters.add(clientID);
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
            throw new RuntimeException(e);
        }
    }

    //todo: REWRITE IT
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
    
    //TODO: FIX IT AND REWRITE
    public boolean deleteEmployee(int employeeID) throws SQLException{ 
        String query = "DELETE FROM employee_credentials WHERE employee_id = " + employeeID;
        try {
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            if (count > 0) {
                query = "DELETE FROM employee_card WHERE employee_id = " + employeeID;
                count = statement.executeUpdate(query);
                if (count > 0) {
                    query = "DELETE FROM employee WHERE id = " + employeeID;
                    statement.executeUpdate(query);
                    return true;
                }
                return false;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadEmployee() throws SQLException{
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
            throw new RuntimeException(e);
        }
    }

    public String getClient(int clientID) throws SQLException{
        String query = "SELECT * FROM client WHERE id = " + clientID;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String report = "";
            if (resultSet.next()) {
                report += resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getDate("date_of_birth") + "," + resultSet.getString("phone_number") + "," + resultSet.getString("email");
            }
            return report;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTrainer(int trainerID) throws SQLException {
        String query = "SELECT * FROM employee WHERE id = " + trainerID;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String report = "";
            if (resultSet.next()) {
                report += resultSet.getString("first_name") + "," + resultSet.getString("last_name") + "," + resultSet.getDate("date_of_birth") + "," +  resultSet.getString("phone_number") + "," + resultSet.getString("email") + "," + resultSet.getDate("date_of_employment");
            }
            return report;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //TODO:  REWRITE
    public boolean addTraining(String name, LocalDate date, LocalTime startHour, LocalTime endHour, int capacity, int room, int trainerID, int gymID) throws SQLException{
        // Check if room and trainer are available
        String query = "SELECT * FROM training WHERE " +
                    "((room = '" + room + "' AND gym_id = '" + gymID + "' AND date = '" + date + "' AND start_hour <= '" + endHour + "' AND end_hour >= '" + startHour + "') " +
                    "OR " +
                    "(trainer_id = '" + trainerID + "' AND date = '" + date + "' AND start_hour <= '" + endHour + "' AND end_hour >= '" + startHour + "' AND gym_id = '" + gymID + "' AND room != '" + room + "' " +
                    "AND trainer_id NOT IN (SELECT trainer_id FROM training WHERE date = '" + date + "' AND start_hour <= '" + endHour + "' AND end_hour >= '" + startHour + "' AND (gym_id != '" + gymID + "' OR room IS NULL))))";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return false; 
        }

        // Check if the trainer is already occupied at the specified time
        query = "SELECT * FROM training WHERE trainer_id = '" + trainerID + "' AND date = '" + date + "' AND start_hour <= '" + endHour + "' AND end_hour >= '" + startHour + "' AND gym_id != '" + gymID + "'";
        resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return false; 
        }

        // Add training
        query = "INSERT INTO training (name, date, start_hour, end_hour, capacity, room, trainer_id, gym_id) VALUES ('" + name + "', '" + date + "', '" + startHour + "', '" + endHour + "', " + capacity + ", " + room + ", " + trainerID + ", " + gymID + ")";
        int count = statement.executeUpdate(query);
        if (count > 0) {
            return true; 
        } else {
            return false;
        }
    }
        //TODO:  REWRITE
    public String loadTrainings(int userID) throws SQLException {
        String query = "SELECT original_gym_id, all_gyms_access FROM membership_card JOIN client ON client.id = membership_card.client_id WHERE client.id = " + userID;
        String report = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int originalGymID = resultSet.getInt("original_gym_id");
                int allGymsAccess = resultSet.getInt("all_gyms_access");
                
                if (allGymsAccess == 1) {
                    query = "SELECT tr.id, tr.name, tr.date, tr.start_hour, tr.end_hour, tr.capacity, tr.room, CONCAT(e.first_name, ' ', e.last_name) AS trainer_name, CONCAT(g.address, ', ', g.city) AS gym_location, (SELECT COUNT(*) FROM reservation r WHERE r.training_id = tr.id) AS current_reservations FROM training tr JOIN employee e ON tr.trainer_id = e.id JOIN gym g ON tr.gym_id = g.id WHERE tr.date > CURDATE() ORDER BY tr.date, tr.start_hour";
                }
                else {
                    query = "SELECT tr.id, tr.name, tr.date, tr.start_hour, tr.end_hour, tr.capacity, tr.room, CONCAT(e.first_name, ' ', e.last_name) AS trainer_name, CONCAT(g.address, ', ', g.city) AS gym_location, (SELECT COUNT(*) FROM reservation r WHERE r.training_id = tr.id) AS current_reservations FROM training tr JOIN employee e ON tr.trainer_id = e.id JOIN gym g ON tr.gym_id = g.id WHERE tr.date > CURDATE() AND tr.gym_id = " + originalGymID + " ORDER BY tr.date, tr.start_hour";
                }
                resultSet = statement.executeQuery(query);
                
                while (resultSet.next()) {
                    report += resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getDate("date") + "," + resultSet.getTime("start_hour") + "," + resultSet.getTime("end_hour") + "," + resultSet.getInt("current_reservations") + "/" + resultSet.getInt("capacity") + "," + resultSet.getInt("room") + "," + resultSet.getString("trainer_name") + "," + resultSet.getString("gym_location") + "///";
                }
            }
            return report;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
       
    }
    //TODO:  REWRITE
    public boolean reserveTraining(int userID, int trainingID) throws SQLException {
        // check if client has a valid membership card
        String query = "SELECT * FROM membership_card JOIN client ON client.id = membership_card.client_id WHERE client.id = " + userID + " AND expiration_date > CURDATE()";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            // check if client has already reserved this training
            query = "SELECT * FROM reservation WHERE client_id = " + userID + " AND training_id = " + trainingID;
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return false;
            }

            // check if there is still space for this training
            query = "SELECT COUNT(*) AS current_reservations, capacity FROM reservation JOIN training ON training.id = reservation.training_id WHERE training_id = " + trainingID;
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int currentReservations = resultSet.getInt("current_reservations");
                int capacity = resultSet.getInt("capacity");
                if (currentReservations < capacity) {
                    query = "INSERT INTO reservation (client_id, training_id) VALUES (" + userID + ", " + trainingID + ")";
                    int count = statement.executeUpdate(query);
                    if (count > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //TODO:  REWRITE
    public String timeSpentReport(LocalDate entranceFromDate, LocalDate entranceToDate, LocalTime entranceFromHour,
            LocalTime entranceToHour, LocalDate exitFromDate, LocalDate exitToDate, LocalTime exitFromHour,
            LocalTime exitToHour, int userID) throws SQLException {
        String query = "SELECT * FROM gym_visits WHERE client_id = " + userID + " AND entrance_date BETWEEN '" + entranceFromDate + "' AND '" + entranceToDate + "' AND entrance_time BETWEEN '" + entranceFromHour + "' AND '" + entranceToHour + "' AND exit_date BETWEEN '" + exitFromDate + "' AND '" + exitToDate + "' AND exit_time BETWEEN '" + exitFromHour + "' AND '" + exitToHour + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        String report = "";

        while (resultSet.next()) {
            // Calculate time spent (hh:mm)
            LocalTime entranceTime = resultSet.getTime("entrance_time").toLocalTime();
            LocalTime exitTime = resultSet.getTime("exit_time").toLocalTime();
            int hours = exitTime.getHour() - entranceTime.getHour();
            int minutes = exitTime.getMinute() - entranceTime.getMinute();
            String timeSpent = hours + ":" + minutes;

            report += resultSet.getInt("id") + "," + resultSet.getDate("entrance_date") + "," + resultSet.getTime("entrance_time") + "," + resultSet.getDate("exit_date") + "," + resultSet.getTime("exit_time") + "," + timeSpent + "///";
        }
        return report;
    }
}
