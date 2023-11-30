package Handlers;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeeHandlers {

    private PrintWriter SendToClient;
    private SQLEngine sqlEngine;

    //create constructor
    public EmployeeHandlers(BufferedReader ReadFromClient, PrintWriter SendToClient, Socket clientSocket, SQLEngine sqlEngine) {
        this.SendToClient = SendToClient;
        this.sqlEngine = sqlEngine;
    }
    
    public void addEmployee(String data){
        String[] employeeInfo = data.split(",");
        String name = employeeInfo[0];
        String surname = employeeInfo[1];
        String position = employeeInfo[2];
        LocalDate dateOfBirth = LocalDate.parse(employeeInfo[3]);
        LocalDate dateOfEmployment = LocalDate.parse(employeeInfo[4]);
        String phone = employeeInfo[5];
        String email = employeeInfo[6];
        String login = employeeInfo[7];
        
        try {
            boolean ifAdded = sqlEngine.addEmployee(name, surname, position, dateOfBirth,dateOfEmployment, phone, email, login);
            if (ifAdded) {
                System.out.println("Employee " + name + " added");
                SendToClient.println("True");
            } else {
                System.out.println("Employee " + name + " wasn't added");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }

    }

    public void loadGym() {
        System.out.println("Loading gyms");
        try {
            String report = sqlEngine.loadGym();
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error loading gym: " + e.getMessage());
        }
    }

    public void deleteEmployee(String data) {
        int employeeID = Integer.parseInt(data);
        System.out.println("Deleting employee " + employeeID);

        try {
            boolean ifDeleted = sqlEngine.deleteEmployee(employeeID);
            if (ifDeleted) {
                System.out.println("Employee " + employeeID + " deleted");
                SendToClient.println("True");
            } else {
                System.out.println("Employee " + employeeID + " wasn't deleted");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }


    public void loadEmployees(String data) {
        System.out.println("Loading employees");
        try {
            String report = sqlEngine.loadEmployees();
            SendToClient.println(report);
        } catch (SQLException e) {
            System.out.println("Error loading employee: " + e.getMessage());
        }
    }

    public void updateEmployee(String data) {
        String[] employeeInfo = data.split(",");
        int employeeID = Integer.parseInt(employeeInfo[0]);
        String name = employeeInfo[1];
        String surname = employeeInfo[2];
        String position = employeeInfo[3];
        LocalDate dateOfBirth = LocalDate.parse(employeeInfo[4]);
        LocalDate dateOfEmployment = LocalDate.parse(employeeInfo[5]);
        String phone = employeeInfo[6];
        String email = employeeInfo[7];
        try {
            boolean ifUpdated = sqlEngine.updateEmployee(employeeID, name, surname, position, dateOfBirth,dateOfEmployment, phone, email);
            if (ifUpdated) {
                System.out.println("Employee " + employeeID + " updated");
                SendToClient.println("True");
            } else {
                System.out.println("Employee " + employeeID + " wasn't updated");
                SendToClient.println("False");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }
}