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
        PRINT, SEND, EXIT, CAN_ENTER_GYM, CAN_EXIT_GYM, CAN_ENTER_TRAINING,
        LOGIN, REGISTER, ADD_GYM, ADD_EMPLOYEE, PAYMENT_REPORT, GYM_REPORT,
        CLIENT_REPORT, EMPLOYEE_REPORT, TRAINING_REPORT, LOAD_GYM, DELETE_GYM,
        DELETE_EMPLOYEE, LOAD_EMPLOYEE, GET_CLIENT, GET_TRAINER, ADD_TRAINING,
        LOAD_TRAININGS, RESERVE_TRAINING, TIME_SPENT_REPORT, UNKNOWN,SHUTDOWN,
        CHANGE_PASSWORD
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
                    case EMPLOYEE_REPORT:
                        Handlers.employeeReport(data);
                        break;
                    case TRAINING_REPORT:
                        Handlers.trainingReport(data);
                        break;
                    case LOAD_GYM:
                        Handlers.loadGym();
                        break;
                    case DELETE_GYM:
                        Handlers.deleteGym(data);
                        break;
                    case DELETE_EMPLOYEE:
                        Handlers.deleteEmployee(data);
                        break;
                    case LOAD_EMPLOYEE:
                        Handlers.loadEmployees(data);
                        break;
                    case GET_CLIENT:
                        Handlers.getClient(data);
                        break;
                    case GET_TRAINER:
                        Handlers.getTrainer(data);
                        break;
                    case ADD_TRAINING:
                        Handlers.addTraining(data);
                        break;
                    case LOAD_TRAININGS:
                        Handlers.loadTrainings(data);
                        break;
                    case RESERVE_TRAINING:
                        Handlers.reserveTraining(data);
                        break;
                    case TIME_SPENT_REPORT:
                        Handlers.timeSpentReport(data);
                        break;
                    case CHANGE_PASSWORD:
                        Handlers.changePassword(data);
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
                if (commandType == CommandType.SHUTDOWN) {
                    break;
                }
            }

            // Close streams and socket
            ReadFromClient.close();
            SendToClient.close();
            clientSocket.close();
        } catch (Exception e) {
            System.out.println(utils.Color.ANSI_RED + "Exception in client handler: " + e.getMessage() + utils.Color.ANSI_RESET);
        }
        return "Task completed";
    }
}