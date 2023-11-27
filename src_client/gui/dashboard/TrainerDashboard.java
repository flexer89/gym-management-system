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

import gui.dashboard.trainer_dashboard.training_management.*;
import gui.dashboard.trainer_dashboard.*;

import gui.login.LoginRegisterWindow;
import utils.Message;

public class TrainerDashboard extends JFrame {

    public TrainerDashboard(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, LoginRegisterWindow loginRegisterWindow, int employeeID) {
        // Create the main window
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setTitle("Gym Management System | Trainer Dashboard");

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
        JPanel reportsPanel = new JPanel(new GridLayout(1, 2));
        reportsPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportsPanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the reports panel
        JButton trainingReportButton = new JButton("Training Report");
        JButton timeSpentReporButton = new JButton("Time Spent Report");

        // Set the style of the buttons
        trainingReportButton.setBackground(new Color(238, 242, 247));
        trainingReportButton.setForeground(new Color(51, 51, 51));
        trainingReportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        timeSpentReporButton.setBackground(new Color(238, 242, 247));
        timeSpentReporButton.setForeground(new Color(51, 51, 51));
        timeSpentReporButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the reports panel
        reportsPanel.add(trainingReportButton);
        reportsPanel.add(timeSpentReporButton);

        // Create a gym management panel
        JPanel trainingManagementPanel = new JPanel(new GridLayout(1, 3));
        trainingManagementPanel.setBorder(BorderFactory.createTitledBorder("Training Management"));
        trainingManagementPanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the gym management panel
        JButton addTrainingButton = new JButton("Add Training");
        JButton updateTrainingButton = new JButton("Update Training");
        JButton deleteTrainingButton = new JButton("Delete Training");

        // Set the style of the buttons
        addTrainingButton.setBackground(new Color(238, 242, 247));
        addTrainingButton.setForeground(new Color(51, 51, 51));
        addTrainingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateTrainingButton.setBackground(new Color(238, 242, 247));
        updateTrainingButton.setForeground(new Color(51, 51, 51));
        updateTrainingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteTrainingButton.setBackground(new Color(238, 242, 247));
        deleteTrainingButton.setForeground(new Color(51, 51, 51));
        deleteTrainingButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the gym management panel
        trainingManagementPanel.add(addTrainingButton);
        trainingManagementPanel.add(updateTrainingButton);
        trainingManagementPanel.add(deleteTrainingButton);

        // Create My Profile panel  
        JPanel myProfilePanel = new JPanel(new GridLayout(1, 1));
        myProfilePanel.setBorder(BorderFactory.createTitledBorder("My Profile"));
        myProfilePanel.setBackground(new Color(255, 255, 255));

        // Create buttons for the My Profile panel
        JButton updateProfileButton = new JButton("My Profile");  
        updateProfileButton.setBackground(new Color(238, 242, 247));
        updateProfileButton.setForeground(new Color(51, 51, 51));
        updateProfileButton.setFont(new Font("Arial", Font.PLAIN, 14)); 

        // Add the buttons to the My Profile panel
        myProfilePanel.add(updateProfileButton);

        // Create a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 77, 77));
        logoutButton.setForeground(new Color(255, 255, 255));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the panels to the content panel
        contentPanel.add(reportsPanel);
        contentPanel.add(trainingManagementPanel);
        contentPanel.add(myProfilePanel);

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

        // =============================================================================================================
        // Add event listener for add training button
        addTrainingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddTrainingWindow(message, ReadFromServer, SendToServer, employeeID);
            }
        });

        // Add event listener for update training button
        updateTrainingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdateTrainingWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for delete training button
        deleteTrainingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteTrainingWindow(message, ReadFromServer, SendToServer);
            }
        });

        // =============================================================================================================
        // Add event listener for training report button
        trainingReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TrainingReportWindow(message, ReadFromServer, SendToServer);
            }
        });

        // Add event listener for time spent report button
        timeSpentReporButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EntranceReportWindow(message, ReadFromServer, SendToServer, employeeID);
            }
        });

        // =============================================================================================================

        // Add event listener for my profile button
        updateProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new ProfileWindow(message, ReadFromServer, SendToServer, employeeID);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
