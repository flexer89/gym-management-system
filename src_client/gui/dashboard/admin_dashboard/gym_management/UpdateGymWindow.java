package gui.dashboard.admin_dashboard.gym_management;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import utils.ValidateData;

public class UpdateGymWindow extends JFrame {
    private JPanel mainPanel;
    private JButton updateButton;
    private JButton loadGymButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public UpdateGymWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Update Gym Window");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the Update gym panel
        JPanel updateGymPanel = new JPanel();
        updateGymPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delete Gym"));

        // Create the update button
        updateButton = new JButton("Update");

        // Add the load gym button to the main panel
        loadGymButton = new JButton("Load");

        updateGymPanel.add(loadGymButton);
        updateGymPanel.add(updateButton);
        mainPanel.add(updateGymPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Address", "Postal Code", "City", "Phone", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the ID column unmodifiable
                return column != 0;
            }
        };
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

        // Add action listener to the "Load" button
        loadGymButton.addActionListener(new ActionListener() {    
            public void actionPerformed(ActionEvent e) {
                // Send the message to the server
                message.sendLoadGymMessage(SendToServer, "null");

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

         // Add action listener to the "Generate Report" button
         updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

                // Get the data from the selected row
                String gymID = reportTable.getValueAt(selectedRow, 0).toString();
                String gymName = reportTable.getValueAt(selectedRow, 1).toString();
                String gymAddress = reportTable.getValueAt(selectedRow, 2).toString();
                String gymPostalCode = reportTable.getValueAt(selectedRow, 3).toString();
                String gymCity = reportTable.getValueAt(selectedRow, 4).toString();
                String gymPhone = reportTable.getValueAt(selectedRow, 5).toString();
                String gymEmail = reportTable.getValueAt(selectedRow, 6).toString();

                // Validate the data
                if (!ValidateData.validateName(gymName) || !ValidateData.validateAddress(gymAddress) || !ValidateData.ValidatePostalCode(gymPostalCode) || !ValidateData.ValidateCity(gymCity) || !ValidateData.validatePhoneNumber(gymPhone) || !ValidateData.validateMail(gymEmail)) {
                    return;
                }

                // Send the message to the server
                message.sendUpdateGymMessage(SendToServer, gymID + "," + gymName + "," + gymAddress + "," + gymPostalCode + "," + gymCity + "," + gymPhone + "," + gymEmail);

                // Read the response from the server
                try {
                    String response = ReadFromServer.readLine();

                    // Check if the update was successful
                    if (response.equals("Update successful.")) {
                        JOptionPane.showMessageDialog(null, response, "Updated succesfully.", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, response, "Error updating Gym", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    CustomLogger.logError("Error reading from server: " + ex.getMessage());
                }
            }
        });
    }
}