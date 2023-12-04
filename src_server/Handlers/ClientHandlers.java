package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;


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
        System.out.println("User with membership card with ID: " + userID + " wants to enter room " + roomID);
        try {
            boolean canEnterTraining = sqlEngine.canEnterTraining(userID, roomID);
            if (canEnterTraining) {
                SendToClient.println("True");
            } else {
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            System.out.println("Error checking if user can enter training: " + e.getMessage());
        }
    }

    public void canExitGym(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");
        String userID = loginInfo[0];
        int gymID = Integer.parseInt(loginInfo[1]);
        System.out.println("User " + userID + " wants to exit gym " + gymID);
        try {
            boolean canExitGym = sqlEngine.canExitGym(userID, gymID);
            SendToClient.println(canExitGym);
        } catch (SQLException e) {
            System.out.println("Error checking if user can exit gym: " + e.getMessage());
        }
    }

    public void canEnterGym(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");

        String card_number = loginInfo[0];
        int gymID = Integer.parseInt(loginInfo[1]);
        System.out.println("User with card " + card_number + " wants to enter gym " + gymID);
        try {
            boolean canEnterGym = sqlEngine.canEnterGym(card_number, gymID);
            SendToClient.println(canEnterGym);
        } catch (SQLException e) {
            System.out.println("Error checking if user can enter gym: " + e.getMessage());
        }
    }

    public void getClient(String data) {
        int clientID = Integer.parseInt(data);
        System.out.println("Getting client " + clientID);

        try {
            String report = sqlEngine.getClient(clientID);
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error getting client: " + e.getMessage());
        }
    }

    public void getMembershipCard(String data) {
        int userID = Integer.parseInt(data);
        System.out.println("Getting membership card for client " + userID);
        try {
            String report = sqlEngine.getMembershipCard(userID);
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error getting membership card: " + e.getMessage());
        }
    }


    public void payment(String data) {
        String[] paymentInfo = data.split(",");

        int amount = Integer.parseInt(paymentInfo[0]);
        String paymentMethod = paymentInfo[1];
        int userID = Integer.parseInt(paymentInfo[2]);
        int gymID = Integer.parseInt(paymentInfo[3]);
        boolean allGymAccess = Boolean.parseBoolean(paymentInfo[4]);

        System.out.println("User " + userID + " wants to pay " + amount + " PLN using " + paymentMethod);
        try {
            boolean paymentSuccessful = sqlEngine.payment(userID, amount, paymentMethod);
            String membershipCard = sqlEngine.addMembershipCard(userID, LocalDate.now().plusDays(1), "membership", allGymAccess, gymID);
            SendToClient.println(paymentSuccessful+":"+membershipCard);
        } catch (SQLException e) {
            System.out.println("Error processing payment: " + e.getMessage());
        }
    }


    public void cancelSubscription(String data) {
        int userID = Integer.parseInt(data);
        System.out.println("User " + userID + " wants to cancel subscription");
        try {
            boolean subscriptionCancelled = sqlEngine.cancelSubscription(userID);
            SendToClient.println(subscriptionCancelled);
        } catch (SQLException e) {
            System.out.println("Error cancelling subscription: " + e.getMessage());
        }
    }

}