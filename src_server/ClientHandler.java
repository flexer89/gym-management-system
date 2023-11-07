import java.net.*;
import java.sql.SQLException;
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
                    // Exit the loop
                    System.out.println(serverMessage.substring(5));
                    break;
                }
                else if (serverMessage.startsWith("LOGIN:")) {
                    // TODO move this to separate function
                    // Login to account
                    String[] loginInfo = serverMessage.substring(6).split(",");
                    String username = loginInfo[0];
                    String password = loginInfo[1];
                    SendToClient.println("LOGIN:Success");
                    // try {
                    //     int userID = sqlEngine.loginToAccount(username, password);
                    //     if (userID > 0 ) {
                    //         SendToClient.println("LOGIN:Success");
                    //     } else {
                    //         SendToClient.println("LOGIN:Failed");
                    //     }
                    // } catch (SQLException e) {
                    //     System.out.println("Error logging in: " + e.getMessage());
                    //     SendToClient.println("EXIT:Error logging in: " + e.getMessage());
                    // }
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