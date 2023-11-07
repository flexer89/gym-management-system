import java.net.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.*;
import java.util.concurrent.*;

import javax.swing.JOptionPane;

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
                    try {
                        int userID = sqlEngine.loginToAccount(username, password);
                        SendToClient.println("LOGIN:Success");
                        JOptionPane.showMessageDialog(null, "Login successful!", "Success", userID);
                    } catch (SQLException e) {
                        System.out.println("Error logging in: " + e.getMessage());
                        JOptionPane.showMessageDialog(null, "Invalid login credentials!", "Error", JOptionPane.ERROR_MESSAGE);
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
                    System.out.println(username + " " + password + " " + firstName + " " + lastName + " " + birthDate + " " + phoneNumber + " " + email);
                    try {
                        int userID = sqlEngine.registerAccount(username, password, firstName, lastName, birthDate, phoneNumber, email);
                        SendToClient.println("REGISTER:Success");
                        JOptionPane.showMessageDialog(null, "Account registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e) {
                        System.out.println("Error registering account: " + e.getMessage());
                        JOptionPane.showMessageDialog(null, "Error registering account!", "Error", JOptionPane.ERROR_MESSAGE);
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