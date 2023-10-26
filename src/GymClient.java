import java.net.*;
import java.io.*;

public class GymClient {
    public static void main(String[] args) throws IOException {
        // Create client socket
        Socket clientSocket = new Socket("localhost", 5000);
        System.out.println("Connected to server");

        // Create input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Send request to server
        out.println("Client request");

        // Receive response from server
        String response = in.readLine();
        System.out.println("Server response: " + response);

        // Close streams and socket
        in.close();
        out.close();
        clientSocket.close();
    }
}