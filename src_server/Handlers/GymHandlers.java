package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import utils.CustomLogger;

public class GymHandlers {

    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;

    //create constructor
    public GymHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
    }

    public void addGym(String serverMessage)
    {
        String[] gymInfo = serverMessage.split(",");
        String name = gymInfo[0];
        String address = gymInfo[1];
        String postalCode = gymInfo[2];
        String city = gymInfo[3];
        String phone = gymInfo[4];
        String email = gymInfo[5];
        try {
            boolean ifAdded = sqlEngine.addGym(name, address, postalCode, city, phone, email);
            if (ifAdded) {
                CustomLogger.logInfo("Gym " + name + " added");
                SendToClient.println("True");
            } else {
                CustomLogger.logInfo("Gym " + name + " wasn't added");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error adding gym: " + e.getMessage());
        }
    }

    public void loadGym() {
        CustomLogger.logInfo("Loading gyms");
        try {
            String report = sqlEngine.loadGym();
            CustomLogger.logInfo("Gyms loaded");
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error loading gyms: " + e.getMessage());
        }
    }

    public void deleteGym(String data) {
        int gymID = Integer.parseInt(data);
        CustomLogger.logInfo("Deleting gym " + gymID);

        try {
            boolean ifDeleted = sqlEngine.deleteGym(gymID);
            if (ifDeleted) {
                CustomLogger.logInfo("Gym " + gymID + " deleted");
                SendToClient.println("True");
            } else {
                CustomLogger.logInfo("Gym " + gymID + " wasn't deleted");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error deleting gym: " + e.getMessage());
        }
    }

    public void updateGym(String data) {
        String[] gymInfo = data.split(",");
        int gymID = Integer.parseInt(gymInfo[0]);
        String name = gymInfo[1];
        String address = gymInfo[2];
        String postalCode = gymInfo[3];
        String city = gymInfo[4];
        String phone = gymInfo[5];
        String email = gymInfo[6];

        CustomLogger.logInfo("Updating gym " + gymID + " with data: " + name + " " + address + " | " + postalCode + " " + city + " | " + phone + " | " + email);

        try {
            boolean ifUpdated = sqlEngine.updateGym(gymID, name, address, postalCode, city, phone, email);
            if (ifUpdated) {
                CustomLogger.logInfo("Gym " + gymID + " updated");
                SendToClient.println("Update successful.");
            } else {
                CustomLogger.logInfo("Gym " + gymID + " wasn't updated");
                SendToClient.println("Error updating gym.");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error updating gym: " + e.getMessage());
        }
    }
    
}