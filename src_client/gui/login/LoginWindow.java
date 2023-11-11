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

import gui.dashboard.AdminDashboard;
import gui.dashboard.ClientDashboard;
import gui.dashboard.TrainerDashboard;
import utils.Message;

public class LoginWindow extends JFrame{
    public LoginWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, LoginRegisterWindow loginRegisterWindow) {
        // Create the login window
        this.setSize(300, 150);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        // Create the username label and text field
        JPanel usernamePanel = new JPanel(new GridLayout(1, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameTextField = new JTextField();
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        // Create the password label and password field
        JPanel passwordPanel = new JPanel(new GridLayout(1, 2));
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Create the login button
        JButton loginButton = new JButton("Login");

        // Add the components to the window
        this.add(usernamePanel);
        this.add(passwordPanel);
        this.add(loginButton);

        SwingUtilities.updateComponentTreeUI(getContentPane());

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the username and password
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                int userID;
                String type;

                // Send the username and password to the server
                message.sendLoginMessage(SendToServer, username + "," + password);

                // Close the login window
                dispose();

                try {
                    // Read the response from the server (type to know which dashboard should be created and userID to know the user's ID)
                    userID = Integer.parseInt(ReadFromServer.readLine());
                    type = ReadFromServer.readLine();

                    if (userID > 0) {
                        JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loginRegisterWindow.dispose();
                        if (type.equals("admin")) {
                            AdminDashboard adminDashboard = new AdminDashboard();
                        } else if (type.equals("client")) {
                            ClientDashboard clientDashboard = new ClientDashboard();
                        } else if (type.equals("trainer")) {
                            TrainerDashboard trainerDashboard = new TrainerDashboard();
                        }
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        });

        // Show the login window
        this.setVisible(true);
    }
}
