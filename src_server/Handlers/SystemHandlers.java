package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import utils.CustomLogger;

public class SystemHandlers {

    private PrintWriter SendToClient;

    //create constructor
    public SystemHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket) {
        this.SendToClient = SendToClient;
    }

    public void print(String serverMessage)
    {
        System.out.println(serverMessage);
        CustomLogger.logInfo("Printed message to server: " + serverMessage);
    }

    public void send()
    {
        String clientMessage = System.console().readLine();
        SendToClient.println(clientMessage);
        CustomLogger.logInfo("Sent message to client: " + clientMessage);
    }

    public void exit()
    {
        CustomLogger.logInfo("Exiting server");
        System.exit(0);
    }

}
