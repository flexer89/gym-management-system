import java.net.*;
import java.io.*;

public class GymClient {
    public static void main(String[] args) throws IOException {
        // Create client socket
        Socket clientSocket = new Socket("localhost", 5000);
        System.out.println(Color.ColorString("Connected to server", Color.ANSI_GREEN));
        String response = "";

        // Create input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        // TODO: need to create nice way to get text from buffer
        for (int i = 0; i < 4; i++) {
            // Receive response from server
            response = in.readLine();
            System.out.println(response);
        }

        // Send request to server
        int menuSelection = Integer.parseInt(System.console().readLine("Enter your selection: "));

        // Send the menu selection to the server
        out.println(menuSelection);

        // Receive response from server
        response = in.readLine();
        System.out.println("Server response: " + response);

        // Close streams and socket
        in.close();
        out.close();
        clientSocket.close();
    }
}