import java.net.*;
import java.io.*;

public class GymClient {
    public static void main(String[] args) throws IOException {
        // Create client socket
        Socket clientSocket = new Socket("localhost", 5000);
        System.out.println(Color.ColorString("Connected to server", Color.ANSI_GREEN));
        String response = "";

        // Create input and output streams
        BufferedReader ReadFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter SendToServer = new PrintWriter(clientSocket.getOutputStream(), true);


        // System.out.println(ReadFromServer.readLine());
        // System.out.println(ReadFromServer.readLine());
        // //read username
        // String username = System.console().readLine();
        // SendToServer.println(username);
        // System.out.println(ReadFromServer.readLine());
        // String password = System.console().readLine();
        // SendToServer.println(password);

        while (true) {
            String serverMessage = ReadFromServer.readLine();
            // TODO: someshit to handle when server dont send anything
            // if (serverMessage == null || serverMessage.isEmpty()) {
            //     System.out.println("Waiting for data...");
            //     continue;
            // }
            if (serverMessage.startsWith("PRINT:")) {
                // Print the message, excluding the "PRINT:" prefix
                System.out.println(serverMessage.substring(6));
            } else if (serverMessage.startsWith("SEND:")) {
                // Send data to the server
                System.out.println(serverMessage.substring(5));
                String clientMessage = System.console().readLine();
                SendToServer.println(clientMessage);
            } else if (serverMessage.startsWith("EXIT:")) {
                // Exit the loop
                System.out.println(serverMessage.substring(5));
                break;
            }
        }
        
        // // TODO: need to create nice way to get text from buffer
        // for (int i = 0; i < 4; i++) {
        //     // Receive response from server
        //     response = in.readLine();
        //     System.out.println(response);
        // }

        // // Send request to server
        // int menuSelection = Integer.parseInt(System.console().readLine("Enter your selection: "));

        // // Send the menu selection to the server
        // out.println(menuSelection);

        // // Receive response from server
        // response = in.readLine();
        // System.out.println("Server response: " + response);

        // Close streams and socket
        ReadFromServer.close();
        SendToServer.close();
        clientSocket.close();
    }
}