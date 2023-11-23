package gui.dashboard.client_dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import utils.Message;
import utils.ValidateData;

public class TimeSpentReportWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel entranceDateLabel;
    private JTextField entranceDateField;
    private JLabel entranceTimeLabel;
    private JTextField entranceTimeField;
    private JLabel exitDateLabel;
    private JTextField fromDateTextField;
    private JLabel exitTimeLabel;
    private JTextField exitTimeexitDatePanel;
    private JLabel entranceFromHourLabel;
    private JTextField entranceFromHourField;
    private JLabel entranceToHourLabel;
    private JTextField entranceToHourField;
    private JButton generateReportButton;
    private JTextField capacityTextField;
    private JLabel capacityLabel;
    private JTextField trainerIdTextField;
    private JLabel trainerIdLabel;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;
    private JTextField exitToHourField;
    private JLabel exitToHourLabel;
    private JLabel exitFromHourLabel;
    private JTextField exitFromHourField;
    private JLabel exitFromDateLabel;
    private JTextField exitFromDateField;
    private JLabel exitToDateLabel;
    private JTextField exitToDateField;
    private JLabel entranceFromDateLabel;
    private JTextField entranceFromDateField;
    private JLabel entranceToDateLabel;
    private JTextField entranceToDateField;

    public TimeSpentReportWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID) {
        String userIDString = Integer.toString(userID);
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Time Spent Report");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the training report panel
        JPanel timeSpentPanel = new JPanel();
        timeSpentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Time Spent Report"));
        timeSpentPanel.setLayout(new GridLayout(2, 4));

        // Add the from hour label and text field
        entranceFromHourLabel = new JLabel("Entrance Time (from):");
        entranceFromHourField = new JTextField(10);
        JPanel entranceHourPanel = new JPanel(new GridLayout(2, 2));
        entranceHourPanel.add(entranceFromHourLabel, constraints);
        entranceHourPanel.add(entranceFromHourField, constraints);

        // Add the to hour label and text field
        entranceToHourLabel = new JLabel("Entrance Time (to):");
        entranceToHourField = new JTextField(10);
        entranceHourPanel.add(entranceToHourLabel, constraints);
        entranceHourPanel.add(entranceToHourField, constraints);

        // Add the from hour label and text field
        exitFromHourLabel = new JLabel("Exit Time (from):");
        exitFromHourField = new JTextField(10);
        JPanel exitHourPanel = new JPanel(new GridLayout(2, 2));
        exitHourPanel.add(exitFromHourLabel, constraints);
        exitHourPanel.add(exitFromHourField, constraints);

        // Add the to hour label and text field
        exitToHourLabel = new JLabel("Exit Time (to):");
        exitToHourField = new JTextField(10);
        exitHourPanel.add(exitToHourLabel, constraints);
        exitHourPanel.add(exitToHourField, constraints);

        // Add the from date label and text field
        exitFromDateLabel = new JLabel("Exit Date (from):");
        exitFromDateField = new JTextField(10);
        JPanel exitDatePanel = new JPanel(new GridLayout(2, 2));
        exitDatePanel.add(exitFromDateLabel, constraints);
        exitDatePanel.add(exitFromDateField, constraints);

        // Add the to date label and text field
        exitToDateLabel = new JLabel("Exit Date (to):");
        exitToDateField = new JTextField(10);
        exitDatePanel.add(exitToDateLabel, constraints);
        exitDatePanel.add(exitToDateField, constraints);

        // Add the from date label and text field
        entranceFromDateLabel = new JLabel("Exit Date (from):");
        entranceFromDateField = new JTextField(10);
        JPanel entranceDatePanel = new JPanel(new GridLayout(2, 2));
        entranceDatePanel.add(entranceFromDateLabel, constraints);
        entranceDatePanel.add(entranceFromDateField, constraints);

        // Add the to date label and text field
        entranceToDateLabel = new JLabel("Exit Date (to):");
        entranceToDateField = new JTextField(10);
        entranceDatePanel.add(entranceToDateLabel, constraints);
        entranceDatePanel.add(exitToDateField, constraints);

        // Add panels to the main panel
        timeSpentPanel.add(entranceHourPanel);
        timeSpentPanel.add(exitHourPanel);
        timeSpentPanel.add(entranceDatePanel);
        timeSpentPanel.add(exitDatePanel);

        // Add the "Generate Report" button
        generateReportButton = new JButton("Generate Report");
        mainPanel.add(generateReportButton);
        mainPanel.add(timeSpentPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Entrance Date", "Entrance Time", "Exit Date", "Exit Time", "Time Spent"}, 0);
        reportTable = new JTable(reportTableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        mainPanel.add(scrollPane, constraints);

        // Add the report panel to the main window
        this.add(mainPanel);

        // Add action listener to the "Generate Report" button
        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the data from the text fields
                String entranceFromHour = entranceFromHourField.getText();
                String entranceToHour = entranceToHourField.getText();
                String exitFromHour = exitFromHourField.getText(); 
                String exitToHour = exitToHourField.getText();
                String entranceFromDate = entranceFromDateField.getText();
                String entranceToDate = entranceToDateField.getText();
                String exitFromDate = exitFromDateField.getText();
                String exitToDate = exitToDateField.getText();

                // Validate the data
                if (!ValidateData.ValidateDataRange(entranceFromDate, entranceToDate) || !ValidateData.ValidateDataRange(exitFromDate, exitToDate) || !ValidateData.ValidateDataRange(entranceToDate, exitFromDate) || !ValidateData.ValidateHourRange(entranceToHour, exitFromHour) || !ValidateData.ValidateHourRange(exitFromHour, exitToHour) || !ValidateData.ValidateHourRange(entranceFromHour, entranceToHour)) {
                    return;
                }

                // Set empty fields 
                entranceFromDate = entranceFromDate.isEmpty() ? "1900-01-01" : entranceFromDate;
                entranceToDate = entranceToDate.isEmpty() ? "2100-01-01" : entranceToDate;
                entranceToHour = entranceToHour.isEmpty() ? "23:59" : entranceToHour;
                entranceFromHour = entranceFromHour.isEmpty() ? "00:00" : entranceFromHour;

                exitFromDate = exitFromDate.isEmpty() ? "1900-01-01" : exitFromDate;
                exitToDate = exitToDate.isEmpty() ? "2100-01-01" : exitToDate;
                exitToHour = exitToHour.isEmpty() ? "23:59" : exitToHour;
                exitFromHour = exitFromHour.isEmpty() ? "00:00" : exitFromHour;

                // Send the message to the server
                message.sendTimeSpentReportMessage(SendToServer, entranceFromDate + "," + entranceToDate + "," + entranceFromHour + "," + entranceToHour + "," + exitFromDate + "," + exitToDate + "," + exitFromHour + "," + exitToHour + "," + userIDString);

                // Clear the report table
                reportTableModel.setRowCount(0);

                // Read the report from the server
                try {
                    String report = ReadFromServer.readLine();
                    String[] reportLines = report.split("///");

                    // Add the report to the report table
                    for (String reportLine : reportLines) {
                        String[] reportLineParts = reportLine.split(",");
                        reportTableModel.addRow(reportLineParts);
                    }
                } catch (IOException ex) {
                    System.out.println(utils.Color.ANSI_RED + "Error reading response from server." + utils.Color.ANSI_RESET);
                }
            }
        });
    }
}