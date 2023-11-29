package gui.dashboard.client_dashboard;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Message;

public class ProfileWindow extends JFrame {
    private JButton changePasswordButton;
    public ProfileWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID) throws IOException {
        // cast the userID to string
        String userIDString = Integer.toString(userID);

        // Send the message to the server
        message.sendGetClientMessage(SendToServer, userIDString);

        // Get the response from the server
        String response = ReadFromServer.readLine();

        // Split the response
        String[] responseSplit = response.split(",");
        String name = responseSplit[0];
        String surname = responseSplit[1];
        String dateOfBirth = responseSplit[2];
        String phoneNumber = responseSplit[3];
        String email = responseSplit[4];

        // Create the main window
        this.setSize(400, 600);
        this.setVisible(true);
        this.setTitle("Gym Management System | My Profile");

        // Create the main panel
        JPanel mainPanel = new JPanel(new GridLayout(2,1));
        
        // Create my profile panel
        JPanel myProfilePanel = new JPanel();
        myProfilePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("My Profile"));
        myProfilePanel.setLayout(new GridLayout(5,1));

        // Create name Label 
        JLabel nameLabel = new JLabel("<html><b>Name: </b> " + name + "</html>");
        nameLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel surnameLabel = new JLabel("<html><b>Surname:</b> " + surname + "</html>");
        surnameLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        surnameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel dateOfBirthLabel = new JLabel("<html><b>Date of Birth: </b> " + dateOfBirth + "</html>");
        dateOfBirthLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        dateOfBirthLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel phoneNumberLabel = new JLabel("<html><b>Phone number: </b> " + phoneNumber + "</html>");
        phoneNumberLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel emailLabel = new JLabel("<html><b>Email: </b> " + email + "</html>");
        emailLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create password reset panel
        JPanel passwordResetPanel = new JPanel();
        passwordResetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Password Reset"));
        passwordResetPanel.setLayout(new GridLayout(3,1));

        // Create new password panel
        JPanel newPasswordPanel = new JPanel();
        newPasswordPanel.setLayout(new GridLayout(2,1));
        JLabel newPasswordLabel = new JLabel("New password: ");
        JTextField newPasswordTextField = new JTextField();
        newPasswordPanel.add(newPasswordLabel);
        newPasswordPanel.add(newPasswordTextField);

        // Create confirm password panel
        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.setLayout(new GridLayout(2,1));
        JLabel confirmPasswordLabel = new JLabel("Confirm password: ");
        JTextField confirmPasswordTextField = new JTextField();
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(confirmPasswordTextField);

        // Create Change Password button
        changePasswordButton = new JButton("Change Password");

        // Add action listener to the button
        changePasswordButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String newPassword = newPasswordTextField.getText();
                String confirmPassword = confirmPasswordTextField.getText();
        
                try {
                    if (!newPassword.equals(confirmPassword)) {
                        throw new Exception("Passwords do not match");
                    }
                    System.out.println("New password and confirm password match");

                    message.sendChangePasswordMessage(SendToServer, newPassword + "," + userIDString + "," + "client");
                    String status =ReadFromServer.readLine();
                    
                    if (status.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Password changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } 
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Password change failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add all password reset panels to password reset panel
        passwordResetPanel.add(newPasswordPanel);
        passwordResetPanel.add(confirmPasswordPanel);
        passwordResetPanel.add(changePasswordButton);

        // Add all panels to my profile panel
        myProfilePanel.add(nameLabel);
        myProfilePanel.add(surnameLabel);
        myProfilePanel.add(dateOfBirthLabel);
        myProfilePanel.add(phoneNumberLabel);
        myProfilePanel.add(emailLabel);
        mainPanel.add(myProfilePanel);
        mainPanel.add(passwordResetPanel);

        this.add(mainPanel);
    }
}