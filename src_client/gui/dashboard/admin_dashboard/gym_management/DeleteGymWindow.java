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

public class DeleteGymWindow extends JFrame {
    private JPanel mainPanel;
    private JButton deleteGymButton;
    private JButton loadGymButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public DeleteGymWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Delete Gym");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the delete gym panel and fit it to center
        JPanel deleteGymPanel = new JPanel();
        deleteGymPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delete Gym"));

        // Add the "Generate Report" button
        deleteGymButton = new JButton("Delete");

        // Add the load gym button to the main panel
        loadGymButton = new JButton("Load");

        deleteGymPanel.add(loadGymButton);
        deleteGymPanel.add(deleteGymButton);
        mainPanel.add(deleteGymPanel);

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

        // Add action listener to the "Generate Report" button
        deleteGymButton.addActionListener(new ActionListener() {    
            public void actionPerformed(ActionEvent e) {
                // Get the ID from table
                int row = reportTable.getSelectedRow(); 

                // Check if a row is selected
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row", "Success", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if fields aren't edited
                if (reportTable.isEditing()) {
                    JOptionPane.showMessageDialog(null, "Please finish editing the table", "Success", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get the ID from the table
                String id = reportTable.getValueAt(row, 0).toString();

                // Send the message to the server
                message.sendDeleteGymMessage(SendToServer, id);

                // read the report from the server
                try {
                    String report = ReadFromServer.readLine();
                    if (report.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Gym deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // Send the message to the server
                        message.sendLoadGymMessage(SendToServer, "null");

                        // Clear the table
                        reportTableModel.setRowCount(0);

                        // Read the report from the server
                        try {
                            report = ReadFromServer.readLine();
                            String[] reportLines = report.split("///");

                            // Add the report to the report table
                            for (String reportLine : reportLines) {
                                String[] reportLineParts = reportLine.split(",");
                                reportTableModel.addRow(reportLineParts);
                            }
                        } catch (IOException ex) {
                           CustomLogger.logError("Error reading from server: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Gym not deleted", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    CustomLogger.logError("Error reading from server: " + e1.getMessage());
                }
            }
        });

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
    }
}