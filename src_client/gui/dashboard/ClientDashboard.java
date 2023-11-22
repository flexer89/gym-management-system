package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
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
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setTitle("Gym Management System | Client Dashboard");

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
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a reports panel
        JPanel reportsPanel = new JPanel(new GridLayout(1, 5));
        reportsPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportsPanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the reports panel
        JButton paymentReportButton = new JButton("Payment Report");
        JButton timeSpentReportButton = new JButton("Time Spent Report");
        JButton trainingReportButton = new JButton("Training Report");

        // Set the style of the buttons
        paymentReportButton.setBackground(new Color(238, 242, 247));
        paymentReportButton.setForeground(new Color(51, 51, 51));
        paymentReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        timeSpentReportButton.setBackground(new Color(238, 242, 247));
        timeSpentReportButton.setForeground(new Color(51, 51, 51));
        timeSpentReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        trainingReportButton.setBackground(new Color(238, 242, 247));
        trainingReportButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the reports panel
        reportsPanel.add(paymentReportButton);
        reportsPanel.add(timeSpentReportButton);
        reportsPanel.add(trainingReportButton);

        // Create an employee management panel
        JPanel generalClientManagementButton = new JPanel(new GridLayout(1, 3));
        generalClientManagementButton.setBorder(BorderFactory.createTitledBorder("General"));
        generalClientManagementButton.setBackground(new Color(255, 255, 255));

        // Create buttons for the employee management panel
        JButton trainingManagementButton = new JButton("Training Management");
        JButton membershipManagementButton = new JButton("Membership Management");
        JButton profileManagementButton = new JButton("My Profile");

        // Set the style of the buttons
        trainingManagementButton.setBackground(new Color(238, 242, 247));
        trainingManagementButton.setForeground(new Color(51, 51, 51));
        trainingManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
        profileManagementButton.setBackground(new Color(238, 242, 247));
        profileManagementButton.setForeground(new Color(51, 51, 51));
        profileManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
        membershipManagementButton.setBackground(new Color(238, 242, 247));
        membershipManagementButton.setForeground(new Color(51, 51, 51));
        membershipManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the employee management panel
        generalClientManagementButton.add(trainingManagementButton);
        generalClientManagementButton.add(profileManagementButton);
        generalClientManagementButton.add(membershipManagementButton);

        // Create a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 77, 77));
        logoutButton.setForeground(new Color(255, 255, 255));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the reports panel, gym management panel, employee management panel, and logout button to the content panel
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
                new TimeSpentReportWindow(message, ReadFromServer, SendToServer);
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
                new MembershipCardWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for profile management button
        profileManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new ProfileWindow(message, ReadFromServer, SendToServer, userID);
                } catch (IOException e1) {
                    System.out.println(utils.Color.ANSI_RED + "Error creating profile window." + utils.Color.ANSI_RESET);
                }
            }
        });

    }
}
