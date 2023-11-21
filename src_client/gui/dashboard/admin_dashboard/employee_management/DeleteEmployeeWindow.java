package gui.dashboard.admin_dashboard.employee_management;

import java.awt.FlowLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import utils.Message;

public class DeleteEmployeeWindow extends JFrame {
    private JPanel mainPanel;
    private JPanel idPanel;
    private JLabel idLabel;
    private JTextField idTextField;
    private JButton deleteEmployeeButton;
    private JButton loadEmployeeButton;
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

        // Add the "ID" label
        idLabel = new JLabel("Type Employee ID to delete: ");
        idTextField = new JTextField();
        idPanel = new JPanel(new GridLayout(2,1));
        idPanel.add(idLabel);
        idPanel.add(idTextField);

        // Add the delete button
        deleteEmployeeButton = new JButton("Delete");

        // Add the load employee button to the main panel
        loadEmployeeButton = new JButton("Load");

        deleteEmployeePanel.add(loadEmployeeButton);
        deleteEmployeePanel.add(deleteEmployeeButton);
        deleteEmployeePanel.add(idPanel);
        mainPanel.add(deleteEmployeePanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Surname", "Position", "Date of Employment", "Phone", "Email"}, 0);
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
                // Get the ID from the text field
                String id = idTextField.getText();

                // Send the message to the server
                message.sendDeleteEmployeeMessage(SendToServer, id);

                // read the report from the server
                try {
                    String report = ReadFromServer.readLine();
                    if (report.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Employee deleted", "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Send the message to the server
                        message.sendLoadEmployeeMessage(SendToServer, "null");

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
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Employee not deleted", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        // Add action listener to the "Load" button
        loadEmployeeButton.addActionListener(new ActionListener() {    
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Gym button clicked");

                // Send the message to the server
                message.sendLoadEmployeeMessage(SendToServer, "null");

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
                    ex.printStackTrace();
                }
            }
        });
    }
}