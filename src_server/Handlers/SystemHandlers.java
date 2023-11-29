package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SystemHandlers {

    private PrintWriter SendToClient;

    //create constructor
    public SystemHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket) {
        this.SendToClient = SendToClient;
    }
    

    public void print(String serverMessage)
    {
        System.out.println(serverMessage);
    }

    public void send()
    {
        String clientMessage = System.console().readLine();
        SendToClient.println(clientMessage);
    }

    public void exit()
    {
        System.out.println("Exiting");
    }

}
