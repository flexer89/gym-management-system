package gui.dashboard.client_dashboard.membership_extension;

import utils.Message;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class OneGymMembership extends JFrame {
    private JTextField startDateField;
    private JTextField monthsField;
    private JButton addButton;
    private JButton cancelButton;

    public OneGymMembership(Message message, BufferedReader readFromServer, PrintWriter sendToServer, int userId) {
        // Create the main window
        this.setSize(960, 600);
        this.setVisible(true);
        this.setTitle("Gym Management System | Membership Card Management");

        // Add the report table
        DefaultTableModel reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Address", "Postal Code", "City", "Phone", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the ID column unmodifiable
                return column != 0;
            }
        };
        JTable reportTable = new JTable(reportTableModel);
        this.add(new JScrollPane(reportTable), BorderLayout.NORTH);

         // Send the message to the server
        message.sendLoadGymMessage(sendToServer, "null");

        // Clear the table
        reportTableModel.setRowCount(0);

        // Read the report from the server
        try {
            String report = readFromServer.readLine();
            String[] reportLines = report.split("///");

            // Add the report to the report table
            for (String reportLine : reportLines) {
                String[] reportLineParts = reportLine.split(",");
                reportTableModel.addRow(reportLineParts);
            }
        } catch (IOException ex) {
            System.out.println(utils.Color.ANSI_RED + "Error reading response from server." + utils.Color.ANSI_RESET);
        }

        // Create the panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Add the Start field
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(new JLabel("Start Date:"), c);

        c.gridx = 1;
        c.gridy = 0;
        startDateField = new JTextField(20);
        formPanel.add(startDateField, c);

        // Add the Months field
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(new JLabel("Months:"), c);

        c.gridx = 1;
        c.gridy = 1;
        monthsField = new JTextField(20);
        formPanel.add(monthsField, c);

        // Add the original gym id label
        c.gridy = 2;
        formPanel.add(new JLabel("You can only set one gym for this membership"), c);

        // Create the panel for the buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Add the panels to the main window
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the start date and months
                String startDate = startDateField.getText();
                String monthsString = monthsField.getText();

                // check if empty
                if (startDate.isEmpty() || monthsString.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if months is a number
                int months;
                try {
                    months = Integer.parseInt(monthsString);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Months must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // validate
                if (!utils.ValidateData.ValidateDate(startDate) || !utils.ValidateData.ValidateIsBefore(startDate)) {
                    return;
                }

                // Get the selected row
                int selectedRow = reportTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row", "Success", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get the gym ID
                int gymId = Integer.parseInt(reportTable.getValueAt(selectedRow, 0).toString());

                new PaymentWindow(message, readFromServer, sendToServer, userId, utils.Prices.ONE_GYM_MEMBERSHIP_PRICE * months, gymId, 0);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}