package gui.dashboard.trainer_dashboard;

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

public class TrainingReportWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel roomLabel;
    private JTextField roomTextField;
    private JLabel fromDateLabel;
    private JTextField fromDateTextField;
    private JLabel toDateLabel;
    private JTextField toDateTextField;
    private JLabel fromHourLabel;
    private JTextField fromHourTextField;
    private JLabel toHourLabel;
    private JTextField toHourTextField;
    private JButton generateReportButton;
    private JTextField capacityTextField;
    private JLabel capacityLabel;
    private JTextField trainerIdTextField;
    private JLabel trainerIdLabel;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public TrainingReportWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int employeeID) {
        // Create the main window
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setTitle("Gym Management System | Training Report");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the training report panel
        JPanel trainingReportPanel = new JPanel();
        trainingReportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Training Report"));
        trainingReportPanel.setLayout(new GridLayout(2, 4));

        // Add the namelabel and text field
        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(9);
        
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.add(nameLabel, constraints);
        namePanel.add(nameTextField, constraints);

        // Add the room label and text field
        roomLabel = new JLabel("Room:");
        roomTextField = new JTextField(9);
        JPanel roomPanel = new JPanel(new GridLayout(2, 1));
        roomPanel.add(roomLabel, constraints);
        roomPanel.add(roomTextField, constraints);

        // Add the from date label and text field
        fromDateLabel = new JLabel("Date (from):");
        fromDateTextField = new JTextField(10);
        JPanel datePanel = new JPanel(new GridLayout(2, 2));
        datePanel.add(fromDateLabel, constraints);
        datePanel.add(fromDateTextField, constraints);

        // Add the to date label and text field
        toDateLabel = new JLabel("Date (to):");
        toDateTextField = new JTextField(10);
        datePanel.add(toDateLabel, constraints);
        datePanel.add(toDateTextField, constraints);

        // Add the from hour label and text field
        fromHourLabel = new JLabel("Start Hour (from):");
        fromHourTextField = new JTextField(10);
        JPanel HourPanel = new JPanel(new GridLayout(2, 2));
        HourPanel.add(fromHourLabel, constraints);
        HourPanel.add(fromHourTextField, constraints);

        // Add the to hour label and text field
        toHourLabel = new JLabel("Start Hour (to):");
        toHourTextField = new JTextField(10);
        HourPanel.add(toHourLabel, constraints);
        HourPanel.add(toHourTextField, constraints);

        // Add the capacity label and text field
        capacityLabel = new JLabel("Capacity:");
        capacityTextField = new JTextField(9);
        JPanel capacityPanel = new JPanel(new GridLayout(2, 1));
        capacityPanel.add(capacityLabel, constraints);
        capacityPanel.add(capacityTextField, constraints);

        // Add the trainer ID label and text field
        trainerIdLabel = new JLabel("Trainer ID:");
        trainerIdTextField = new JTextField(9);
        JPanel trainerIdPanel = new JPanel(new GridLayout(2, 1));
        trainerIdPanel.add(trainerIdLabel, constraints);
        trainerIdPanel.add(trainerIdTextField, constraints);

        // Add the panels to the traing report panel
        trainingReportPanel.add(namePanel);
        trainingReportPanel.add(datePanel);
        trainingReportPanel.add(HourPanel);
        trainingReportPanel.add(capacityPanel);
        trainingReportPanel.add(roomPanel);
        trainingReportPanel.add(trainerIdPanel);

        // Add the "Generate Report" button
        generateReportButton = new JButton("Generate Report");
        JButton exportButton = new JButton("Export to PDF");
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(generateReportButton);
        buttonPanel.add(exportButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(trainingReportPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Hour", "Capacity", "Room", "Trainer ID"}, 0);
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
                String room = roomTextField.getText();
                String fromDate = fromDateTextField.getText();
                String toDate = toDateTextField.getText();
                String fromHour = fromHourTextField.getText();
                String toHour = toHourTextField.getText();
                String capacity = capacityTextField.getText();
                String trainerId = String.valueOf(employeeID);

                // Validate the data
                if (!ValidateData.ValidateDataRange(fromDate, toDate) || !ValidateData.ValidateHourRange(fromHour, toHour) || !ValidateData.ValidateCapacity(capacity)) {
                    return;
                }

                // Set the empty fields to %
                name = name.isEmpty() ? "%" : name;
                room = room.isEmpty() ? "0" : room;
                fromDate = fromDate.isEmpty() ? "1900-01-01" : fromDate;
                toDate = toDate.isEmpty() ? "2100-01-01" : toDate;
                fromHour = fromHour.isEmpty() ? "00:00" : fromHour;
                toHour = toHour.isEmpty() ? "23:59" : toHour;
                capacity = capacity.isEmpty() ? "0" : capacity;

                // Send the message to the server
                message.sendTrainingReportMessage(SendToServer, name + "," + fromDate + "," + toDate + "," + fromHour + "," + toHour + "," + capacity + "," + room + "," + trainerId + "," + "0");

                // Clear the report table
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

        // Add action listener to the "Export to PDF" button
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                utils.ExportToPDF.ExportTableToPDF(reportTableModel, "report.pdf");
            }
        });
    }
}