package gui.dashboard.admin_dashboard.employee_management;

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

public class UpdateEmployeeWindow extends JFrame {
    private JPanel mainPanel;
    private JButton updateButton;
    private JButton loadGymButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public UpdateEmployeeWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Update Employee Window");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the Update gym panel
        JPanel updateEmployee = new JPanel();
        updateEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder("Delete Gym"));

        // Create the update button
        updateButton = new JButton("Update");

        // Add the load gym button to the main panel
        loadGymButton = new JButton("Load");

        updateEmployee.add(loadGymButton);
        updateEmployee.add(updateButton);
        mainPanel.add(updateEmployee);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Surname", "Position", "Date of Birth", "Date of Employment", "Phone", "Email"}, 0) {
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
                String id = reportTable.getValueAt(selectedRow, 0).toString();
                String name = reportTable.getValueAt(selectedRow, 1).toString();
                String surname = reportTable.getValueAt(selectedRow, 2).toString();
                String position = reportTable.getValueAt(selectedRow, 3).toString();
                String dateOfBirth = reportTable.getValueAt(selectedRow, 4).toString();
                String dateOfEmployment = reportTable.getValueAt(selectedRow, 5).toString();
                String phone = reportTable.getValueAt(selectedRow, 6).toString();
                String email = reportTable.getValueAt(selectedRow, 7).toString();

                // Validate the data
                if (!ValidateData.validateName(name) || !ValidateData.validateName(surname) || !ValidateData.ValidatePosition(position) || !ValidateData.ValidateDate(dateOfBirth) || !ValidateData.ValidateDate(dateOfEmployment) || !ValidateData.validatePhoneNumber(phone) || !ValidateData.validateMail(email)) {
                    return;
                }

                // Send the message to the server
                message.sendUpdateEmployeeMessage(SendToServer, id + "," + name + "," + surname + "," + position + "," + dateOfBirth + "," + dateOfEmployment + "," + phone + "," + email);

                // Read the response from the server
                try {
                    String response = ReadFromServer.readLine();

                    // Check if the update was successful
                    if (response.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Employee updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error updating employee", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    System.out.println(utils.Color.ANSI_RED + "Error reading response from server." + utils.Color.ANSI_RESET);
                }
            }
        });
    }
}