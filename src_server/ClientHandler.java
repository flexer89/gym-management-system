import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.concurrent.*;

// Client handler class
class ClientHandler implements Callable<String> {
    private Socket clientSocket;
    private SQLEngine sqlEngine;
    private int menuSelection;

    public ClientHandler(Socket clientSocket, SQLEngine sqlEngine) {
        this.clientSocket = clientSocket;
        this.sqlEngine = sqlEngine;
        this.menuSelection = 0;
    }

    @Override
    public String call() {
        try {
            // Create input and output streams
            BufferedReader ReadFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter SendToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            sendPrintMessage(SendToClient, "Welcome to GMS!");
            sendInputMessage(SendToClient, "Please enter your username: ");
            String username = ReadFromClient.readLine();
            sendInputMessage(SendToClient, "Please enter your password: ");
            String password = ReadFromClient.readLine();
            System.out.println("Username: " + username + " Password: " + password);
            try {
                int userID = sqlEngine.loginToAccount(username, password);
                sendPrintMessage(SendToClient, "Successfully logged in as user ID: " + userID);
            } catch (SQLException e) {
                System.out.println("Error logging in: " + e.getMessage());
                sendPrintMessage(SendToClient, "Error logging in: " + e.getMessage());
                return "Error";
            }



            // Send menu options to client
            // out.println(Menu.getMenuOptions());
            // menuSelection = Integer.parseInt(in.readLine());

            // // TODO - prepare all features for client and organize them in a menu
            // // Handle menu selection
            // switch (menuSelection) {
            //     case Menu.OPTION_1:
            //         out.println("Login To GMS");
            //         break;
            //     case Menu.OPTION_2:
            //         out.println("Selected option 2");
            //         break;
            //     case Menu.OPTION_3:
            //         out.println("Selected option 3");
            //         break;
            //     case Menu.EXIT:
            //         out.println("Selected option exit");
            //         break;
            //     default:
            //         out.println("Invalid selection. Please try again.");
            //         break;
            // }

            // Close streams and socket
            ReadFromClient.close();
            SendToClient.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Task completed";
    }

    public void sendPrintMessage(PrintWriter SendToClient, String message) {
        SendToClient.println("PRINT:" + message);
    }
    
    public void sendInputMessage(PrintWriter SendToClient, String message) {
        SendToClient.println("SEND:" + message);
    }
}