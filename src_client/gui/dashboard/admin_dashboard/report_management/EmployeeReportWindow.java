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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import utils.Message;
import utils.ValidateData;

public class EmployeeReportWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel surnameLabel;
    private JTextField surnameTextField;
    private JLabel fromDateBirthLabel;
    private JTextField fromDateBirthField;
    private JLabel toDateBirthLabel;
    private JTextField toDateBirthField;
    private JLabel fromDateEmploymentLabel;
    private JTextField fromDateEmploymentField;
    private JLabel toDateEmploymentLabel;
    private JTextField toDateEmploymentField;
    private JButton generateReportButton;
    private JTextField phoneNumberField;
    private JLabel phoneNumberLabel;
    private JTextField emailField;
    private JLabel emailLabel;
    private JLabel position;
    private JComboBox<String> positionComboBox;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public EmployeeReportWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Employee Report");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the client report panel
        JPanel ClientReportPanel = new JPanel();
        ClientReportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Report"));
        ClientReportPanel.setLayout(new GridLayout(2, 4));

        // Add the namelabel and text field
        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(9);
        
        JPanel namePanel = new JPanel(new GridLayout(2, 2));
        namePanel.add(nameLabel, constraints);
        namePanel.add(nameTextField, constraints);

        // Add the surname label and text field
        surnameLabel = new JLabel("Surname:");
        surnameTextField = new JTextField(9);
        namePanel.add(surnameLabel, constraints);
        namePanel.add(surnameTextField, constraints);

        // Add the from date label and text field
        fromDateBirthLabel = new JLabel("Date of birth (from):");
        fromDateBirthField = new JTextField(10);
        JPanel fromDateBirthPanel = new JPanel(new GridLayout(2, 2));
        fromDateBirthPanel.add(fromDateBirthLabel, constraints);
        fromDateBirthPanel.add(fromDateBirthField, constraints);

        // Add the to date label and text field
        toDateBirthLabel = new JLabel("Date of birth (to):");
        toDateBirthField = new JTextField(10);
        fromDateBirthPanel.add(toDateBirthLabel, constraints);
        fromDateBirthPanel.add(toDateBirthField, constraints);

        // Add the from date label and text field
        fromDateEmploymentLabel = new JLabel("Start Date (from):");
        fromDateEmploymentField = new JTextField(10);
        JPanel fromDateEmploymentPanel = new JPanel(new GridLayout(2, 2));
        fromDateEmploymentPanel.add(fromDateEmploymentLabel, constraints);
        fromDateEmploymentPanel.add(fromDateEmploymentField, constraints);

        // Add the to date label and text field
        toDateEmploymentLabel = new JLabel("Start Date (to):");
        toDateEmploymentField = new JTextField(10);
        fromDateEmploymentPanel.add(toDateEmploymentLabel, constraints);
        fromDateEmploymentPanel.add(toDateEmploymentField, constraints);

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

        // Add the position label and combo box
        position = new JLabel("Position:");
        String[] employeeTypes = {"", "Trainer", "Admin"};
        positionComboBox = new JComboBox<>(employeeTypes);
        JPanel positionPanel = new JPanel(new GridLayout(2, 1));
        positionPanel.add(position, constraints);
        positionPanel.add(positionComboBox, constraints);

        // Add the "From Date" and "To Date" panels to the payment report panel
        ClientReportPanel.add(namePanel);
        ClientReportPanel.add(fromDateBirthPanel);
        ClientReportPanel.add(fromDateEmploymentPanel);
        ClientReportPanel.add(phoneNumberPanel);
        ClientReportPanel.add(emailPanel);
        ClientReportPanel.add(positionPanel);

        // Add the "Generate Report" button
        generateReportButton = new JButton("Generate Report");
        JButton exportButton = new JButton("Export to PDF");
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(generateReportButton);
        buttonPanel.add(exportButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(ClientReportPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Surname", "Date of birth", "Job start", "Phone Number", "Email", "Position"}, 0);
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
                String fromDateBirth = fromDateBirthField.getText();
                String toDateBirth = toDateBirthField.getText();
                String fromDateEmployment = fromDateEmploymentField.getText();
                String toDateEmployment = toDateEmploymentField.getText();
                String phoneNumber = phoneNumberField.getText();
                String email = emailField.getText();
                String position = positionComboBox.getSelectedItem().toString();

                // validate data
                if (!ValidateData.validateMail(email) || !ValidateData.validateName(name) || !ValidateData.validateSurname(surname) || !ValidateData.validatePhoneNumber(phoneNumber) || !ValidateData.ValidateDataRange(fromDateBirth, toDateBirth) || !ValidateData.ValidateDataRange(fromDateEmployment, toDateEmployment)) {
                    return;
                }

                // if any of the fields are empty, set them to null
                name = name.isEmpty() ? "%" : name;
                surname = surname.isEmpty() ? "%" : surname;
                fromDateBirth = fromDateBirth.isEmpty() ? "1900-01-01" : fromDateBirth;
                toDateBirth = toDateBirth.isEmpty() ? "2100-01-01" : toDateBirth;
                fromDateEmployment = fromDateEmployment.isEmpty() ? "1900-01-01" : fromDateEmployment;
                toDateEmployment = toDateEmployment.isEmpty() ? "2100-01-01" : toDateEmployment;
                phoneNumber = phoneNumber.isEmpty() ? "%" : phoneNumber;
                email = email.isEmpty() ? "%" : email;
                position = position.isEmpty() ? "all" : position;

                // Send the report data to the server
                SendToServer.println("EMPLOYEE_REPORT:" + name + "," + surname + "," + fromDateBirth + "," + toDateBirth + "," + fromDateEmployment + "," + toDateEmployment + "," + phoneNumber + "," + email + "," + position);

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

        // Add action listener to the "Export to PDF" button
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                utils.ExportToPDF.ExportTableToPDF(reportTableModel, "report.pdf");
            }
        });
    }
}