package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;

import utils.CustomLogger;


public class ClientHandlers {

    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;

    //create constructor
    public ClientHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
    }
    

    public void canEnterTraining(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");

        String userID = loginInfo[0];
        int roomID = Integer.parseInt(loginInfo[1]);
        CustomLogger.logInfo("User " + userID + " wants to enter training room " + roomID);
        try {
            boolean canEnterTraining = sqlEngine.canEnterTraining(userID, roomID);
            if (canEnterTraining) {
                SendToClient.println("True");
                CustomLogger.logInfo("User " + userID + " entered training room " + roomID);
            } else {
                SendToClient.println("False");
                CustomLogger.logInfo("User " + userID + " didn't enter training room " + roomID);
            }
        } catch (SQLException e) {
            CustomLogger.logError("Error checking if user can enter training room: " + e.getMessage());
        }
    }

    public void canExitGym(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");
        String userID = loginInfo[0];
        int gymID = Integer.parseInt(loginInfo[1]);
        CustomLogger.logInfo("User " + userID + " wants to exit gym " + gymID);
        try {
            boolean canExitGym = sqlEngine.canExitGym(userID, gymID);
            SendToClient.println(canExitGym);
            CustomLogger.logInfo("User " + userID + " exited gym " + gymID);
        } catch (SQLException e) {
            CustomLogger.logError("Error checking if user can exit gym: " + e.getMessage());
        }
    }

    public void canEnterGym(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");

        String card_number = loginInfo[0];
        int gymID = Integer.parseInt(loginInfo[1]);
        CustomLogger.logInfo("User " + card_number + " wants to enter gym " + gymID);
        try {
            boolean canEnterGym = sqlEngine.canEnterGym(card_number, gymID);
            SendToClient.println(canEnterGym);
            CustomLogger.logInfo("User " + card_number + " entered gym " + gymID);
        } catch (SQLException e) {
            CustomLogger.logError("Error checking if user can enter gym: " + e.getMessage());
        }
    }

    public void getClient(String data) {
        int clientID = Integer.parseInt(data);
        CustomLogger.logInfo("Getting client " + clientID);

        try {
            String report = sqlEngine.getClient(clientID);
            SendToClient.println(report);
            CustomLogger.logInfo("Client " + clientID + " got");
        } catch (SQLException e) {
            CustomLogger.logError("Error getting client: " + e.getMessage());
        }
    }

    public void getMembershipCard(String data) {
        int userID = Integer.parseInt(data);
        CustomLogger.logInfo("Getting membership card for user " + userID);
        try {
            String report = sqlEngine.getMembershipCard(userID);
            SendToClient.println(report);
            CustomLogger.logInfo("Membership card for user " + userID + " got");
        } catch (SQLException e) {
            CustomLogger.logError("Error getting membership card: " + e.getMessage());
        }
    }


    public void payment(String data) {
        String[] paymentInfo = data.split(",");

        int amount = Integer.parseInt(paymentInfo[0]);
        String paymentMethod = paymentInfo[1];
        int userID = Integer.parseInt(paymentInfo[2]);
        int gymID = Integer.parseInt(paymentInfo[3]);
        boolean allGymAccess = Boolean.parseBoolean(paymentInfo[4]);
        LocalDate endDate = LocalDate.parse(paymentInfo[5]);

        CustomLogger.logInfo("Processing payment for user " + userID + ", amount: " + amount + ", payment method: " + paymentMethod);
        try {
            boolean paymentSuccessful = sqlEngine.payment(userID, amount, paymentMethod);
            String membershipCard = sqlEngine.addMembershipCard(userID, endDate, "membership", allGymAccess, gymID);
            SendToClient.println(paymentSuccessful + ":" + membershipCard);
            CustomLogger.logInfo("Payment for user " + userID + " processed");
        } catch (SQLException e) {
            CustomLogger.logError("Error processing payment: " + e.getMessage());
        }
    }


    public void cancelSubscription(String data) {
        int userID = Integer.parseInt(data);
        CustomLogger.logInfo("Cancelling subscription for user " + userID);
        try {
            boolean subscriptionCancelled = sqlEngine.cancelSubscription(userID);
            SendToClient.println(subscriptionCancelled);
            CustomLogger.logInfo("Subscription for user " + userID + " cancelled");
        } catch (SQLException e) {
            CustomLogger.logError("Error cancelling subscription: " + e.getMessage());
        }
    }


    public void loadClientTrainings(String data) {
        int userID = Integer.parseInt(data);
        CustomLogger.logInfo("Loading trainings for user " + userID);
        try {
            String report = sqlEngine.loadClientTrainings(userID);
            SendToClient.println(report);
            CustomLogger.logInfo("Trainings loaded for user " + userID);
        } catch (SQLException e) {
            CustomLogger.logError("Error loading trainings: " + e.getMessage());
        }
    }


    public void cancelReservation(String data) {
        String[] reservationInfo = data.split(",");

        int trainingID = Integer.parseInt(reservationInfo[0]);
        int userID = Integer.parseInt(reservationInfo[1]);
        CustomLogger.logInfo("Cancelling reservation for user " + userID + " for training " + trainingID);
        try {
            boolean reservationCancelled = sqlEngine.cancelReservation(trainingID, userID);
            SendToClient.println(reservationCancelled);
            CustomLogger.logInfo("Reservation for user " + userID + " for training " + trainingID + " cancelled");
        } catch (SQLException e) {
            CustomLogger.logError("Error cancelling reservation: " + e.getMessage());
        }
    }


    public void registerMultisportCard(String data) {
        String[] cardInfo = data.split(",");

        String cardNumber = cardInfo[0];
        int userID = Integer.parseInt(cardInfo[1]);
        CustomLogger.logInfo("Registering multisport card " + cardNumber + " for user " + userID);
        try {
            boolean cardRegistered = sqlEngine.registerMultisportCard(cardNumber, userID);
            SendToClient.println(cardRegistered);
        } catch (SQLException e) {
            CustomLogger.logError("Error registering multisport card: " + e.getMessage());
        }
    }

}