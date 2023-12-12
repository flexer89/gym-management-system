import java.net.*;
import java.io.*;
import java.util.concurrent.*;

import Handlers.*;
import utils.CustomLogger;

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
        GET_MEMBERSHIP_CARD, PAYMENT, CANCEL_SUBSCRIPTION, UPDATE_TRAINING,
        CHANGE_PASSWORD, UPDATE_GYM, UPDATE_EMPLOYEE, TIME_SPENT_EMPLOYEE_REPORT,
        LOAD_EMPLOYEE_TRAININGS, DELETE_TRAINING, LOAD_CLIENT_TRAININGS
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


            SystemHandlers          SystemHandlers      = new SystemHandlers(ReadFromClient, SendToClient,clientSocket);
            CredentialHandlers      CredentialHandlers  = new CredentialHandlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);
            ReportsHandlers         ReportsHandlers     = new ReportsHandlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);
            ClientHandlers          ClientHandlers      = new ClientHandlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);
            EmployeeHandlers        EmployeeHandlers    = new EmployeeHandlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);
            GymHandlers             GymHandlers         = new GymHandlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);
            TrainingHandlers        TrainingHandlers    = new TrainingHandlers(ReadFromClient, SendToClient,clientSocket, sqlEngine);

            while (true) {
                String serverMessage = ReadFromClient.readLine();
                String[] parts = serverMessage.split(":", 2);
                String command = parts[0];
                String data= parts[1];
                
                CustomLogger.logInfo("Received command: " + command + " with data: " + String.join(" | ", data));
                
                CommandType commandType = getCommandType(command);
                
            
                switch (commandType) {
                    case PRINT:
                        SystemHandlers.print(data);
                        break;
                    case SEND:
                        SystemHandlers.send();
                        break;
                    case EXIT:
                        SystemHandlers.exit();
                        break;
                    case CAN_ENTER_GYM:
                        ClientHandlers.canEnterGym(data);
                        break;
                    case CAN_EXIT_GYM:
                        ClientHandlers.canExitGym(data);
                        break;
                    case CAN_ENTER_TRAINING:
                        ClientHandlers.canEnterTraining(data);
                        break;
                    case LOGIN:
                        CredentialHandlers.login(data);
                        break;
                    case REGISTER:
                        CredentialHandlers.register(data);
                        break;
                    case ADD_GYM:
                        GymHandlers.addGym(data);
                        break;
                    case ADD_EMPLOYEE:
                        EmployeeHandlers.addEmployee(data);
                        break;
                    case PAYMENT_REPORT:
                        ReportsHandlers.paymentReport(data);
                        break;
                    case GYM_REPORT:
                        ReportsHandlers.gymReport(data);
                        break;
                    case CLIENT_REPORT:
                        ReportsHandlers.clientReport(data);
                        break;
                    case EMPLOYEE_REPORT:
                        ReportsHandlers.employeeReport(data);
                        break;
                    case TRAINING_REPORT:
                        ReportsHandlers.trainingReport(data);
                        break;
                    case LOAD_GYM:
                        GymHandlers.loadGym();
                        break;
                    case DELETE_GYM:
                        GymHandlers.deleteGym(data);
                        break;
                    case DELETE_EMPLOYEE:
                        EmployeeHandlers.deleteEmployee(data);
                        break;
                    case LOAD_EMPLOYEE:
                        EmployeeHandlers.loadEmployees(data);
                        break;
                    case GET_CLIENT:
                        ClientHandlers.getClient(data);
                        break;
                    case GET_TRAINER:
                        TrainingHandlers.getTrainer(data);
                        break;
                    case ADD_TRAINING:
                        TrainingHandlers.addTraining(data);
                        break;
                    case LOAD_TRAININGS:
                        TrainingHandlers.loadTrainings(data);
                        break;
                    case RESERVE_TRAINING:
                        TrainingHandlers.reserveTraining(data);
                        break;
                    case TIME_SPENT_REPORT:
                        ReportsHandlers.timeSpentReport(data);
                        break;
                    case CHANGE_PASSWORD:
                        CredentialHandlers.changePassword(data);
                        break;
                    case GET_MEMBERSHIP_CARD:
                        ClientHandlers.getMembershipCard(data);
                        break;
                    case PAYMENT:
                        ClientHandlers.payment(data);
                        break;
                    case CANCEL_SUBSCRIPTION:
                        ClientHandlers.cancelSubscription(data);
                        break;
                    case UPDATE_GYM:
                        GymHandlers.updateGym(data);
                        break;
                    case UPDATE_EMPLOYEE:
                        EmployeeHandlers.updateEmployee(data);
                        break;
                    case UPDATE_TRAINING:
                        TrainingHandlers.updateTraining(data);
                        break;
                    case TIME_SPENT_EMPLOYEE_REPORT:
                        ReportsHandlers.timeSpentEmployeeReport(data);
                        break;
                    case LOAD_EMPLOYEE_TRAININGS:
                        EmployeeHandlers.loadEmployeeTrainings(data);
                        break;
                    case DELETE_TRAINING:
                        TrainingHandlers.deleteTraining(data);
                        break;
                    case LOAD_CLIENT_TRAININGS:
                        ClientHandlers.loadClientTrainings(data);
                        break;
                    default:
                        CustomLogger.logWarning("Unknown command: " + command);
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
            CustomLogger.logError("Error in client handler: " + e.getMessage());
        }
        return "Task completed";
    }
}