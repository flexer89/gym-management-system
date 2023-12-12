package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.dashboard.client_dashboard.*;

import gui.login.LoginRegisterWindow;
import utils.*;

public class ClientDashboard extends JFrame{
    public ClientDashboard(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, LoginRegisterWindow loginRegisterWindow, int userID) {
        // Create the main window
        this.setSize(960, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setTitle("Gym Management System | Client Dashboard");

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
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setBackground(UIFormat.WHITE_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a reports panel
        JPanel reportsPanel = new JPanel(new GridLayout(1, 5));
        reportsPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportsPanel.setBackground(UIFormat.WHITE_BACKGROUND);

        // Create buttons for the reports panel
        JButton paymentReportButton = new JButton("Payment Report");
        JButton timeSpentReportButton = new JButton("Time Spent Report");
        JButton trainingReportButton = new JButton("Training Report");

        // Set the style of the buttons
        paymentReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        paymentReportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        paymentReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        timeSpentReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        timeSpentReportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        timeSpentReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        trainingReportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        trainingReportButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the reports panel
        reportsPanel.add(paymentReportButton);
        reportsPanel.add(timeSpentReportButton);
        reportsPanel.add(trainingReportButton);

        // Create an employee management panel
        JPanel generalClientManagementButton = new JPanel(new GridLayout(1, 3));
        generalClientManagementButton.setBorder(BorderFactory.createTitledBorder("General"));
        generalClientManagementButton.setBackground(UIFormat.WHITE_BACKGROUND);

        // Create buttons for the employee management panel
        JButton trainingManagementButton = new JButton("Training Management");
        JButton bookedTrainingButton = new JButton("My Trainings");
        JButton membershipManagementButton = new JButton("Membership Management");
        JButton profileManagementButton = new JButton("My Profile");

        // Set the style of the buttons
        trainingManagementButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        trainingManagementButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        trainingManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
        profileManagementButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        profileManagementButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        profileManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
        membershipManagementButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        membershipManagementButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        membershipManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
        bookedTrainingButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        bookedTrainingButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        bookedTrainingButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the employee management panel
        generalClientManagementButton.add(trainingManagementButton);
        generalClientManagementButton.add(bookedTrainingButton);
        generalClientManagementButton.add(profileManagementButton);
        generalClientManagementButton.add(membershipManagementButton);

        // Create a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(UIFormat.LOGOUT_BTN_BACKGROUND);
        logoutButton.setForeground(UIFormat.WHITE_BACKGROUND);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add panels to the content panel
        contentPanel.add(reportsPanel);
        contentPanel.add(generalClientManagementButton);

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
        // Add event listener for payment report button
        paymentReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PaymentReportWindow(message, ReadFromServer, SendToServer, userID);
            }
        });

        // Add event listener for time spent report button
        timeSpentReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TimeSpentReportWindow(message, ReadFromServer, SendToServer, userID);
            }
        });

        // Add event listener for training report button
        trainingReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TrainingReportWindow(message, ReadFromServer, SendToServer, userID);
            }
        });

        // ====================================================================================================
        // Add event listener for training management button
        trainingManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ReservationWindow(message, ReadFromServer, SendToServer, userID);
            }
        });

        // Add event listener for membership management button
        membershipManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new MembershipCardWindow(message, ReadFromServer, SendToServer, userID);
                } catch (IOException e1) {
                    CustomLogger.logError("Error opening membership card window: " + e1.getMessage());
                }
            }
        });

        // Add event listener for profile management button
        profileManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new ProfileWindow(message, ReadFromServer, SendToServer, userID);
                } catch (IOException e1) {
                    CustomLogger.logError("Error opening profile window: " + e1.getMessage());
                }
            }
        });

        // Add event listener for booked training button
        bookedTrainingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 new BookedTrainingWindow(message, ReadFromServer, SendToServer, userID);
            }
        });

    }
}
