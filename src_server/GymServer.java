import java.net.*;
import java.io.*;
import java.util.concurrent.*;

import Handlers.SQLEngine;
import utils.Color;
import utils.CustomLogger;

public class GymServer {
    // Server properties
    private static final int SERVER_PORT = 5000;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    // DB properties
    private static final String host = "localhost";
    private static final int DBPort = 3306;
    private static final String DBName = "GMS";
    private static final String username = "root";
    private static final String password = "root";
    private static SQLEngine sqlEngine;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {

        // Create server socket
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            CustomLogger.logInfo("Server started on port " + SERVER_PORT);
        } catch (IOException e) {
            CustomLogger.logError("Could not start server: " + e.getMessage());
            System.exit(-1);
        }

        try {
            // Create DB engine
            sqlEngine = new SQLEngine(host, DBPort, DBName, username, password);

            // Connect to DB
            sqlEngine.getConnection();
            CustomLogger.logInfo("Connected to DB");

        } 
        catch (Exception e) 
        {
            CustomLogger.logError("Could not connect to DB: " + e.getMessage());
            System.exit(-1);
        }

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Log the server state
            String message = "  Number of active tasks: " + ((ThreadPoolExecutor) executorService).getActiveCount();
            System.out.println(Color.ColorString("Server state:", Color.ANSI_GREEN));
            System.out.println(Color.ColorString(message, Color.ANSI_YELLOW));

            // log the server state
            CustomLogger.logInfo("Server state:");
            CustomLogger.logInfo(message);
        }, 0, 10, TimeUnit.SECONDS);

        // Listen for client connections
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            CustomLogger.logInfo("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // Submit new task to thread pool
            FutureTask<String> task = new FutureTask<>(new ClientHandler(clientSocket, sqlEngine));
            executorService.submit(task);
        }
    }
}





