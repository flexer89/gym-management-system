package gui.dashboard.admin_dashboard.employee_management;

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
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddEmployeeWindow extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField positionField;
    private JTextField dateOfBirthField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField loginField;
    private JTextField dateOfEmploymentField;
    private JButton addButton;
    private JButton cancelButton;
    private BufferedReader readFromServer;
    private PrintWriter sendToServer;

    public AddEmployeeWindow(Message message, BufferedReader readFromServer, PrintWriter sendToServer) {
        // Create the main window
        this.setSize(400, 400);
        this.setVisible(true);
        this.setTitle("Add Employee");
        this.readFromServer = readFromServer;
        this.sendToServer = sendToServer;

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

        // Add the surname field
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(new JLabel("Surname:"), c);

        c.gridx = 1;
        c.gridy = 1;
        surnameField = new JTextField(20);
        formPanel.add(surnameField, c);

        // Add the position field
        c.gridx = 0;
        c.gridy = 2;
        formPanel.add(new JLabel("Position"), c);

        c.gridx = 1;
        c.gridy = 2;
        positionField = new JTextField(20);
        formPanel.add(positionField, c);

        // Add the date of birth field
        c.gridx = 0;
        c.gridy = 3;
        formPanel.add(new JLabel("Date of birth:"), c);

        c.gridx = 1;
        c.gridy = 3;
        dateOfBirthField = new JTextField(20);
        formPanel.add(dateOfBirthField, c);

        // Add the date of employment field
        c.gridx = 0;
        c.gridy = 4;
        formPanel.add(new JLabel("Date of employment:"), c);

        c.gridx = 1;
        c.gridy = 4;
        dateOfEmploymentField = new JTextField(20);
        formPanel.add(dateOfEmploymentField, c);

        // Add the phone field
        c.gridx = 0;
        c.gridy = 5;
        formPanel.add(new JLabel("Phone:"), c);

        c.gridx = 1;
        c.gridy = 5;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, c);

        // Add the email field
        c.gridx = 0;
        c.gridy = 6;
        formPanel.add(new JLabel("Email:"), c);

        c.gridx = 1;
        c.gridy = 6;
        emailField = new JTextField(20);
        formPanel.add(emailField, c);

        // Add the login field
        c.gridx = 0;
        c.gridy = 7;
        formPanel.add(new JLabel("Login:"), c);

        c.gridx = 1;
        c.gridy = 7;
        loginField = new JTextField(20);
        formPanel.add(loginField, c);
        

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
                // Get the data from the fields
                String name = nameField.getText();
                String surname = surnameField.getText();
                String position = positionField.getText();
                String dateOfBirth = dateOfBirthField.getText();
                String dateOfEmployment = dateOfBirthField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String login = loginField.getText();

                // Validate the input
                try {
                    LocalDate.parse(dateOfBirth);

                    if (!position.equals("admin") && !position.equals("trainer")) {
                        throw new IllegalArgumentException("Position must be either 'admin' or 'trainer'");
                    }

                    if (login.length() > 255) {
                        throw new IllegalArgumentException("Username is not valid");
                    }

                    if (!email.matches("^(.+)@(\\S+)$")) {
                        throw new IllegalArgumentException("Email is not valid");
                    }
                    if (!phone.matches("\\d{9}")) {
                        throw new IllegalArgumentException("Phone number should only contain 9 digits");
                    }

                    if (name.isEmpty() || surname.isEmpty() || position.isEmpty() || dateOfBirth.isEmpty() || phone.isEmpty()
                            || email.isEmpty() || login.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                } catch (IllegalArgumentException e2) {
                    JOptionPane.showMessageDialog(null, "Error registering employee: " + e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Send the data to the server
                message.sendAddEmployeeMessage(sendToServer, name + "," + surname + "," + position + "," + dateOfBirth + "," + dateOfEmployment + "," + phone + "," + email + "," + login);
                // Read the response from the server
                try {
                    String response = readFromServer.readLine();
                    if (response.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error adding Employee!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
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