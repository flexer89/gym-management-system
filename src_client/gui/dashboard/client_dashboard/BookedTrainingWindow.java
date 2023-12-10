package gui.dashboard.client_dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import utils.Message;

public class BookedTrainingWindow extends JFrame {
    private JPanel mainPanel;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public BookedTrainingWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID) {
        String userIDString = Integer.toString(userID);
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Reservation");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the training report panel
        JPanel reservationPanel = new JPanel();
        reservationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Reservation"));
        reservationPanel.setLayout(new GridLayout(2, 4));
        mainPanel.add(reservationPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Start Time", "End Time", "Room", "Trainer Name", "Gym Location"}, 0);
        reportTable = new JTable(reportTableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        mainPanel.add(scrollPane, constraints);

         // Send the message to the server
        message.sendLoadClientTrainingsMessage(SendToServer, userIDString);

        // Clear the table
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

        // Add the report panel to the main window
        this.add(mainPanel);
    }
}