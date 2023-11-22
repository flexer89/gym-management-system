package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
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
        mainPanel.setBackground(new Color(238, 242, 247));

        // Create a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a title label
        JLabel titleLabel = new JLabel("Gym Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));

        // Add the title label to the header panel
        headerPanel.add(titleLabel);

        // Add the header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a content panel
        JPanel contentPanel = new JPanel(new GridLayout(3, 1));
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a reports panel
        JPanel reportsPanel = new JPanel(new GridLayout(1, 5));
        reportsPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportsPanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the reports panel
        JButton paymentReportButton = new JButton("Payment Report");
        JButton gymReportButton = new JButton("Gym Report");
        JButton clientReportButton = new JButton("Client Report");
        JButton employeeReportButton = new JButton("Employee Report");
        JButton trainingReportButton = new JButton("Training Report");

        // Set the style of the buttons
        paymentReportButton.setBackground(new Color(238, 242, 247));
        paymentReportButton.setForeground(new Color(51, 51, 51));
        paymentReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        gymReportButton.setBackground(new Color(238, 242, 247));
        gymReportButton.setForeground(new Color(51, 51, 51));
        gymReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clientReportButton.setBackground(new Color(238, 242, 247));
        clientReportButton.setForeground(new Color(51, 51, 51));
        clientReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeReportButton.setBackground(new Color(238, 242, 247));
        employeeReportButton.setForeground(new Color(51, 51, 51));
        employeeReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        trainingReportButton.setBackground(new Color(238, 242, 247));
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
        gymManagementPanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the gym management panel
        JButton addGymButton = new JButton("Add Gym");
        JButton updateGymButton = new JButton("Edit Gym");
        JButton deleteGymButton = new JButton("Delete Gym");

        // Set the style of the buttons
        addGymButton.setBackground(new Color(238, 242, 247));
        addGymButton.setForeground(new Color(51, 51, 51));
        addGymButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateGymButton.setBackground(new Color(238, 242, 247));
        updateGymButton.setForeground(new Color(51, 51, 51));
        updateGymButton.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteGymButton.setBackground(new Color(238, 242, 247));
        deleteGymButton.setForeground(new Color(51, 51, 51));
        deleteGymButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the gym management panel
        gymManagementPanel.add(addGymButton);
        gymManagementPanel.add(updateGymButton);
        gymManagementPanel.add(deleteGymButton);

        // Create an employee management panel
        JPanel employeeManagementPanel = new JPanel(new GridLayout(1, 3));
        employeeManagementPanel.setBorder(BorderFactory.createTitledBorder("Employee Management"));
        employeeManagementPanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the employee management panel
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton updateEmployeeButton = new JButton("Edit Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");

        // Set the style of the buttons
        addEmployeeButton.setBackground(new Color(238, 242, 247));
        addEmployeeButton.setForeground(new Color(51, 51, 51));
        addEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateEmployeeButton.setBackground(new Color(238, 242, 247));
        updateEmployeeButton.setForeground(new Color(51, 51, 51));
        updateEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteEmployeeButton.setBackground(new Color(238, 242, 247));
        deleteEmployeeButton.setForeground(new Color(51, 51, 51));
        deleteEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the employee management panel
        employeeManagementPanel.add(addEmployeeButton);
        employeeManagementPanel.add(updateEmployeeButton);
        employeeManagementPanel.add(deleteEmployeeButton);

        // Create a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 77, 77));
        logoutButton.setForeground(new Color(255, 255, 255));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the reports panel, gym management panel, employee management panel, and logout button to the content panel
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
