package gui.dashboard.client_dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import utils.CustomLogger;
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
        JButton cancelReservationButton = new JButton("Cancel Reservation");
        reservationPanel.add(cancelReservationButton);
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
            CustomLogger.logError("Error while reading the report from the server: " + ex.getMessage());
        }

        // Add the report panel to the main window
        this.add(mainPanel);

        // Add the action listeners
        cancelReservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Get the selected row
                int selectedRow = reportTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row", "Success", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if fields aren't edited
                if (reportTable.isEditing()) {
                    JOptionPane.showMessageDialog(null, "Please finish editing the table", "Success", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // get the training ID
                String trainingID = reportTable.getValueAt(selectedRow, 0).toString();

                // Send the message to the server
                message.sendCancelReservationMessage(SendToServer, trainingID + "," + userIDString);

                // Read the response from the server
                try {
                    String response = ReadFromServer.readLine();
                    if (response.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Reservation cancelled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error cancelling reservation", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    CustomLogger.logError("Error while reading the response from the server: " + ex.getMessage());
                }
            }
        });
    }
}