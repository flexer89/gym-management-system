import java.net.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.*;
import java.util.concurrent.*;

// Client handler class
class ClientHandler implements Callable<String> {
    private Socket clientSocket;
    private SQLEngine sqlEngine;

    public ClientHandler(Socket clientSocket, SQLEngine sqlEngine) {
        this.clientSocket = clientSocket;
        this.sqlEngine = sqlEngine;
    }

    enum CommandType {
        PRINT, SEND, EXIT, CAN_ENTER_GYM, CAN_EXIT_GYM, CAN_ENTER_TRAINING, LOGIN, REGISTER, UNKNOWN,SHUTDOWN
    }

    CommandType getCommandType(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.UNKNOWN;
        }
    }

    
    @Override
    public String call() {
        try {
            // Create input and output streams
            BufferedReader ReadFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter SendToClient = new PrintWriter(clientSocket.getOutputStream(), true);


            Handlers Handlers = new Handlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);

            while (true) {
                String serverMessage = ReadFromClient.readLine();
                String[] parts = serverMessage.split(":", 2);
                String command = parts[0];
                String data= parts[1];
                
                System.out.println("Command: " + command + " Data: " + data);
                
                CommandType commandType = getCommandType(command);
                
            
                switch (commandType) {
                    case PRINT:
                        Handlers.print(data);
                        break;
                    case SEND:
                        Handlers.send();
                        break;
                    case EXIT:
                        Handlers.exit();
                        break;
                    case CAN_ENTER_GYM:
                        Handlers.canEnterGym(data);
                        break;
                    case CAN_EXIT_GYM:
                        Handlers.canExitGym(data);
                        break;
                    case CAN_ENTER_TRAINING:
                        Handlers.canEnterTraining(data);
                        break;
                    case LOGIN:
                        Handlers.login(data);
                        break;
                    case REGISTER:
                        Handlers.register(data);
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
                //TODO how to implement this lol
                if (commandType == CommandType.SHUTDOWN) {
                    break;
                }
            }

            // Close streams and socket
            ReadFromClient.close();
            SendToClient.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Task completed";
    }

}