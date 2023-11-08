import java.io.*;
import java.net.*;

import gui.login.LoginRegisterWindow;
import utils.Color;
import utils.Message;

public class GymClient {
    public static void main(String[] args) throws IOException {
        // Create client socket
        Socket clientSocket = new Socket("localhost", 5000);
        System.out.println(Color.ColorString("Connected to server", Color.ANSI_GREEN));

        // Create input and output streams
        BufferedReader ReadFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter SendToServer = new PrintWriter(clientSocket.getOutputStream(), true);

        // Create message object
        Message message = new Message();

        // Create the login/register window
        LoginRegisterWindow loginRegisterWindow = new LoginRegisterWindow(message, ReadFromServer, SendToServer, clientSocket);
    }
}
