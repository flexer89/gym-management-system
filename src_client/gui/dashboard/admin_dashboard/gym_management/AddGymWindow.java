package gui.dashboard.admin_dashboard.gym_management;

import utils.CustomLogger;
import utils.Message;
import utils.ValidateData;

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
import javax.swing.JTextField;

public class AddGymWindow extends JFrame {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField postalCodeField;
    private JTextField cityField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton addButton;
    private JButton cancelButton;

    public AddGymWindow(Message message, BufferedReader readFromServer, PrintWriter sendToServer) {
        // Create the main window
        this.setSize(400, 300);
        this.setVisible(true);
        this.setTitle("Add Gym");

        // Create the panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Add the name field
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(new JLabel("Name:"), c);

        c.gridx = 1;
        c.gridy = 0;
        nameField = new JTextField(20);
        formPanel.add(nameField, c);

        // Add the address field
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(new JLabel("Address:"), c);

        c.gridx = 1;
        c.gridy = 1;
        addressField = new JTextField(20);
        formPanel.add(addressField, c);

        // Add the postal code field
        c.gridx = 0;
        c.gridy = 2;
        formPanel.add(new JLabel("Postal Code:"), c);

        c.gridx = 1;
        c.gridy = 2;
        postalCodeField = new JTextField(20);
        formPanel.add(postalCodeField, c);

        // Add the city field
        c.gridx = 0;
        c.gridy = 3;
        formPanel.add(new JLabel("City:"), c);

        c.gridx = 1;
        c.gridy = 3;
        cityField = new JTextField(20);
        formPanel.add(cityField, c);

        // Add the phone field
        c.gridx = 0;
        c.gridy = 4;
        formPanel.add(new JLabel("Phone:"), c);

        c.gridx = 1;
        c.gridy = 4;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, c);

        // Add the email field
        c.gridx = 0;
        c.gridy = 5;
        formPanel.add(new JLabel("Email:"), c);

        c.gridx = 1;
        c.gridy = 5;
        emailField = new JTextField(20);
        formPanel.add(emailField, c);

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
                String name = nameField.getText();
                String address = addressField.getText();
                String postalCode = postalCodeField.getText();
                String city = cityField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();

                // Validate the input
                if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || city.isEmpty() || phone.isEmpty()
                        || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // validate data
                if (!ValidateData.validateMail(email) || !ValidateData.validatePhoneNumber(phone) || !ValidateData.ValidatePostalCode(postalCode) || !ValidateData.validateName(name) || !ValidateData.validateName(city)) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Send the data to the server
                message.sendAddGymMessage(sendToServer, name + "," + address + "," + postalCode + "," + city + "," + phone + "," + email );

                // Read the response from the server
                try {
                    String response = readFromServer.readLine();
                    if (response.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Gym added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error adding gym!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    CustomLogger.logError("Error reading from server: " + e1.getMessage());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}