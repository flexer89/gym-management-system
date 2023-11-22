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

public class PaymentReportWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel fromDateLabel;
    private JTextField fromDateField;
    private JLabel toDateLabel;
    private JTextField toDateField;
    private JLabel paymentMethodLabel;
    private JComboBox<String> paymentMethodComboBox;
    private JButton generateReportButton;
    private JTextField minimumPaymentField;
    private JTextField maximumPaymentField;
    private JLabel maximumPaymentLabel;
    private JLabel minimumPaymentLabel;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public PaymentReportWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(800, 600);
        this.setVisible(true);
        this.setTitle("Gym Management System | Payment Report");

        // Create the main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Create the payment report panel
        JPanel paymentReportPanel = new JPanel();
        paymentReportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Report"));

        // Add the "From Date" label and text field
        fromDateLabel = new JLabel("From Date:");
        fromDateField = new JTextField(6);
        
        JPanel fromDatePanel = new JPanel(new GridLayout(2, 1));
        fromDatePanel.add(fromDateLabel, constraints);
        fromDatePanel.add(fromDateField, constraints);

        // Add the "To Date" label and text field
        toDateLabel = new JLabel("To Date:");
        toDateField = new JTextField(6);
        JPanel toDatePanel = new JPanel(new GridLayout(2, 1));
        toDatePanel.add(toDateLabel, constraints);
        toDatePanel.add(toDateField, constraints);

        // Add the "Minimum Payment" label and text field
        minimumPaymentLabel = new JLabel("Minimum Payment:");
        minimumPaymentField = new JTextField(6);
        JPanel minimumPaymentPanel = new JPanel(new GridLayout(2, 1));
        minimumPaymentPanel.add(minimumPaymentLabel, constraints);
        minimumPaymentPanel.add(minimumPaymentField, constraints);

        // Add the "Maximum Payment" label and text field
        maximumPaymentLabel = new JLabel("Maximum Payment:");
        maximumPaymentField = new JTextField(6);
        JPanel maximumPaymentPanel = new JPanel(new GridLayout(2, 1));
        maximumPaymentPanel.add(maximumPaymentLabel, constraints);
        maximumPaymentPanel.add(maximumPaymentField, constraints);

        // Add the "Payment Method" label and combo box
        paymentMethodLabel = new JLabel("Payment Method:");
        String[] paymentMethods = {"", "cash", "card", "blik"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        JPanel paymentMethodPanel = new JPanel(new GridLayout(2, 1));
        paymentMethodPanel.add(paymentMethodLabel, constraints);
        paymentMethodPanel.add(paymentMethodComboBox, constraints);

        // Add the "From Date" and "To Date" panels to the payment report panel
        paymentReportPanel.add(fromDatePanel);
        paymentReportPanel.add(toDatePanel);
        paymentReportPanel.add(minimumPaymentPanel);
        paymentReportPanel.add(maximumPaymentPanel);
        paymentReportPanel.add(paymentMethodPanel);

        // Add the "Generate Report" button
        generateReportButton = new JButton("Generate Report");
        mainPanel.add(generateReportButton);
        mainPanel.add(paymentReportPanel);

        // Add the report table
        reportTableModel = new DefaultTableModel(new String[]{"ID", "Payment Date", "Amount", "Payment Method", "Client ID"}, 0);
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
                String fromDate = fromDateField.getText();
                String toDate = toDateField.getText();
                String minimumPayment = minimumPaymentField.getText();
                String maximumPayment = maximumPaymentField.getText();
                String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

                // validate data
                if (!ValidateData.ValidateDataRange(fromDate, toDate) || !ValidateData.ValidatePaymentOption(paymentMethod) || !ValidateData.ValidatePaymentAmount(minimumPayment, maximumPayment)) {
                    return;
                }

                // if any value is empty, set it to default
                fromDate = fromDate.isEmpty() ? "1900-01-01" : fromDate;
                toDate = toDate.isEmpty() ? "2100-01-01" : toDate;
                minimumPayment = minimumPayment.isEmpty() ? "0" : minimumPayment;
                maximumPayment = maximumPayment.isEmpty() ? "999999999" : maximumPayment;
                paymentMethod = paymentMethod.isEmpty() ? "all" : paymentMethod;


                // Send the report data to the server
                message.sendPaymentReportMessage(SendToServer, fromDate + "," + toDate + "," + minimumPayment + "," + maximumPayment + "," + paymentMethod + ",0");

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
                } catch (IOException e1) {
                    System.out.println("Error reading payment report: " + e1.getMessage());
                }
            }
        });
    }
}