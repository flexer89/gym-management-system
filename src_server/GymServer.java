import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.concurrent.*;

// Server class
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
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Log the server state
            // TODO: Create guide to server log messages
            String message = "  Number of active tasks: " + ((ThreadPoolExecutor) executorService).getActiveCount();
            System.out.println(Color.ColorString("Server state:", Color.ANSI_GREEN));
            System.out.println(Color.ColorString(message, Color.ANSI_YELLOW));
        }, 0, 5, TimeUnit.SECONDS);

        // Create server socket
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on port " + SERVER_PORT);
        } catch (IOException e) {
            System.out.println("Could not listen on port " + SERVER_PORT);
            System.exit(-1);
        }

        try {
            // Create DB engine
            sqlEngine = new SQLEngine(host, DBPort, DBName, username, password);
            System.out.println("DB engine created");

            // Connect to DB
            sqlEngine.getConnection();
            System.out.println("DB connection established");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Listen for client connections
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // Submit new task to thread pool
            FutureTask<String> task = new FutureTask<>(new ClientHandler(clientSocket, sqlEngine));
            executorService.submit(task);
        }
    }
}





