import java.io.*;
import java.net.*;

import gui.exit_entrance.ExitEntranceWindow;
import utils.Color;
import utils.CustomLogger;
import utils.Message;

public class ExitEntranceManager {
    public static void main(String[] args) throws IOException {
        // Create client socket
        Socket clientSocket = new Socket("localhost", 5000);
        CustomLogger.logInfo("Exit entrance manager connected to server");

        // Create input and output streams
        BufferedReader ReadFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter SendToServer = new PrintWriter(clientSocket.getOutputStream(), true);

        // Create message object
        Message message = new Message();

        // Create the training entrance window
        int gymID = 1;
        new ExitEntranceWindow(message, ReadFromServer, SendToServer, clientSocket, gymID);
    }
}
