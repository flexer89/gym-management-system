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

import utils.CustomLogger;
import utils.Message;
import utils.ValidateData;

public class ClientReportWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel surnameLabel;
    private JTextField surnameTextField;
    private JLabel fromDateLabel;
    private JTextField fromDateField;
    private JLabel toDateLabel;
    private JTextField toDateField;
    private JButton generateReportButton;
    private JTextField phoneNumberField;
    private JLabel phoneNumberLabel;
    private JTextField emailField;
    private JLabel emailLabel;
    private JLabel membershipCardLabel;
    private JTextField membershipCardField;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public ClientReportWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(960, 640);
        this.setVisible(true);
        this.setTitle("Gym Management System | Client Report");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the client report panel
        JPanel ClientReportPanel = new JPanel();
        ClientReportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Client Report"));

        // Add the namelabel and text field
        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(9);
        
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.add(nameLabel, constraints);
        namePanel.add(nameTextField, constraints);

        // Add the surname label and text field
        surnameLabel = new JLabel("Surname:");
        surnameTextField = new JTextField(9);
        JPanel surnamePanel = new JPanel(new GridLayout(2, 1));
        surnamePanel.add(surnameLabel, constraints);
        surnamePanel.add(surnameTextField, constraints);

        // Add the from date label and text field
        fromDateLabel = new JLabel("Date of birth:");
        fromDateField = new JTextField(10);
        JPanel fromDatePanel = new JPanel(new GridLayout(2, 1));
        fromDatePanel.add(fromDateLabel, constraints);
        fromDatePanel.add(fromDateField, constraints);

        // Add the to date label and text field
        toDateLabel = new JLabel("Date of birth:");
        toDateField = new JTextField(10);
        JPanel toDatePanel = new JPanel(new GridLayout(2, 1));
        toDatePanel.add(toDateLabel, constraints);
        toDatePanel.add(toDateField, constraints);

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

        // Add the membership card label and combo box
        membershipCardLabel = new JLabel("Membership Card:");  
        membershipCardField = new JTextField(9);
        JPanel membershipCardPanel = new JPanel(new GridLayout(2, 1));
        membershipCardPanel.add(membershipCardLabel, constraints);
        membershipCardPanel.add(membershipCardField, constraints);

        // Add the "From Date" and "To Date" panels to the payment report panel
        ClientReportPanel.add(namePanel);
        ClientReportPanel.add(surnamePanel);
        ClientReportPanel.add(fromDatePanel);
        ClientReportPanel.add(toDatePanel);
        ClientReportPanel.add(phoneNumberPanel);
        ClientReportPanel.add(emailPanel);
        ClientReportPanel.add(membershipCardPanel);

        // Add the "Generate Report" button
        generateReportButton = new JButton("Generate Report");
        JButton exportButton = new JButton("Export to PDF");
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(generateReportButton);
        buttonPanel.add(exportButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(ClientReportPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Surname", "Date of birth", "Phone Number", "Email", "Membership Card ID"}, 0);
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
                // Get the data from the text fields
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                String fromDate = fromDateField.getText();
                String toDate = toDateField.getText();
                String phoneNumber = phoneNumberField.getText();
                String email = emailField.getText();
                String membershipCard = membershipCardField.getText();

                // validate data
                if (!ValidateData.validateMail(email) || !ValidateData.validateName(name) || !ValidateData.validateSurname(surname) || !ValidateData.validatePhoneNumber(phoneNumber) || !ValidateData.ValidateDataRange(fromDate, toDate)) {
                    return;
                }

                // if any of the fields are empty, set them to null
                name = name.isEmpty() ? "%" : name;
                surname = surname.isEmpty() ? "%" : surname;
                fromDate = fromDate.isEmpty() ? "1900-01-01" : fromDate;
                toDate = toDate.isEmpty() ? "2100-01-01" : toDate;
                phoneNumber = phoneNumber.isEmpty() ? "%" : phoneNumber;
                email = email.isEmpty() ? "%" : email;
                membershipCard = membershipCard.isEmpty() ? "%" : membershipCard;

                // Send the report data to the server
                SendToServer.println("CLIENT_REPORT:" + name + "," + surname + "," + fromDate + "," + toDate + "," + phoneNumber + "," + email + "," + membershipCard);

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
                    CustomLogger.logError("Error reading from server: " + ex.getMessage());
                }
            }
        });

        // Add action listener to the "Export to PDF" button
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                utils.ExportToPDF.ExportTableToPDF(reportTableModel, "report.pdf");
            }
        });
    }
}
