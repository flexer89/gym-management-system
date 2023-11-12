import java.net.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.*;
import java.util.concurrent.*;

// Client handler class
class ClientHandler implements Callable<String> {
    private Socket clientSocket;
    private SQLEngine sqlEngine;

    public ClientHandler(Socket clientSocket, SQLEngine sqlEngine) {
        this.clientSocket = clientSocket;
        this.sqlEngine = sqlEngine;
    }

    @Override
    public String call() {
        try {
            // Create input and output streams
            BufferedReader ReadFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter SendToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            String serverMessage;
            while (true) {
                serverMessage = ReadFromClient.readLine();
                if (serverMessage.startsWith("PRINT:")) {
                    // Print the message, excluding the "PRINT:" prefix
                    System.out.println(serverMessage.substring(6));
                } else if (serverMessage.startsWith("SEND:")) {
                    // Send data to the server
                    System.out.println(serverMessage.substring(5));
                    String clientMessage = System.console().readLine();
                    SendToClient.println(clientMessage);
                } else if (serverMessage.startsWith("EXIT:")) {
                    System.out.println(serverMessage.substring(5));
                    break;
                } else if (serverMessage.startsWith("CAN_ENTER:")){
                    System.out.println(serverMessage.substring(10));
                    String[] loginInfo = serverMessage.substring(10).split(",");

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
                else if (serverMessage.startsWith("LOGIN:")) {
                    // TODO move this to separate function
                    String[] loginInfo = serverMessage.substring(6).split(",");
                    String username = loginInfo[0];
                    String password = loginInfo[1];
                    try {
                        String data = sqlEngine.loginToAccount(username, password);
                        String type = data.split(",")[0];
                        System.out.println(data);
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
                else if (serverMessage.startsWith("REGISTER:"))
                {
                    // TODO move this to separate function
                    // Register new account
                    String[] registerInfo = serverMessage.substring(9).split(",");
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
            }

            // Close streams and socket
            ReadFromClient.close();
            SendToClient.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Task completed";
    }

}