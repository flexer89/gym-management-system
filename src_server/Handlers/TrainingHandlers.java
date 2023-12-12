package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import utils.CustomLogger;

public class TrainingHandlers {

    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;

    //create constructor
    public TrainingHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
    }

    public void getTrainer(String data) {
        int trainerID = Integer.parseInt(data);
        CustomLogger.logInfo("Getting trainer " + trainerID);

        try {
            String report = sqlEngine.getTrainer(trainerID);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error getting trainer: " + e.getMessage());
        }
    }


    public void addTraining(String data) {
        String[] trainingInfo = data.split(",");
        String name = trainingInfo[0];
        LocalDate date = LocalDate.parse(trainingInfo[1]);
        LocalTime startHour = LocalTime.parse(trainingInfo[2]);
        LocalTime endHour = LocalTime.parse(trainingInfo[3]);
        int capacity = Integer.parseInt(trainingInfo[4]);
        int room = Integer.parseInt(trainingInfo[5]);
        int trainerID = Integer.parseInt(trainingInfo[6]);
        int gymID = Integer.parseInt(trainingInfo[7]);
        CustomLogger.logInfo("Adding training | Name: " + name + " Date: " + date + " Start hour: " + startHour + " End hour: " + endHour + " Capacity: " + capacity + " Room: " + room + " Trainer ID: " + trainerID + " Gym ID: " + gymID);

        try {
            boolean ifAdded = sqlEngine.addTraining(name, date, startHour, endHour, capacity, room, trainerID, gymID);
            if (ifAdded) {
                CustomLogger.logInfo("Training | Name: " + name + " Date: " + date + " Start hour: " + startHour + " End hour: " + endHour + " Capacity: " + capacity + " Room: " + room + " Trainer ID: " + trainerID + " Gym ID: " + gymID + " added");
                SendToClient.println("True");
            } else {
                CustomLogger.logError("Training | Name: " + name + " Date: " + date + " Start hour: " + startHour + " End hour: " + endHour + " Capacity: " + capacity + " Room: " + room + " Trainer ID: " + trainerID + " Gym ID: " + gymID + " wasn't added");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error adding training: " + e.getMessage());
        }
    }


    public void loadTrainings(String data) {
        int userID = Integer.parseInt(data);
        CustomLogger.logInfo("Loading trainings for client " + userID);
        try {
            String report = sqlEngine.loadTrainings(userID);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error loading trainings: " + e.getMessage());
        }
    }


    public void reserveTraining(String data) {
        String[] trainingInfo = data.split(",");
        int userID = Integer.parseInt(trainingInfo[0]);
        int trainingID = Integer.parseInt(trainingInfo[1]);
        CustomLogger.logInfo("Reserving training " + trainingID + " for client " + userID);

        try {
            boolean ifReserved = sqlEngine.reserveTraining(userID, trainingID);
            if (ifReserved) {
                CustomLogger.logInfo("Training " + trainingID + " reserved for client " + userID);
                SendToClient.println("Reservation successful.");
            } else {
                CustomLogger.logError("Training " + trainingID + " wasn't reserved for client " + userID);
                SendToClient.println("Reservation failed.");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error reserving training: " + e.getMessage());
        }
    }


    public void timeSpentReport(String data) {
        String[] reportData = data.split(",");
        LocalDate entranceFromDate = LocalDate.parse(reportData[0]);
        LocalDate entranceToDate = LocalDate.parse(reportData[1]);
        LocalTime entranceFromHour = LocalTime.parse(reportData[2]);
        LocalTime entranceToHour = LocalTime.parse(reportData[3]);
        LocalDate exitFromDate = LocalDate.parse(reportData[4]);
        LocalDate exitToDate = LocalDate.parse(reportData[5]);
        LocalTime exitFromHour = LocalTime.parse(reportData[6]);
        LocalTime exitToHour = LocalTime.parse(reportData[7]);
        int userID = Integer.parseInt(reportData[8]);

        // Generate time spent report
        CustomLogger.logInfo("Generating time spent report | Entrance from date: " + entranceFromDate + " Entrance to date: " + entranceToDate + " Entrance from hour: " + entranceFromHour + " Entrance to hour: " + entranceToHour + " Exit from date: " + exitFromDate + " Exit to date: " + exitToDate + " Exit from hour: " + exitFromHour + " Exit to hour: " + exitToHour + " User ID: " + userID);
        try {
            String report = sqlEngine.timeSpentReport(entranceFromDate, entranceToDate, entranceFromHour, entranceToHour, exitFromDate, exitToDate, exitFromHour, exitToHour, userID);
            SendToClient.println(report);
            CustomLogger.logInfo("Time spent report generated");
        } catch (SQLException e) {
            CustomLogger.logError("Error generating time spent report: " + e.getMessage());
        }
    }

    public void updateTraining(String data) {
        String[] trainingInfo = data.split(",");
        int trainingID = Integer.parseInt(trainingInfo[0]);
        String name = trainingInfo[1];
        LocalDate date = LocalDate.parse(trainingInfo[2]);
        LocalTime startHour = LocalTime.parse(trainingInfo[3]);
        LocalTime endHour = LocalTime.parse(trainingInfo[4]);
        int capacity = Integer.parseInt(trainingInfo[5]);
        int room = Integer.parseInt(trainingInfo[6]);
        int trainerID = Integer.parseInt(trainingInfo[7]);
        int gymID = Integer.parseInt(trainingInfo[8]);

        CustomLogger.logInfo("Updating training " + trainingID + " | Name: " + name + " Date: " + date + " Start hour: " + startHour + " End hour: " + endHour + " Capacity: " + capacity + " Room: " + room + " Trainer ID: " + trainerID + " Gym ID: " + gymID);

        try {
            boolean ifUpdated = sqlEngine.updateTraining(trainingID, name, date, startHour, endHour, capacity, room, trainerID, gymID);
            if (ifUpdated) {
                CustomLogger.logInfo("Training " + trainingID + " updated");
                SendToClient.println("True");
            } else {
                CustomLogger.logError("Training " + trainingID + " wasn't updated");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error updating training: " + e.getMessage());
        }
    }

    public void deleteTraining(String data) {
        int trainingID = Integer.parseInt(data);
        CustomLogger.logInfo("Deleting training " + trainingID);

        try {
            boolean ifDeleted = sqlEngine.deleteTraining(trainingID);
            if (ifDeleted) {
                CustomLogger.logInfo("Training " + trainingID + " deleted");
                SendToClient.println("True");
            } else {
                CustomLogger.logError("Training " + trainingID + " wasn't deleted");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error deleting training: " + e.getMessage());
        }
    }


}