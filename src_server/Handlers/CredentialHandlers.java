package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;

import utils.CustomLogger;
import utils.Secure;

public class CredentialHandlers {

    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;

    //create constructor
    public CredentialHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
    }


    public void register(String serverMessage)
    {
        String[] registerInfo = serverMessage.split(",");
        String username = registerInfo[0];
        String password = registerInfo[1];
        String firstName = registerInfo[2];
        String lastName = registerInfo[3];
        LocalDate birthDate = LocalDate.parse(registerInfo[4]);
        String phoneNumber = registerInfo[5];
        String email = registerInfo[6];

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltString = salt.toString();
        password = Secure.hashWithSalt(password, saltString);

        CustomLogger.logInfo("Registering user " + username + " " + firstName + " " + lastName + " " + birthDate + " " + phoneNumber + " " + email);

        try {
            int userID = sqlEngine.registerAccount(username, password, saltString, firstName, lastName, birthDate, phoneNumber, email);
            CustomLogger.logInfo("User " + username + " registered");
            SendToClient.println(userID);
        } catch (SQLException e) {
            CustomLogger.logError("Error registering user: " + e.getMessage());
        }
    }

    public void changePassword(String serverMessage)
    {
        String[] registerInfo = serverMessage.split(",");
        String newPassword = registerInfo[0];
        String userIDString = registerInfo[1];
        String userType = registerInfo[2];
        
        try {
            CustomLogger.logInfo("Changing password for user " + userIDString);
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String saltString = salt.toString();
            newPassword = Secure.hashWithSalt(newPassword, saltString);

            boolean ifChanged = sqlEngine.changePassword(userIDString, newPassword, saltString, userType);
            if (ifChanged) {
                CustomLogger.logInfo("Password changed for user " + userIDString);
                SendToClient.println("True");
            } else {
                CustomLogger.logInfo("Password wasn't changed for user " + userIDString);
                SendToClient.println("False");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error changing password: " + e.getMessage());
        }

    }

    public void login(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");
        String username = loginInfo[0];
        String password = loginInfo[1];

        try {
            String fetchedData = sqlEngine.getIDbyLogin(username);
            
            String[] parts = fetchedData.split(",");
            String ID = parts[0];
            String table = parts[1];
            
            String storedHash = sqlEngine.getHashByID(Integer.parseInt(ID), table);
            String salt = sqlEngine.getSaltByID(Integer.parseInt(ID), table);
            String hashedInput = Secure.hashWithSalt(password, salt);

            CustomLogger.logInfo("Logging in user " + username);
            
            if (storedHash.equals(hashedInput)) {
                try {
                    String data = sqlEngine.loginToAccount(username, hashedInput);
                    String type = data.split(",")[0];
                    int userID = Integer.parseInt(data.split(",")[1]);
            
                    CustomLogger.logInfo("User " + username + " logged in");
                    SendToClient.println(userID);
                    SendToClient.println(type);
                } catch (SQLException e) {
                    CustomLogger.logError("Error logging in: " + e.getMessage());
                    SendToClient.println(-1);
                    SendToClient.println("ERROR");
                }
            } else {
                CustomLogger.logInfo("Hashes don't match for user " + username);
                SendToClient.println(-1);
                SendToClient.println("ERROR");
            }
        }
        catch (Exception e) {
            CustomLogger.logError("Error logging in: " + e.getMessage());
            SendToClient.println(-1);
            SendToClient.println("ERROR");
        }

    }

}