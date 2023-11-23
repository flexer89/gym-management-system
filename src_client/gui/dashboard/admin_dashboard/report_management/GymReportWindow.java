package gui.dashboard.admin_dashboard.report_management;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import utils.Message;
import utils.ValidateData;

public class GymReportWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel addressLabel;
    private JTextField addressField;
    private JTextField postalCodeField;
    private JLabel postalCodeLabel;
    private JTextField cityField;
    private JLabel cityLabel;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumberField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;
    private JButton generateReportButton;

    public GymReportWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(960, 640);
        this.setVisible(true);
        this.setTitle("Gym Management System | Gym Report");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the payment report panel
        JPanel gymReportPanel = new JPanel();
        gymReportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Gym Report"));

        // Add the name label and text field
        nameLabel = new JLabel("Name:");
        nameField = new JTextField(6);
        
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.add(nameLabel, constraints);
        namePanel.add(nameField, constraints);

        // Add the address label and text field
        addressLabel = new JLabel("Address:");
        addressField = new JTextField(20);
        JPanel addressPanel = new JPanel(new GridLayout(2, 1));
        addressPanel.add(addressLabel, constraints);
        addressPanel.add(addressField, constraints);

        // Add the postalcode label and text field
        postalCodeLabel = new JLabel("Postal Code:");
        postalCodeField = new JTextField(6);
        JPanel postalCodePanel = new JPanel(new GridLayout(2, 1));
        postalCodePanel.add(postalCodeLabel, constraints);
        postalCodePanel.add(postalCodeField, constraints);

        // Add the city label and text field
        cityLabel = new JLabel("City:");
        cityField = new JTextField(9);
        JPanel cityPanel = new JPanel(new GridLayout(2, 1));
        cityPanel.add(cityLabel, constraints);
        cityPanel.add(cityField, constraints);

        // Add the phone number label and text field
        phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberField = new JTextField(9);
        JPanel phoneNumberPanel = new JPanel(new GridLayout(2, 1));
        phoneNumberPanel.add(phoneNumberLabel, constraints);
        phoneNumberPanel.add(phoneNumberField, constraints);

        // Add the email label and text field
        emailLabel = new JLabel("Email:");
        emailField = new JTextField(9);
        JPanel emailPanel = new JPanel(new GridLayout(2, 1));
        emailPanel.add(emailLabel, constraints);
        emailPanel.add(emailField, constraints);

        // Add panels to the gym report panel
        gymReportPanel.add(namePanel);
        gymReportPanel.add(addressPanel);
        gymReportPanel.add(postalCodePanel);
        gymReportPanel.add(cityPanel);
        gymReportPanel.add(phoneNumberPanel);
        gymReportPanel.add(emailPanel);

        // Add the "Generate Report" button
        generateReportButton = new JButton("Generate Report");
        mainPanel.add(generateReportButton);
        mainPanel.add(gymReportPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Address", "Postal Code", "City", "Phone", "Email"}, 0);
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
        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the report data
                String name = nameField.getText();
                String address = addressField.getText();
                String postalCode = postalCodeField.getText();
                String city = cityField.getText();
                String phoneNumber = phoneNumberField.getText();
                String email = emailField.getText();

                // Validate the report data
                if (!ValidateData.validateMail(email) || !ValidateData.validateName(name) || !ValidateData.validatePhoneNumber(phoneNumber) || !ValidateData.ValidatePostalCode(postalCode) || !ValidateData.validateName(city)) {
                    return;
                }

                name = name.isEmpty() ? "%" : name;
                address = address.isEmpty() ? "%" : address;
                postalCode = postalCode.isEmpty() ? "%" : postalCode;
                city = city.isEmpty() ? "%" : city;
                phoneNumber = phoneNumber.isEmpty() ? "%" : phoneNumber;
                email = email.isEmpty() ? "%" : email;

                // Send the report data to the server
                message.sendGymReportMessage(SendToServer, name + "," + address + "," + postalCode + "," + city + "," + phoneNumber + "," + email);

                // Clear the report table
                reportTableModel.setRowCount(0);

                // Get the report from the server
                try {
                    String report = ReadFromServer.readLine();
                    String[] reportLines = report.split("///");
                    for (String reportLine : reportLines) {
                        String[] reportLineData = reportLine.split(",");
                        reportTableModel.addRow(reportLineData);
                    }
                } catch (IOException ex) {
                    System.out.println(utils.Color.ANSI_RED + "Error reading response from server." + utils.Color.ANSI_RESET);
                }
            }
        });
    }
}