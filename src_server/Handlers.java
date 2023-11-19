import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;

import utils.Secure;

public class Handlers {

    private BufferedReader ReadFromClient;
    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;
    private Socket clientSocket;

    //create constructor
    public Handlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.ReadFromClient = ReadFromClient;
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
        this.clientSocket = clientSocket;
    }


    public void canEnterTraining(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");

        int userID = Integer.parseInt(loginInfo[0]);
        int roomID = Integer.parseInt(loginInfo[1]);
        System.out.println("User " + userID + " wants to enter room " + roomID);
        try {
            boolean canEnterTraining = sqlEngine.canEnterTraining(userID, roomID);
            SendToClient.println(canEnterTraining);
        } catch (SQLException e) {
            System.out.println("Error checking if user can enter training: " + e.getMessage());
        }
    }


    public void canExitGym(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");

        int userID = Integer.parseInt(loginInfo[0]);
        int gymID = Integer.parseInt(loginInfo[1]);
        System.out.println("User " + userID + " wants to exit gym " + gymID);
        try {
            boolean canExitGym = sqlEngine.canExitGym(userID, gymID);
            SendToClient.println(canExitGym);
        } catch (SQLException e) {
            System.out.println("Error checking if user can exit gym: " + e.getMessage());
        }
    }


    public void canEnterGym(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");

        int card_number = Integer.parseInt(loginInfo[0]);
        int gymID = Integer.parseInt(loginInfo[1]);
        System.out.println("User with card " + card_number + " wants to enter gym " + gymID);
        try {
            boolean canEnterGym = sqlEngine.canEnterGym(card_number, gymID);
            SendToClient.println(canEnterGym);
        } catch (SQLException e) {
            System.out.println("Error checking if user can enter gym: " + e.getMessage());
        }
    }

    public void register(String serverMessage)
    {
            // TODO move this to separate function
            // Register new account
            String[] registerInfo = serverMessage.split(",");
            String username = registerInfo[0];
            String password = registerInfo[1];
            String firstName = registerInfo[2];
            String lastName = registerInfo[3];
            LocalDate birthDate = LocalDate.parse(registerInfo[4]);
            String phoneNumber = registerInfo[5];
            String email = registerInfo[6];

            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            System.out.println("Generated salt: " + salt);
            String saltString = salt.toString();
            password = Secure.hashWithSalt(password, saltString);


            System.out.println("Passwd: " + password + " Salt: " + salt);

            try {
                int userID = sqlEngine.registerAccount(username, password, saltString, firstName, lastName, birthDate, phoneNumber, email);
                System.out.println("User " + userID + " registered");
                SendToClient.println(userID);
            } catch (SQLException e) {
                System.out.println("Error registering account: " + e.getMessage());
            }
    }


    public void login(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");
        String username = loginInfo[0];
        String password = loginInfo[1];

        String fetchedData = sqlEngine.getIDbyLogin(username);
        System.out.println("Fetched data: " + fetchedData);
        
        String[] parts = fetchedData.split(",");
        String ID = parts[0];
        String table = parts[1];
        
        System.out.println("Testing creds for table: " + table);
        String storedHash = sqlEngine.getHashByID(Integer.parseInt(ID), table);
        String salt = sqlEngine.getSaltByID(Integer.parseInt(ID), table);
        System.out.println("ID: " + ID + "   Hash: " + storedHash + " Salt: " + salt);
        String hashedInput = Secure.hashWithSalt(password, salt);
        System.out.println("Hashed input: " + hashedInput);
        
        if (storedHash.equals(hashedInput)) {
            System.out.println("Hashes match");
            try {
                String data = sqlEngine.loginToAccount(username, hashedInput);
                String type = data.split(",")[0];
                int userID = Integer.parseInt(data.split(",")[1]);
        
                System.out.println(type + " " + userID + " logged in");
                SendToClient.println(userID);
                SendToClient.println(type);
            } catch (SQLException e) {
                System.out.println("Error logging in: " + e.getMessage());
                SendToClient.println(-1);
                SendToClient.println("ERROR");
            }
        } else {
            System.out.println("Hashes don't match");
        }
    }

    public void addGym(String serverMessage)
    {
        String[] gymInfo = serverMessage.split(",");
        String name = gymInfo[0];
        String address = gymInfo[1];
        String postalCode = gymInfo[2];
        String city = gymInfo[3];
        String phone = gymInfo[4];
        String email = gymInfo[5];
        try {
            boolean ifAdded = sqlEngine.addGym(name, address, postalCode, city, phone, email);
            if (ifAdded) {
                System.out.println("Gym " + name + " added");
                SendToClient.println("True");
            } else {
                System.out.println("Gym " + name + " wasn't added");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            System.out.println("Error adding gym: " + e.getMessage());
        }
    }

    public void print(String serverMessage)
    {
        System.out.println(serverMessage);
    }

    public void send()
    {
        String clientMessage = System.console().readLine();
        SendToClient.println(clientMessage);
    }

    public void exit()
    {
        System.out.println("Exiting");
    }


    public void addEmployee(String data){
        String[] employeeInfo = data.split(",");
        String name = employeeInfo[0];
        String surname = employeeInfo[1];
        String position = employeeInfo[2];
        LocalDate dateOfBirth = LocalDate.parse(employeeInfo[3]);
        LocalDate dateOfEmployment = LocalDate.parse(employeeInfo[4]);
        String phone = employeeInfo[5];
        String email = employeeInfo[6];
        String login = employeeInfo[7];
        
        try {
            boolean ifAdded = sqlEngine.addEmployee(name, surname, position, dateOfBirth,dateOfEmployment, phone, email, login);
            if (ifAdded) {
                System.out.println("Employee " + name + " added");
                SendToClient.println("True");
            } else {
                System.out.println("Employee " + name + " wasn't added");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }

    }

    public void paymentReport(String data) {
        String[] reportData = data.split(",");
        LocalDate fromDate = LocalDate.parse(reportData[0]);
        LocalDate toDate = LocalDate.parse(reportData[1]);
        int minimumPayment = Integer.parseInt(reportData[2]);
        int maximumPayment = Integer.parseInt(reportData[3]);
        String paymentMethod = reportData[4];

        // Generate payment report
        System.out.println("Generating payment report");
        System.out.println("From date: " + fromDate + " To date: " + toDate + " Minimum payment: " + minimumPayment + " Maximum payment: " + maximumPayment + " Payment method: " + paymentMethod);
        try {
            String report = sqlEngine.paymentReport(fromDate, toDate, minimumPayment, maximumPayment, paymentMethod);
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error generating payment report: " + e.getMessage());
        }
    }

    public void gymReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        String address = reportData[1];
        String postalCode = reportData[2];
        String city = reportData[3];
        String phoneNumber = reportData[4];
        String email = reportData[5];

        // Generate gym report
        System.out.println("Generating gym report");
        System.out.println("Name: " + name + " Address: " + address + " Postal code: " + postalCode + " City: " + city + " Phone number: " + phoneNumber + " Email: " + email);
        try {
            String report = sqlEngine.gymReport(name, address, postalCode, city, phoneNumber, email);
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error generating gym report: " + e.getMessage());
        }
    }

    public void clientReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        String surname = reportData[1];
        LocalDate fromDate = LocalDate.parse(reportData[2]);
        LocalDate toDate = LocalDate.parse(reportData[3]);
        String phoneNumber = reportData[4];
        String email = reportData[5];
        String membershipCard = reportData[6];

        // Generate client report
        System.out.println("Generating client report");
        System.out.println("Name: " + name + " Surname: " + surname + "From date: " + fromDate + " To date: " + toDate + " Phone number: " + phoneNumber + " Email: " + email + " Membership card: " + membershipCard);
        try {
            String report = sqlEngine.clientReport(name, surname, fromDate, toDate, phoneNumber, email, membershipCard);
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error generating client report: " + e.getMessage());
        }
    }


    public void employeeReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        String surname = reportData[1];
        LocalDate fromDateBirth = LocalDate.parse(reportData[2]);
        LocalDate toDateBirth = LocalDate.parse(reportData[3]);
        LocalDate fromDateEmployment = LocalDate.parse(reportData[4]);
        LocalDate toDateEmployment = LocalDate.parse(reportData[5]);
        String phoneNumber = reportData[6];
        String email = reportData[7];
        String position = reportData[8];

        // Generate employee report
        System.out.println("Generating employee report");
        System.out.println("Name: " + name + " Surname: " + surname + " From date birth: " + fromDateBirth + " To date birth: " + toDateBirth + " From date employment: " + fromDateEmployment + " To date employment: " + toDateEmployment + " Phone number: " + phoneNumber + " Email: " + email + " Position: " + position);
        try {
            String report = sqlEngine.employeeReport(name, surname, fromDateBirth, toDateBirth, fromDateEmployment, toDateEmployment, phoneNumber, email, position);
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error generating employee report: " + e.getMessage());
        }
    }
}