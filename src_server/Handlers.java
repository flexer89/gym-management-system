import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;

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
}