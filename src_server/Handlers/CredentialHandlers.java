package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
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
        System.out.println("Generated salt: " + salt);
        String saltString = salt.toString();
        password = Secure.hashWithSalt(password, saltString);


        System.out.println("Passwd: " + password + " Salt: " + salt);

        try {
            int userID = sqlEngine.registerAccount(username, password, saltString, firstName, lastName, birthDate, phoneNumber, email);
            System.out.println("User " + userID + " registered");
            SendToClient.println(userID);
        } catch (SQLException e) {
            System.out.println("Error registering account: " + e.getMessage());
        }
    }

    public void changePassword(String serverMessage)
    {
        String[] registerInfo = serverMessage.split(",");
        String newPassword = registerInfo[0];
        String userIDString = registerInfo[1];
        String userType = registerInfo[2];
        
        try {
            System.out.println("User " + userIDString + " changing password");
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            System.out.println("Generated salt: " + salt);
            String saltString = salt.toString();
            newPassword = Secure.hashWithSalt(newPassword, saltString);
            System.out.println("Passwd: " + newPassword + " Salt: " + salt);

            boolean ifChanged = sqlEngine.changePassword(userIDString, newPassword, saltString, userType);
            if (ifChanged) {
                System.out.println("Password changed");
                SendToClient.println("True");
            } else {
                System.out.println("Password wasn't changed");
                SendToClient.println("False");
            }
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }

    }

    public void login(String serverMessage)
    {
        String[] loginInfo = serverMessage.split(",");
        String username = loginInfo[0];
        String password = loginInfo[1];

        try {
            String fetchedData = sqlEngine.getIDbyLogin(username);
            System.out.println("Fetched data: " + fetchedData);
            
            String[] parts = fetchedData.split(",");
            String ID = parts[0];
            String table = parts[1];
            
            System.out.println("Testing creds for table: " + table);
            String storedHash = sqlEngine.getHashByID(Integer.parseInt(ID), table);
            String salt = sqlEngine.getSaltByID(Integer.parseInt(ID), table);
            System.out.println("ID: " + ID + "   Hash: " + storedHash + " Salt: " + salt);
            String hashedInput = Secure.hashWithSalt(password, salt);
            System.out.println("Hashed input: " + hashedInput);
            
            if (storedHash.equals(hashedInput)) {
                System.out.println("Hashes match");
                try {
                    String data = sqlEngine.loginToAccount(username, hashedInput);
                    String type = data.split(",")[0];
                    int userID = Integer.parseInt(data.split(",")[1]);
            
                    System.out.println(type + " " + userID + " logged in");
                    SendToClient.println(userID);
                    SendToClient.println(type);
                } catch (SQLException e) {
                    System.out.println("SQL Error logging in: " + e.getMessage());
                    SendToClient.println(-1);
                    SendToClient.println("ERROR");
                }
            } else {
                System.out.println("Hashes don't match");
                SendToClient.println(-1);
                SendToClient.println("ERROR");
            }
        }
        catch (Exception all) {
            System.out.println("Error logging in: " + all.getMessage());
            SendToClient.println(-1);
            SendToClient.println("ERROR");
        }

    }

}