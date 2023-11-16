import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Handler;

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
            try {
                int userID = sqlEngine.registerAccount(username, password, firstName, lastName, birthDate, phoneNumber, email);
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
        try {
            String data = sqlEngine.loginToAccount(username, password);
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
}