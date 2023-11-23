package gui.login;
import java.awt.GridLayout;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import utils.Message;
import utils.ValidateData;

public class RegisterWindow extends JFrame{
    public RegisterWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the register window
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(8, 1));
        this.setVisible(true);

        JPanel registerPanel = new JPanel(new GridLayout(1, 1));
        JButton registerButton = new JButton("Register");

        JPanel usernamePanel = new JPanel(new GridLayout(1, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameTextField = new JTextField();

        JPanel passwordPanel = new JPanel(new GridLayout(1, 2));
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField();

        JPanel surnamePanel = new JPanel(new GridLayout(1, 2));
        JLabel surnameLabel = new JLabel("Surname:");
        JTextField surnameTextField = new JTextField();

        JPanel dateOfBirthPanel = new JPanel(new GridLayout(1, 2));
        JLabel dateOfBirthLabel = new JLabel("Date of birth:");
        JTextField dateOfBirthTextField = new JTextField();

        JPanel phonePanel = new JPanel(new GridLayout(1, 2));
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneTextField = new JTextField();

        JPanel emailPanel = new JPanel(new GridLayout(1, 2));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailTextField = new JTextField();

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        surnamePanel.add(surnameLabel);
        surnamePanel.add(surnameTextField);

        dateOfBirthPanel.add(dateOfBirthLabel);
        dateOfBirthPanel.add(dateOfBirthTextField);

        phonePanel.add(phoneLabel);
        phonePanel.add(phoneTextField);

        emailPanel.add(emailLabel);
        emailPanel.add(emailTextField);

        registerPanel.add(registerButton);

        this.add(usernamePanel);
        this.add(passwordPanel);
        this.add(namePanel);
        this.add(surnamePanel);
        this.add(dateOfBirthPanel);
        this.add(phonePanel);
        this.add(emailPanel);
        this.add(registerPanel);

        SwingUtilities.updateComponentTreeUI(getContentPane());

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the username and password
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                String dateOfBirth = dateOfBirthTextField.getText();
                String phone = phoneTextField.getText();
                String email = emailTextField.getText();

                int userID;

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || surname.isEmpty() || dateOfBirth.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error registering account!", "Fill all fields!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!ValidateData.validateMail(email) || !ValidateData.validatePhoneNumber(phone) || !ValidateData.validateName(name) || !ValidateData.validateName(surname)) {
                    return;
                }

                // Send the username and password to the server
                message.sendRegisterMessage(SendToServer, username + "," + password + "," + name + "," + surname + "," + dateOfBirth + "," + phone + "," + email);

                // Close the register window
                dispose();
                
                // Get the response from the server (the user ID)
                try {
                    userID = Integer.parseInt(ReadFromServer.readLine());
                    System.out.println(userID);
                    if (userID > 0) {
                        JOptionPane.showMessageDialog(null, "Account registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException e1) {
                    System.out.println(utils.Color.ANSI_RED + "Error reading response from server." + utils.Color.ANSI_RESET);
                }
            }
        });
    }
}
