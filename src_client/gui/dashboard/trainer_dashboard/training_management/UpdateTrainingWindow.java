package gui.dashboard.trainer_dashboard.training_management;

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

import utils.Message;
import utils.ValidateData;

public class UpdateTrainingWindow extends JFrame {
    private JPanel mainPanel;
    private JButton updateButton;
    private JButton loadGymButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public UpdateTrainingWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Update Training Window");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the Update gym panel
        JPanel updateEmployee = new JPanel();
        updateEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder("Update Training"));

        // Create the update button
        updateButton = new JButton("Update");

        // Add the load gym button to the main panel
        loadGymButton = new JButton("Load");

        updateEmployee.add(loadGymButton);
        updateEmployee.add(updateButton);
        mainPanel.add(updateEmployee);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Start Hour", "End Hour", "Capacity", "Room", "Trainer", "Gym"}, 0) {
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
                message.sendloadEmployeesMessage(SendToServer, "null");

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
            }
        });

         // Add action listener to the update button
         updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the ID
                int trainingID = Integer.parseInt(reportTable.getValueAt(reportTable.getSelectedRow(), 0).toString());
                String name = reportTable.getValueAt(reportTable.getSelectedRow(), 1).toString();
                String date = reportTable.getValueAt(reportTable.getSelectedRow(), 2).toString();
                String startHour = reportTable.getValueAt(reportTable.getSelectedRow(), 3).toString();
                String endHour = reportTable.getValueAt(reportTable.getSelectedRow(), 4).toString();
                int capacity = Integer.parseInt(reportTable.getValueAt(reportTable.getSelectedRow(), 5).toString());
                String room = reportTable.getValueAt(reportTable.getSelectedRow(), 6).toString();
                String trainer = reportTable.getValueAt(reportTable.getSelectedRow(), 7).toString();
                String gym = reportTable.getValueAt(reportTable.getSelectedRow(), 8).toString();

                // Send the ID to the server
                message.sendUpdateTrainingMessage(SendToServer, trainingID + "," + name + "," + date + "," + startHour + "," + endHour + "," + capacity + "," + room + "," + trainer + "," + gym);
                try {
                    // Get the response from the server
                    String response = ReadFromServer.readLine();
                    if (response.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Training updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Training update failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    System.out.println(utils.Color.ANSI_RED + "Error reading from server: " + e1.getMessage() + utils.Color.ANSI_RESET);
                }
            }
        });
    }
}