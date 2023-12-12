package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import utils.CustomLogger;


public class ReportsHandlers {

    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;

    //create constructor
    public ReportsHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
    }

    public void paymentReport(String data) {
        String[] reportData = data.split(",");
        LocalDate fromDate = LocalDate.parse(reportData[0]);
        LocalDate toDate = LocalDate.parse(reportData[1]);
        int minimumPayment = Integer.parseInt(reportData[2]);
        int maximumPayment = Integer.parseInt(reportData[3]);
        String paymentMethod = reportData[4];
        int clientID = Integer.parseInt(reportData[5]);

        // Generate payment report
    CustomLogger.logInfo("Generating payment report, Data: " + fromDate + " " + toDate + " | " + minimumPayment + " " + maximumPayment + " | " + paymentMethod + " | " + clientID);
        try {
            String report = sqlEngine.paymentReport(fromDate, toDate, minimumPayment, maximumPayment, paymentMethod, clientID);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating payment report: " + e.getMessage());
        }
    }

    public void gymReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        String address = reportData[1];
        String postalCode = reportData[2];
        String city = reportData[3];
        String phoneNumber = reportData[4];
        String email = reportData[5];

        // Generate gym report
        CustomLogger.logInfo("Generating gym report, Data: " + name + " " + address + " | " + postalCode + " " + city + " | " + phoneNumber + " | " + email);
        try {
            String report = sqlEngine.gymReport(name, address, postalCode, city, phoneNumber, email);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating gym report: " + e.getMessage());
        }
    }

    public void clientReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        String surname = reportData[1];
        LocalDate fromDate = LocalDate.parse(reportData[2]);
        LocalDate toDate = LocalDate.parse(reportData[3]);
        String phoneNumber = reportData[4];
        String email = reportData[5];
        String membershipCard = reportData[6];

        // Generate client report
        CustomLogger.logInfo("Generating client report, Data: " + name + " " + surname + " | " + fromDate + " " + toDate + " | " + phoneNumber + " | " + email + " | " + membershipCard);
        try {
            String report = sqlEngine.clientReport(name, surname, fromDate, toDate, phoneNumber, email, membershipCard);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating client report: " + e.getMessage());
        }
    }


    public void employeeReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        String surname = reportData[1];
        LocalDate fromDateBirth = LocalDate.parse(reportData[2]);
        LocalDate toDateBirth = LocalDate.parse(reportData[3]);
        LocalDate fromDateEmployment = LocalDate.parse(reportData[4]);
        LocalDate toDateEmployment = LocalDate.parse(reportData[5]);
        String phoneNumber = reportData[6];
        String email = reportData[7];
        String position = reportData[8];

        // Generate employee report
        CustomLogger.logInfo("Generating employee report, Data: " + name + " " + surname + " | " + fromDateBirth + " " + toDateBirth + " | " + fromDateEmployment + " " + toDateEmployment + " | " + phoneNumber + " | " + email + " | " + position);
        try {
            String report = sqlEngine.employeeReport(name, surname, fromDateBirth, toDateBirth, fromDateEmployment, toDateEmployment, phoneNumber, email, position);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating employee report: " + e.getMessage());
        }
    }


    public void trainingReport(String data) {
        String[] reportData = data.split(",");
        String name = reportData[0];
        LocalDate fromDate = LocalDate.parse(reportData[1]);
        LocalDate toDate = LocalDate.parse(reportData[2]);
        LocalTime fromHour = LocalTime.parse(reportData[3]);
        LocalTime toHour = LocalTime.parse(reportData[4]);
        int capacity = Integer.parseInt(reportData[5]);
        int room = Integer.parseInt(reportData[6]);
        int trainerId = Integer.parseInt(reportData[7]);
        int clientID = Integer.parseInt(reportData[8]);

        // Generate training report
        CustomLogger.logInfo("Generating training report, Data: " + name + " | " + fromDate + " " + toDate + " | " + fromHour + " " + toHour + " | " + capacity + " | " + room + " | " + trainerId + " | " + clientID);
        try {
            String report = sqlEngine.trainingReport(name, fromDate, toDate, fromHour, toHour, capacity, room, trainerId, clientID);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating training report: " + e.getMessage());
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
        CustomLogger.logInfo("Generating time spent report, Data: " + entranceFromDate + " " + entranceToDate + " | " + entranceFromHour + " " + entranceToHour + " | " + exitFromDate + " " + exitToDate + " | " + exitFromHour + " " + exitToHour + " | " + userID);
        try {
            String report = sqlEngine.timeSpentReport(entranceFromDate, entranceToDate, entranceFromHour, entranceToHour, exitFromDate, exitToDate, exitFromHour, exitToHour, userID);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating time spent report: " + e.getMessage());
        }
    }



    public void timeSpentEmployeeReport(String data) {
        String[] reportData = data.split(",");
        LocalDate entranceFromDate = LocalDate.parse(reportData[0]);
        LocalDate entranceToDate = LocalDate.parse(reportData[1]);
        LocalTime entranceFromHour = LocalTime.parse(reportData[2]);
        LocalTime entranceToHour = LocalTime.parse(reportData[3]);
        LocalDate exitFromDate = LocalDate.parse(reportData[4]);
        LocalDate exitToDate = LocalDate.parse(reportData[5]);
        LocalTime exitFromHour = LocalTime.parse(reportData[6]);
        LocalTime exitToHour = LocalTime.parse(reportData[7]);
        int employeeID = Integer.parseInt(reportData[8]);

        // Generate time spent report
        CustomLogger.logInfo("Generating time spent report, Data: " + entranceFromDate + " " + entranceToDate + " | " + entranceFromHour + " " + entranceToHour + " | " + exitFromDate + " " + exitToDate + " | " + exitFromHour + " " + exitToHour + " | " + employeeID);
        try {
            String report = sqlEngine.timeSpentEmployeeReport(entranceFromDate, entranceToDate, entranceFromHour, entranceToHour, exitFromDate, exitToDate, exitFromHour, exitToHour, employeeID);
            SendToClient.println(report);
        } catch (SQLException e) {
            CustomLogger.logError("Error generating time spent report: " + e.getMessage());
        }
    }
}