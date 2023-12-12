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

import utils.CustomLogger;
import utils.Message;

public class DeleteEmployeeWindow extends JFrame {
    private JPanel mainPanel;
    private JButton deleteEmployeeButton;
    private JButton loadEmployeesButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public DeleteEmployeeWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Delete Employee");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the delete employee panel and fit it to center
        JPanel deleteEmployeePanel = new JPanel();
        deleteEmployeePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delete Employee"));

        // Add the delete button
        deleteEmployeeButton = new JButton("Delete");

        // Add the load employee button to the main panel
        loadEmployeesButton = new JButton("Load");

        deleteEmployeePanel.add(loadEmployeesButton);
        deleteEmployeePanel.add(deleteEmployeeButton);
        mainPanel.add(deleteEmployeePanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Surname", "Position", "Date of Employment", "Phone", "Email"}, 0) {
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
        deleteEmployeeButton.addActionListener(new ActionListener() {    
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
                String id = reportTable.getModel().getValueAt(row, 0).toString();

                // Send the message to the server
                message.sendDeleteEmployeeMessage(SendToServer, id);

                // read the report from the server
                try {
                    String report = ReadFromServer.readLine();
                    if (report.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Employee deleted", "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Send the message to the server
                        message.sendloadEmployeesMessage(SendToServer, "null");

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
                            CustomLogger.logError("Error reading response from server: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Employee not deleted", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    CustomLogger.logError("Error reading response from server: " + e1.getMessage());
                }
            }
        });

        // Add action listener to the "Load" button
        loadEmployeesButton.addActionListener(new ActionListener() {    
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
                    CustomLogger.logError("Error reading response from server: " + ex.getMessage());
                }
            }
        });
    }
}