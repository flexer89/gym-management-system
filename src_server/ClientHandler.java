import java.net.*;
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
        PRINT, SEND, EXIT, CAN_ENTER_GYM, CAN_EXIT_GYM, CAN_ENTER_TRAINING, LOGIN, REGISTER, ADD_GYM, ADD_EMPLOYEE, PAYMENT_REPORT, GYM_REPORT, CLIENT_REPORT, UNKNOWN,SHUTDOWN
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
                    case ADD_GYM:
                        Handlers.addGym(data);
                        break;
                    case ADD_EMPLOYEE:
                        Handlers.addEmployee(data);
                        break;
                    case PAYMENT_REPORT:
                        Handlers.paymentReport(data);
                        break;
                    case GYM_REPORT:
                        Handlers.gymReport(data);
                        break;
                    case CLIENT_REPORT:
                        Handlers.clientReport(data);
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