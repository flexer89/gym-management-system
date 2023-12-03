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

    public UpdateTrainingWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int employeeID) {
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
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Start Hour", "End Hour", "Capacity", "Room", "Gym ID", "Gym"}, 0) {
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
                message.sendLoadEmployeeTrainingsMessage(SendToServer, employeeID + "");

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
                
                // Get the ID
                int trainingID = Integer.parseInt(reportTable.getValueAt(selectedRow, 0).toString());
                String name = reportTable.getValueAt(selectedRow, 1).toString();
                String date = reportTable.getValueAt(selectedRow, 2).toString();
                String startHour = reportTable.getValueAt(selectedRow, 3).toString();
                String endHour = reportTable.getValueAt(selectedRow, 4).toString();
                int capacity = Integer.parseInt(reportTable.getValueAt(selectedRow,5).toString());
                String room = reportTable.getValueAt(selectedRow,6).toString();
                String gymID = reportTable.getValueAt(selectedRow, 7).toString();
                String trainer = employeeID + "";

                // if empty fields
                if (name.isEmpty() || date.isEmpty() || startHour.isEmpty() || endHour.isEmpty() || room.isEmpty() || gymID.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate the data
                if (!ValidateData.validateName(name) || !ValidateData.ValidateDate(date) || !ValidateData.ValidateHourRange(startHour, endHour)|| !ValidateData.ValidateCapacity(capacity + "") || !ValidateData.ValidateRoom(room) || !ValidateData.ValidateGymID(gymID)) {
                    return;
                }

                // Send the ID to the server
                message.sendUpdateTrainingMessage(SendToServer, trainingID + "," + name + "," + date + "," + startHour + "," + endHour + "," + capacity + "," + room + "," + trainer + "," + gymID);
                try {
                    // Get the response from the server
                    String response = ReadFromServer.readLine();
                    if (response.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Training updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Training update failed! Trainer is busy or the room is occupied!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    System.out.println(utils.Color.ANSI_RED + "Error reading from server: " + e1.getMessage() + utils.Color.ANSI_RESET);
                }
            }
        });
    }
}