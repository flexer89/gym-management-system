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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import utils.CustomLogger;
import utils.Message;

public class ReservationWindow extends JFrame {
    private JPanel mainPanel;
    private JButton loadButton;
    private JButton reserveButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public ReservationWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID) {
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

        // Add reserve and load buttons
        reserveButton = new JButton("Reserve");
        loadButton = new JButton("Load");
        mainPanel.add(loadButton); 
        mainPanel.add(reserveButton);
        mainPanel.add(reservationPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Start Time", "End Time", "Current Reservations", "Room", "Trainer Name", "Gym Location"}, 0);
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
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row
                int selectedRow = reportTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row", "Success", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get the training ID
                String trainingID = reportTable.getValueAt(selectedRow, 0).toString();

                // Send the message to the server
                message.sendReserveTrainingMessage(SendToServer, userIDString + "," + trainingID);

                // Read the response from the server
                try {
                    String response = ReadFromServer.readLine();

                    // Check if the reservation was successful
                    if (response.equals("Reservation successful.")) {
                        JOptionPane.showMessageDialog(null, response, "Successfully reserved training", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, response, "Error reserving training", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    CustomLogger.logError("Error reading from server: " + ex.getMessage());
                }
            }
        });

        // Add action listener to the "Load" button
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Send the message to the server
                message.sendLoadTrainingsMessage(SendToServer, userIDString);

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
                    CustomLogger.logError("Error reading from server: " + ex.getMessage());
                }
            }
        });
    }
}