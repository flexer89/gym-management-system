import java.net.*;
import java.nio.channels.AsynchronousChannel;
import java.sql.SQLException;
import java.io.*;
import java.util.concurrent.*;

// Server class
public class GymServer {
    // Server properties
    private static final int SERVER_PORT = 5000;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    // DB properties
    private static final String host = "sql.freedb.tech";
    private static final int DBPort = 3306;
    private static final String DBName = "freedb_jodatabase";
    private static final String username = "freedb_jolszak";
    private static final String password = "5Mz4t#?&AbwX@wF";
    private static SQLEngine sqlEngine;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        // Create server socket
        try {
            // Create asynchronous server socket
            serverSocket = new Asyn
            System.out.println("Server started on port " + SERVER_PORT);
        }
        catch (IOException e) {
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

            // Get result from task
            try {
                String result = task.get();
                System.out.println("Result: " + result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}





