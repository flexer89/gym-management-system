package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.dashboard.admin_dashboard.employee_management.*;
import gui.dashboard.admin_dashboard.gym_management.*;
import gui.dashboard.admin_dashboard.report_management.*;

import gui.login.LoginRegisterWindow;
import utils.Message;
import utils.UIFormat;

public class AdminDashboard extends JFrame {

    public AdminDashboard(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, LoginRegisterWindow loginRegisterWindow) {
        // Create the main window
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setTitle("Gym Management System | Admin Dashboard");

        // Create a main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);

        // Create a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIFormat.WHITE_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a title label
        JLabel titleLabel = new JLabel("Gym Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(UIFormat.DARK_GREY_FOREGROUND);

        // Add the title label to the header panel
        headerPanel.add(titleLabel);

        // Add the header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a content panel
        JPanel contentPanel = new JPanel(new GridLayout(3, 1));
        contentPanel.setBackground(UIFormat.WHITE_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a reports panel
        JPanel reportsPanel = new JPanel(new GridLayout(1, 5));
        reportsPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportsPanel.setBackground(UIFormat.WHITE_BACKGROUND);

        // Create buttons for the reports panel
        JButton paymentReportButton = new JButton("Payment Report");
        JButton gymReportButton = new JButton("Gym Report");
        JButton clientReportButton = new JButton("Client Report");
        JButton employeeReportButton = new JButton("Employee Report");
        JButton trainingReportButton = new JButton("Training Report");

        // Set the style of the buttons
        paymentReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        paymentReportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        paymentReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        gymReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        gymReportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        gymReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clientReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        clientReportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        clientReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        employeeReportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        employeeReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        trainingReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        trainingReportButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the reports panel
        reportsPanel.add(paymentReportButton);
        reportsPanel.add(gymReportButton);
        reportsPanel.add(clientReportButton);
        reportsPanel.add(employeeReportButton);
        reportsPanel.add(trainingReportButton);

        // Create a gym management panel
        JPanel gymManagementPanel = new JPanel(new GridLayout(1, 3));
        gymManagementPanel.setBorder(BorderFactory.createTitledBorder("Gym Management"));
        gymManagementPanel.setBackground(UIFormat.WHITE_BACKGROUND);

        // Create buttons for the gym management panel
        JButton addGymButton = new JButton("Add Gym");
        JButton updateGymButton = new JButton("Edit Gym");
        JButton deleteGymButton = new JButton("Delete Gym");

        // Set the style of the buttons
        addGymButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        addGymButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        addGymButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateGymButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        updateGymButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        updateGymButton.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteGymButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        deleteGymButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        deleteGymButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the gym management panel
        gymManagementPanel.add(addGymButton);
        gymManagementPanel.add(updateGymButton);
        gymManagementPanel.add(deleteGymButton);

        // Create an employee management panel
        JPanel employeeManagementPanel = new JPanel(new GridLayout(1, 3));
        employeeManagementPanel.setBorder(BorderFactory.createTitledBorder("Employee Management"));
        employeeManagementPanel.setBackground(UIFormat.WHITE_BACKGROUND);

        // Create buttons for the employee management panel
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton updateEmployeeButton = new JButton("Edit Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");

        // Set the style of the buttons
        addEmployeeButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        addEmployeeButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        addEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateEmployeeButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        updateEmployeeButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        updateEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteEmployeeButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        deleteEmployeeButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        deleteEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the employee management panel
        employeeManagementPanel.add(addEmployeeButton);
        employeeManagementPanel.add(updateEmployeeButton);
        employeeManagementPanel.add(deleteEmployeeButton);

        // Create a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(UIFormat.LOGOUT_BTN_BACKGROUND);
        logoutButton.setForeground(UIFormat.WHITE_BACKGROUND);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add reports panel to the content panel
        contentPanel.add(reportsPanel);
        contentPanel.add(gymManagementPanel);
        contentPanel.add(employeeManagementPanel);

        // Add the content panel to the main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the logout button to the main panel
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        // Add the main panel to the frame
        this.add(mainPanel);

        // Add event listener for logout button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                loginRegisterWindow.setVisible(true);
            }
        });

        // ====================================================================================================
        // Add event listener for add employee button
        addEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddEmployeeWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for update employee button
        updateEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdateEmployeeWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for delete employee button
        deleteEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteEmployeeWindow(message, ReadFromServer, SendToServer);
            }
        });


        // ====================================================================================================
        // Add event listener for update gym button
        updateGymButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdateGymWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for delete gym button
        deleteGymButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteGymWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for add gym button
        addGymButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddGymWindow(message, ReadFromServer, SendToServer);
            }
        });


        // ====================================================================================================
        // Add event listener for payment report button
        paymentReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PaymentReportWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for gym report button
        gymReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GymReportWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for client report button
        clientReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ClientReportWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for employee report button
        employeeReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EmployeeReportWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for training report button
        trainingReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TrainingReportWindow(message, ReadFromServer, SendToServer);
            }
        });
    }
}
